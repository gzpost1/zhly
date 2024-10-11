package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.application.service.impl.ApiArchiveServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import cn.cuiot.dmp.common.bean.dto.SmsBusinessMsgDto;
import cn.cuiot.dmp.common.bean.dto.SysBusinessMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserBusinessMessageAcceptDto;
import cn.cuiot.dmp.common.constant.InformTypeConstant;
import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.cuiot.dmp.common.constant.MsgTypeConstant;
import cn.cuiot.dmp.common.constant.SmsStdTemplate;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionRecordEntity;
import cn.cuiot.dmp.lease.enums.ChargeMsgTemplateEnum;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.rocketmq.ChargeMsgChannel;
import cn.cuiot.dmp.lease.vo.ChargeCollectionManageVo;
import cn.cuiot.dmp.lease.vo.ChargeCollectionRecordVo;
import cn.cuiot.dmp.util.Sm4;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 收费管理-催缴管理
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@Slf4j
@Service
public class ChargeCollectionManageService {

    @Autowired
    private ChargeCollectionRecordService chargeCollectionRecordService;
    @Autowired
    private TbChargeManagerService chargeManagerService;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private ChargeMsgChannel chargeMsgChannel;
    @Autowired
    private ApiArchiveServiceImpl apiArchiveService;

    /**
     * 分页
     */
    public IPage<ChargeCollectionManageVo> queryForPage(ChargeCollectionManageQuery query) {
        IPage<ChargeCollectionManageVo> page = chargeManagerService.queryCollectionManagePage(query);
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            //手机号解密
            page.getRecords().forEach(item -> {
                if (StringUtils.isNotBlank(item.getCustomerUserPhone())) {
                    item.setCustomerUserPhone(Sm4.decrypt(item.getCustomerUserPhone()));
                }
            });

            List<Long> customerUserIds = page.getRecords().stream().map(ChargeCollectionManageVo::getCustomerUserId)
                    .filter(Objects::nonNull).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(customerUserIds)) {
                //查询记录统计
                ChargeCollectionRecordStatisticsQuery statisticsQuery = new ChargeCollectionRecordStatisticsQuery();
                statisticsQuery.setCompanyId(query.getCompanyId());
                statisticsQuery.setCustomerUserIds(customerUserIds);
                List<ChargeCollectionRecordStatisticsDto> recordStatisticsDtoList = chargeCollectionRecordService.getStatistics(statisticsQuery);
                Map<Long, ChargeCollectionRecordStatisticsDto> recordStatisticsMap = recordStatisticsDtoList.stream()
                        .collect(Collectors.toMap(ChargeCollectionRecordStatisticsDto::getCustomerUserId, e -> e));

                //设置上次催款时间、累计催款次数
                page.getRecords().forEach(item -> {
                    if (recordStatisticsMap.containsKey(item.getCustomerUserId())) {
                        ChargeCollectionRecordStatisticsDto recordStatisticsDto = recordStatisticsMap.get(item.getCustomerUserId());
                        item.setLastNoticeTime(recordStatisticsDto.getLastNoticeTime());
                        item.setTotalNoticeNum(recordStatisticsDto.getTotalNoticeNum());
                    }
                });
            }
        }
        return page;
    }

    /**
     * 催款记录
     */
    public IPage<ChargeCollectionRecordVo> record(ChargeCollectionManageRecordQuery query) {
        IPage<ChargeCollectionRecordVo> page = chargeCollectionRecordService.record(query);
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<Long> createUserIds = page.getRecords().stream().map(ChargeCollectionRecordVo::getCreateUser)
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            Map<Long, BaseUserDto> systemUserMap = getSystemUserMap(createUserIds);

            page.getRecords().forEach(item -> {
                Long createUser = item.getCreateUser();
                if (systemUserMap.containsKey(createUser)) {
                    //设置操作人名称
                    item.setOperatorName(systemUserMap.get(createUser).getName());
                }
            });
        }
        return page;
    }

    /**
     * 根据通知单创建用户id查询系统用户名称并返回map
     *
     * @Param createUserIds 创建人ids
     */
    private Map<Long, BaseUserDto> getSystemUserMap(List<Long> createUserIds) {
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setUserIdList(createUserIds);
        List<BaseUserDto> baseUserDtoList = systemToFlowService.lookUpUserList(baseUserReqDto);
        return baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, e -> e,(o1,o2)->o1));
    }

    public void sengMsg(ChargeCollectionManageSendQuery query) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            IPage<ChargeCollectionManageSendDto> page = chargeManagerService.queryUserArrearsStatistics(new Page<>(pageNo.getAndAdd(1), pageSize), query);
            pages = page.getPages();

            List<ChargeCollectionManageSendDto> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {

                //获取用户id列表
                List<Long> customerUserIds = records.stream().map(ChargeCollectionManageSendDto::getCustomerUserId)
                        .collect(Collectors.toList());

                //判断消息类型
                if (Objects.equals(query.getMsgType(), InformTypeConstant.SMS)) {
                    //绑定用户id
                    bindUserId(records);
                    List<SmsBusinessMsgDto> msgDto = constructorSmsBusinessMsgDto(records);
                    if (CollectionUtils.isNotEmpty(msgDto)) {
                        log.info("==============催款管理发送短信==============");
                        //发送短信
                        UserBusinessMessageAcceptDto messageAcceptDto = new UserBusinessMessageAcceptDto().setMsgType(InformTypeConstant.SMS).setSmsMsgDto(msgDto);
                        chargeMsgChannel.userBusinessMessageOutput().send(MessageBuilder.withPayload(messageAcceptDto)
                                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                                .build());
                    }
                } else {
                    //绑定用户id
                    bindUserId(records);

                    List<SysBusinessMsgDto> collect = constructorSysBusinessMsgDtos(records);
                    if (CollectionUtils.isNotEmpty(collect)) {
                        log.info("==============催款管理发送系统信息==============");
                        //发送系统消息
                        UserBusinessMessageAcceptDto msgDto = new UserBusinessMessageAcceptDto().setMsgType(InformTypeConstant.SYS_MSG).setSysMsgDto(collect);
                        chargeMsgChannel.userBusinessMessageOutput().send(MessageBuilder.withPayload(msgDto)
                                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                                .build());
                    } else {
                        log.info("==============催款管理发送系统信息失败，用户id为空==============");
                    }
                }

                //保存记录
                if (CollectionUtils.isNotEmpty(customerUserIds)) {
                    saveRecord(customerUserIds, query.getCompanyId(), query.getMsgType(), query.getOperationType());
                }

            }
        } while (pageNo.get() <= pages);
    }

    /**
     * 构造系统消息
     */
    private List<SysBusinessMsgDto> constructorSysBusinessMsgDtos(List<ChargeCollectionManageSendDto> list) {
        return list.stream().filter(e -> Objects.nonNull(e.getUserId())).map(item -> {
            SysBusinessMsgDto msgDto = new SysBusinessMsgDto();
            msgDto.setSendId(LoginInfoHolder.getCurrentUserId());
            msgDto.setAccepter(item.getUserId());
            msgDto.setDataType(MsgDataType.COLLECTION_NOTICE);
            msgDto.setMsgType(MsgTypeConstant.CHARGE_COLLECTION_NOTICE);
            msgDto.setMessageTime(new Date());
            msgDto.setDataJson(JsonUtil.writeValueAsString(item));
            msgDto.setMessage(fillTemplate(item.getTotal(), item.getAmount()));
            msgDto.setBuildingId(item.getBuildingId());
            msgDto.setUserType(UserTypeEnum.OWNER.getValue());
            return msgDto;
        }).collect(Collectors.toList());
    }

    /**
     * 构造短信消息
     */
    private List<SmsBusinessMsgDto> constructorSmsBusinessMsgDto(List<ChargeCollectionManageSendDto> list) {
        log.info("constructorSmsBusinessMsgDto--list:{}",list);
        List<Long> userIds = list.stream().map(ChargeCollectionManageSendDto::getUserId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, BaseUserDto> dtoMap = getSystemUserMap(userIds);
        return list.stream().map(item -> {
            if (dtoMap.containsKey(item.getUserId())) {
                String phoneNumber = dtoMap.get(item.getUserId()).getPhoneNumber();
                if (StringUtils.isNotBlank(phoneNumber)) {
                    SmsBusinessMsgDto msgDto = new SmsBusinessMsgDto();
                    msgDto.setParams(Arrays.asList(item.getTotal(), item.getAmount()));
                    msgDto.setTelNumbers(phoneNumber);
                    msgDto.setCompanyId(Long.parseLong(dtoMap.get(item.getUserId()).getOrgId()));
                    msgDto.setTemplateId(SmsStdTemplate.CLIENT_PAST_DUE_NOTICE);
                    return msgDto;
                }
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 填充通知模板
     */
    private static String fillTemplate(Object... args) {
        return String.format(ChargeMsgTemplateEnum.COLLECTION_NOTICE_TEMPLATE.getDesc(), args);
    }

    /**
     * 保存催款记录
     *
     * @Param customerUserIds 客户ids
     * @Param companyId 公司id
     * @Param channel 消息发送类型（1：系统消息；2：短信）
     * @Param operationType 操作类型（1：手动催款；2：自动催款）
     */
    private void saveRecord(List<Long> customerUserIds, Long companyId, Byte channel, Byte operationType) {
        Long currentUserId = LoginInfoHolder.getCurrentUserId();
        List<ChargeCollectionRecordEntity> recordList = customerUserIds.stream().map(item -> {
            ChargeCollectionRecordEntity record = new ChargeCollectionRecordEntity();
            record.setId(IdWorker.getId());
            record.setChannel(channel);
            record.setCompanyId(companyId);
            record.setCustomerUserId(item);
            record.setType(operationType);
            record.setCreateUser(currentUserId);
            record.setDate(new Date());
            return record;
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(recordList)) {
            chargeCollectionRecordService.batchSave(recordList);
        }
    }

    /**
     * 根据客户id绑定用户id
     */
    private void bindUserId(List<ChargeCollectionManageSendDto> records) {
        //获取用户id列表
        List<Long> customerUserIds = records.stream().map(ChargeCollectionManageSendDto::getCustomerUserId)
                .collect(Collectors.toList());
        CustomerUseReqDto dto = new CustomerUseReqDto();
        dto.setCustomerIdList(customerUserIds);
        List<CustomerUserRspDto> userList = apiArchiveService.lookupCustomerUsers(dto);
        if (CollectionUtils.isNotEmpty(userList)) {
            Map<Long, CustomerUserRspDto> map = userList.stream().collect(Collectors.toMap(CustomerUserRspDto::getCustomerId, e -> e,(o1,o2)->o1));
            records.forEach(item -> {
                if (map.containsKey(item.getCustomerUserId())) {
                    CustomerUserRspDto user = map.get(item.getCustomerUserId());
                    item.setUserId(user.getUserId());
                }
            });
        }
    }
}