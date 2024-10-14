package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.application.service.impl.ApiArchiveServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import cn.cuiot.dmp.common.bean.dto.SmsBusinessMsgDto;
import cn.cuiot.dmp.common.bean.dto.SysBusinessMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserBusinessMessageAcceptDto;
import cn.cuiot.dmp.common.constant.*;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.ChargeNoticeBuildingEntity;
import cn.cuiot.dmp.lease.entity.charge.ChargeNoticeEntity;
import cn.cuiot.dmp.lease.entity.charge.ChargeNoticeItemEntity;
import cn.cuiot.dmp.lease.enums.ChargeMsgTemplateEnum;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.mapper.charge.ChargeNoticeBuildingMapper;
import cn.cuiot.dmp.lease.mapper.charge.ChargeNoticeItemMapper;
import cn.cuiot.dmp.lease.mapper.charge.ChargeNoticeMapper;
import cn.cuiot.dmp.lease.rocketmq.ChargeMsgChannel;
import cn.cuiot.dmp.lease.vo.ChargeNoticePageVo;
import cn.cuiot.dmp.lease.vo.ChargeNoticeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.*;


/**
 * 收费管理-通知单 业务层
 *
 * @author zc
 */
@Slf4j
@Service
public class ChargeNoticeService extends ServiceImpl<ChargeNoticeMapper, ChargeNoticeEntity> {

    @Autowired
    private ChargeNoticeItemMapper chargeNoticeItemMapper;
    @Autowired
    private ChargeNoticeBuildingMapper chargeNoticeBuildingMapper;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private ChargeMsgChannel chargeMsgChannel;
    @Autowired
    private ApiArchiveServiceImpl apiArchiveService;

    @Autowired
    private ExcelExportService excelExportService;

    public IPage<ChargeNoticePageVo> queryForPage(ChargeNoticePageQuery query) {
        IPage<ChargeNoticePageVo> page = baseMapper.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()), query);
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            //获取通知单当前分页ids列表
            List<Long> ids = page.getRecords().stream().map(ChargeNoticePageVo::getId).collect(Collectors.toList());
            //获取楼盘名称信息
            Map<Long, List<String>> buildingNameMap = getBuildingNameMap(ids);
            //获取收费项目名称信息
            Map<Long, List<String>> chargeItemNameMap = getChargeItemNameMap(ids);

            //获取系统用户信息
            List<Long> userIds = page.getRecords().stream().map(ChargeNoticePageVo::getCreateUser)
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            Map<Long, BaseUserDto> systemUserMap = getSystemUserMap(userIds);

            //设置楼盘名称、收费项目名称、操作人名称
            for (ChargeNoticePageVo vo : page.getRecords()) {
                if (buildingNameMap.containsKey(vo.getId())) {
                    vo.setBuildingsName(String.join(",", buildingNameMap.get(vo.getId())));
                }
                if (chargeItemNameMap.containsKey(vo.getId())) {
                    vo.setChargeItemsName(String.join(",", chargeItemNameMap.get(vo.getId())));
                }
                if (systemUserMap.containsKey(vo.getCreateUser())) {
                    vo.setOperatorName(systemUserMap.get(vo.getCreateUser()).getName());
                }
            }
        }
        return page;
    }

    /**
     * 导出
     * @param dto
     * @throws Exception
     */
    public void export(ChargeNoticePageQuery dto) throws Exception {
        excelExportService.excelExport(ExcelReportDto.<ChargeNoticePageQuery,ChargeNoticePageVo>builder().title("待审批数据").fileName("待审批数据")
                .dataList(queryChargeNotice(dto)).build(),ChargeNoticePageVo.class);
    }

    public List<ChargeNoticePageVo> queryChargeNotice(ChargeNoticePageQuery query){
        Boolean flag =true;
        Long pageNo = 1L;
        query.setPageSize(NumberConst.PAGE_MAX_SIZE);
        List<ChargeNoticePageVo> resultList = new ArrayList<>();
        do{
            query.setPageNo(pageNo);
            IPage<ChargeNoticePageVo> page = baseMapper.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()), query);
            if(page.getTotal()> NumberConst.QUERY_MAX_SIZE){
                throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
            }
            if(CollectionUtils.isEmpty(page.getRecords())){
                flag=false;
            }
            if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
                //获取通知单当前分页ids列表
                List<Long> ids = page.getRecords().stream().map(ChargeNoticePageVo::getId).collect(Collectors.toList());
                //获取楼盘名称信息
                Map<Long, List<String>> buildingNameMap = getBuildingNameMap(ids);
                //获取收费项目名称信息
                Map<Long, List<String>> chargeItemNameMap = getChargeItemNameMap(ids);

                //获取系统用户信息
                List<Long> userIds = page.getRecords().stream().map(ChargeNoticePageVo::getCreateUser)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());
                Map<Long, BaseUserDto> systemUserMap = getSystemUserMap(userIds);

                //设置楼盘名称、收费项目名称、操作人名称
                for (ChargeNoticePageVo vo : page.getRecords()) {
                    if (buildingNameMap.containsKey(vo.getId())) {
                        vo.setBuildingsName(String.join(",", buildingNameMap.get(vo.getId())));
                    }
                    if (chargeItemNameMap.containsKey(vo.getId())) {
                        vo.setChargeItemsName(String.join(",", chargeItemNameMap.get(vo.getId())));
                    }
                    if (systemUserMap.containsKey(vo.getCreateUser())) {
                        vo.setOperatorName(systemUserMap.get(vo.getCreateUser()).getName());
                    }
                    vo.setStatusName(Objects.equals(NumberConst.DATA_STATUS,vo.getStatus())? StatusConst.STOP: StatusConst.ENABLE);
                }
                resultList.addAll(page.getRecords());
            }
        }while (flag);
            return resultList;
    }
    /**
     * 根据通知单id查询楼盘名称并返回map
     *
     * @Param chargeNoticeIds 通知单ids
     */
    private Map<Long, List<String>> getBuildingNameMap(List<Long> chargeNoticeIds) {
        List<ChargeBuildingDto> noticeBuildingVos = chargeNoticeBuildingMapper.getBuildingNamesByChargeNoticeId(chargeNoticeIds);
        Map<Long, List<String>> buildMap = newHashMap();
        if (CollectionUtils.isNotEmpty(noticeBuildingVos)) {
            buildMap = noticeBuildingVos.stream()
                    .collect(Collectors.groupingBy(ChargeBuildingDto::getDataId,
                            Collectors.mapping(ChargeBuildingDto::getBuildingName, Collectors.toList())));
        }
        return buildMap;
    }

    /**
     * 根据通知单id查询收费项目名称并返回map
     *
     * @Param chargeNoticeIds 通知单ids
     */
    private Map<Long, List<String>> getChargeItemNameMap(List<Long> chargeNoticeIds) {
        List<ChargeChargeItemDto> chargeItemVos = chargeNoticeItemMapper.getChargeItemNamesByChargeNoticeId(chargeNoticeIds);
        Map<Long, List<String>> chargeItemMap = newHashMap();
        if (CollectionUtils.isNotEmpty(chargeItemVos)) {
            chargeItemMap = chargeItemVos.stream()
                    .collect(Collectors.groupingBy(ChargeChargeItemDto::getDataId,
                            Collectors.mapping(ChargeChargeItemDto::getChargeItemName, Collectors.toList())));
        }
        return chargeItemMap;
    }

    /**
     * 根据通知单创建用户id查询系统用户名称并返回map
     *
     * @Param createUserIds 创建人ids
     */
    private Map<Long, BaseUserDto> getSystemUserMap(List<Long> userIds) {
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setUserIdList(userIds);
        List<BaseUserDto> baseUserDtoList = systemToFlowService.lookUpUserList(baseUserReqDto);
        return baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, e -> e));
    }

    /**
     * 详情
     *
     * @Param id 参数
     */
    public ChargeNoticeVo queryForDetail(Long id) {
        ChargeNoticeEntity entity = getOptById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "数据不存在"));
        ChargeNoticeVo vo = new ChargeNoticeVo();
        BeanUtils.copyProperties(entity, vo);
        //设置楼盘数据
        List<ChargeNoticeBuildingEntity> buildingList = chargeNoticeBuildingMapper.selectList(
                new LambdaQueryWrapper<ChargeNoticeBuildingEntity>()
                        .eq(ChargeNoticeBuildingEntity::getChargeNoticeId, id));

        if (CollectionUtils.isNotEmpty(buildingList)) {
            List<Long> buildCollect = buildingList.stream().map(ChargeNoticeBuildingEntity::getBuildingId)
                    .collect(Collectors.toList());
            vo.setBuildings(buildCollect);
        }

        List<ChargeNoticeItemEntity> chargeItemList = chargeNoticeItemMapper.selectList(
                new LambdaQueryWrapper<ChargeNoticeItemEntity>()
                        .eq(ChargeNoticeItemEntity::getChargeNoticeId, id));
        if (CollectionUtils.isNotEmpty(chargeItemList)) {
            List<Long> chargeItemCollect = chargeItemList.stream().map(ChargeNoticeItemEntity::getChargeItemId)
                    .collect(Collectors.toList());
            vo.setChargeItems(chargeItemCollect);
        }
        return vo;
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(ChargeNoticeCreateDto dto) {
        if (dto.getOwnershipPeriodEnd().before(dto.getOwnershipPeriodBegin())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "所属账期结束时间不能小于开始时间");
        }
        ChargeNoticeEntity entity = new ChargeNoticeEntity();
        entity.setId(IdWorker.getId());
        entity.setOwnershipPeriodBegin(dto.getOwnershipPeriodBegin());
        entity.setOwnershipPeriodEnd(dto.getOwnershipPeriodEnd());
        entity.setRemark(dto.getRemark());
        entity.setStatus(EntityConstants.ENABLED);
        entity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        //保存通知单
        this.save(entity);

        //保存楼盘信息
        List<ChargeNoticeBuildingEntity> buildingList = dto.getBuildings().stream().map(item -> {
            ChargeNoticeBuildingEntity billBuilding = new ChargeNoticeBuildingEntity();
            billBuilding.setChargeNoticeId(entity.getId());
            billBuilding.setBuildingId(item);
            return billBuilding;
        }).collect(Collectors.toList());
        chargeNoticeBuildingMapper.batchInsert(buildingList);

        //保存收费项目信息
        List<ChargeNoticeItemEntity> chargeItemList = dto.getChargeItems().stream().map(item -> {
            ChargeNoticeItemEntity chargeNoticeItemEntity = new ChargeNoticeItemEntity();
            chargeNoticeItemEntity.setChargeNoticeId(entity.getId());
            chargeNoticeItemEntity.setChargeItemId(item);
            return chargeNoticeItemEntity;
        }).collect(Collectors.toList());
        chargeNoticeItemMapper.batchInsert(chargeItemList);
    }

    public void updateStatus(UpdateStatusParam param) {
        ChargeNoticeEntity entity = getOptById(param.getId())
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "数据不存在"));
        entity.setStatus(param.getStatus());
        updateById(entity);
    }

    /**
     * 删除
     *
     * @Param id 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //删除账单
        removeById(id);

//        //删除楼盘信息
//        chargeNoticeBuildingMapper.delete(new LambdaQueryWrapper<ChargeNoticeBuildingEntity>()
//                .eq(ChargeNoticeBuildingEntity::getChargeNoticeId, id));
//
//        //删除收费项目信息
//        chargeNoticeItemMapper.delete(new LambdaQueryWrapper<ChargeNoticeItemEntity>()
//                .eq(ChargeNoticeItemEntity::getChargeNoticeId, id));
    }

    /**
     * 发送消息
     */
    public void sengMsg(ChargeNoticeSendQuery query) {
        ChargeNoticeEntity noticeEntity = getOptById(query.getId())
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "数据不存在"));
        AssertUtil.isFalse(Objects.equals(noticeEntity.getStatus(),EntityConstants.DISABLED),"数据已停用");

        query.setCompanyId(noticeEntity.getCompanyId());
        query.setOwnershipPeriodBegin(noticeEntity.getOwnershipPeriodBegin());
        query.setOwnershipPeriodEnd(noticeEntity.getOwnershipPeriodEnd());

        //页码
        AtomicLong pageNo = new AtomicLong(1);
        //每页大小
        long pageSize = 200;
        //总页数
        long pages;
        do {
            //分页查询需要发消息的应收数据
            IPage<ChargeNoticeSendDto> page = getBaseMapper().queryChargeNoticeInfo(new Page<>(pageNo.getAndAdd(1), pageSize), query);
            pages = page.getPages();

            List<ChargeNoticeSendDto> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                List<Long> chargeItems = records.stream().map(ChargeNoticeSendDto::getChargeItemId)
                        .filter(Objects::nonNull).collect(Collectors.toList());
                //查询收费项目数据
                CommonOptionSettingReqDTO dto = new CommonOptionSettingReqDTO();
                dto.setIdList(chargeItems);
                List<CommonOptionSettingRspDTO> commonOptionSettingRspDtos = systemToFlowService.batchQueryCommonOptionSetting(dto);
                Map<Long, CommonOptionSettingRspDTO> map = commonOptionSettingRspDtos.stream().collect(Collectors.toMap(CommonOptionSettingRspDTO::getId, e -> e));

                records.forEach(item ->{
                    //设置项目名称
                    item.setChargeItemName(map.containsKey(item.getChargeItemId()) ? map.get(item.getChargeItemId()).getName() : "");
                    //设置所属账期-开始时间
                    item.setOwnershipPeriodBegin(noticeEntity.getOwnershipPeriodBegin());
                    //设置所属账期-结束时间
                    item.setOwnershipPeriodEnd(noticeEntity.getOwnershipPeriodEnd());
                    //设置企业id
                    item.setCompanyId(noticeEntity.getCompanyId());

                });

                //判断消息类型
                if (Objects.equals(query.getMsgType(), InformTypeConstant.SMS)) {
                    List<SmsBusinessMsgDto> msgDto = constructorSmsBusinessMsgDto(records);
                    if (CollectionUtils.isNotEmpty(msgDto)) {
                        log.info("==============客户账单通知单发送短信==============");
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
                        log.info("==============客户账单通知单发送系统消息==============");
                        //发送系统消息
                        UserBusinessMessageAcceptDto msgDto = new UserBusinessMessageAcceptDto().setMsgType(InformTypeConstant.SYS_MSG).setSysMsgDto(collect);
                        chargeMsgChannel.userBusinessMessageOutput().send(MessageBuilder.withPayload(msgDto)
                                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                                .build());
                    }else {
                        log.info("==============客户账单通知单发送系统信息失败，用户id为空==============");
                    }
                }
            }
        } while (pageNo.get() <= pages);

    }

    /**
     * 构造系统消息
     */
    private List<SysBusinessMsgDto> constructorSysBusinessMsgDtos(List<ChargeNoticeSendDto> list) {
        return list.stream().filter(e -> Objects.nonNull(e.getUserId())).map(item -> {
            SysBusinessMsgDto msgDto = new SysBusinessMsgDto();
            msgDto.setSendId(LoginInfoHolder.getCurrentUserId());
            msgDto.setAccepter(item.getUserId());
            msgDto.setDataId(item.getId());
            msgDto.setDataType(MsgDataType.CHARGE_NOTICE);
            msgDto.setMsgType(MsgTypeConstant.CHARGE_BILL_NOTICE);
            msgDto.setMessageTime(new Date());
            msgDto.setDataJson(JsonUtil.writeValueAsString(item));
            msgDto.setBuildingId(item.getBuildingId());
            msgDto.setUserType(UserTypeEnum.OWNER.getValue());

            String date = dateFormat(item.getOwnershipPeriodBegin()) + "-" + dateFormat(item.getOwnershipPeriodEnd());
            msgDto.setMessage(fillTemplate(date, item.getChargeItemName()));
            return msgDto;
        }).collect(Collectors.toList());
    }

    /**
     * 构造短信消息
     */
    private List<SmsBusinessMsgDto> constructorSmsBusinessMsgDto(List<ChargeNoticeSendDto> list) {
        List<Long> userIds = list.stream().map(ChargeNoticeSendDto::getCustomerUserId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, BaseUserDto> dtoMap = getSystemUserMap(userIds);

        return list.stream().map(item -> {
            if (dtoMap.containsKey(item.getCustomerUserId())) {
                String phoneNumber = dtoMap.get(item.getCustomerUserId()).getPhoneNumber();
                if (StringUtils.isNotBlank(phoneNumber)) {
                    SmsBusinessMsgDto msgDto = new SmsBusinessMsgDto();
                    String date = dateFormat(item.getOwnershipPeriodBegin()) + "-" + dateFormat(item.getOwnershipPeriodEnd());
                    msgDto.setParams(Arrays.asList(date, item.getChargeItemName()));
                    msgDto.setTelNumbers(phoneNumber);
                    msgDto.setTemplateId(ChargeMsgTemplateEnum.CUSTOMER_NOTICE_TEMPLATE.getTemplateId());
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
        return String.format(ChargeMsgTemplateEnum.CUSTOMER_NOTICE_TEMPLATE.getDesc(), args);
    }

    /**
     * 日期格式化
     */
    private static String dateFormat(Date date) {
        return DateTimeUtil.localDateToString(DateTimeUtil.dateToLocalDate(date), "yyyy年MM月dd日");
    }

    /**
     * 根据客户id绑定用户id
     */
    private void bindUserId(List<ChargeNoticeSendDto> records) {
        //获取用户id列表
        List<Long> customerUserIds = records.stream().map(ChargeNoticeSendDto::getCustomerUserId)
                .collect(Collectors.toList());
        CustomerUseReqDto dto = new CustomerUseReqDto();
        dto.setCustomerIdList(customerUserIds);
        List<CustomerUserRspDto> userList = apiArchiveService.lookupCustomerUsers(dto);
        if (CollectionUtils.isNotEmpty(userList)) {
            Map<Long, CustomerUserRspDto> map = userList.stream().collect(Collectors.toMap(CustomerUserRspDto::getCustomerId, e -> e));
            records.forEach(item ->{
                if (map.containsKey(item.getCustomerUserId())) {
                    CustomerUserRspDto user = map.get(item.getCustomerUserId());
                    item.setUserId(user.getUserId());
                }
            });
        }
    }


}