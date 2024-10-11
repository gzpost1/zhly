package cn.cuiot.dmp.message.consumer;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.bean.dto.*;
import cn.cuiot.dmp.common.constant.InformTypeConstant;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.message.config.MqMsgChannel;
import cn.cuiot.dmp.message.conver.UserMessageConvert;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.cuiot.dmp.sms.query.SmsSendQuery;
import cn.cuiot.dmp.sms.service.SmsSendService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/27 10:25
 */
@Component
@Slf4j
public class UserMsgConsumer {

    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private SystemApiFeignService systemApiFeignService;
    @Autowired
    private SmsSendService smsSendService;

    @StreamListener(MqMsgChannel.USERMESSAGEINPUT)
    @Transactional(rollbackFor = Exception.class)
    public void userMessageConsumer(@Payload UserMessageAcceptDto userMessageAcceptDto) {
        log.info("userMessageInput:{}", JsonUtil.writeValueAsString(userMessageAcceptDto));
        if ((userMessageAcceptDto.getMsgType() & InformTypeConstant.SYS_MSG) == InformTypeConstant.SYS_MSG) {
            if (userMessageAcceptDto.getSysMsgDto() == null) {
                return;
            }
            UserMessageEntity userMessage = UserMessageConvert.INSTANCE.concert(userMessageAcceptDto.getSysMsgDto());
            userMessage.init();
            List<UserMessageEntity> userMessageEntities = dealMsgByType(userMessage, userMessageAcceptDto.getSysMsgDto());
            userMessageService.saveBatch(userMessageEntities);
        }
        //短信
        if ((userMessageAcceptDto.getMsgType() & InformTypeConstant.SMS) == InformTypeConstant.SMS) {
            if (userMessageAcceptDto.getSmsMsgDto() == null) {
                return;
            }
            SmsMsgDto smsMsgDto = userMessageAcceptDto.getSmsMsgDto();
            BaseUserReqDto query = new BaseUserReqDto();
            query.setUserIdList(smsMsgDto.getUserIds());
            List<BaseUserDto> userDtoList = systemApiFeignService.lookUpUserList(query).getData();
            log.info("lookUpUserList-result:{}", JsonUtil.writeValueAsString(userDtoList));
            if (CollUtil.isNotEmpty(userDtoList)) {
                Map<String, List<BaseUserDto>> groupByOrgId = userDtoList.stream().filter(o -> Objects.nonNull(o.getOrgId())).collect(Collectors.groupingBy(BaseUserDto::getOrgId));
                for (String key : groupByOrgId.keySet()) {
                    List<BaseUserDto> baseUserDtos = groupByOrgId.get(key);
                    List<String> collect = baseUserDtos.stream().map(BaseUserDto::getPhoneNumber).filter(StrUtil::isNotEmpty).distinct().collect(Collectors.toList());
                    String mobile = String.join(",", collect);
                    SmsSendQuery smsSendQuery = new SmsSendQuery();
                    smsSendQuery.setMobile(mobile).setParams(smsMsgDto.getParams()).setStdTemplate(smsMsgDto.getTemplateId()).setCompanyId(Long.parseLong(key));
                    log.info("发送短信：{}", JsonUtil.writeValueAsString(smsSendQuery));
                    smsSendService.sendMsg(smsSendQuery);
                }
            }
        }
    }

    /**
     * 通知用户消费（针对相同模板不同参数）
     */
    @StreamListener(MqMsgChannel.USEBUSINESSRMESSAGEINPUT)
    public void userBusinessMessageConsumer(@Payload UserBusinessMessageAcceptDto dto) {
        log.info("userBusinessMessageInput:{}", JsonUtil.writeValueAsString(dto));
        if (Objects.equals(dto.getMsgType(), InformTypeConstant.SYS_MSG)) {
            if (CollectionUtils.isEmpty(dto.getSysMsgDto())) {
                return;
            }
            List<UserMessageEntity> userMessageEntities = dto.getSysMsgDto().stream().map(item -> {
                UserMessageEntity userMessage = new UserMessageEntity();
                BeanUtils.copyProperties(item, userMessage);
                userMessage.init();
                return userMessage;
            }).collect(Collectors.toList());
            userMessageService.saveBatch(userMessageEntities);
        }
        //短信
        if ((dto.getMsgType() & InformTypeConstant.SMS) == InformTypeConstant.SMS) {
            if (CollUtil.isEmpty(dto.getSmsMsgDto())) {
                return;
            }
            List<SmsBusinessMsgDto> smsMsgDto = dto.getSmsMsgDto();
            smsMsgDto.forEach(item -> {
                SmsSendQuery smsSendQuery = new SmsSendQuery();
                smsSendQuery.setMobile(item.getTelNumbers()).setCompanyId(item.getCompanyId()).setParams(item.getParams()).setStdTemplate(item.getTemplateId());
                log.info("发送短信：{}", JsonUtil.writeValueAsString(smsSendQuery));
                smsSendService.sendMsg(smsSendQuery);
            });

        }
    }

    //TODO
    private List<UserMessageEntity> dealMsgByType(UserMessageEntity userMessage, SysMsgDto sysMsgDto) {

        List<UserMessageEntity> userMessageEntities = new ArrayList<>();
        sysMsgDto.getAcceptors().forEach(acceptor -> {
            UserMessageEntity userMessageEntity = BeanUtil.copyProperties(userMessage, UserMessageEntity.class);
            userMessageEntity.setAccepter(acceptor);
            userMessageEntities.add(userMessageEntity);
        });
        return userMessageEntities;
    }
}
