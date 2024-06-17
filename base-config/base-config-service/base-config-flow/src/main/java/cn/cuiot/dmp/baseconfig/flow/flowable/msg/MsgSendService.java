package cn.cuiot.dmp.baseconfig.flow.flowable.msg;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.baseconfig.flow.config.MsgChannel;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.feign.SystemToFlowService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils;
import cn.cuiot.dmp.common.bean.dto.SmsMsgDto;
import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.cuiot.dmp.common.constant.MsgTypeConstant;
import cn.cuiot.dmp.common.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 消息发送
 * @Date 2024/6/17 17:42
 * @Created by libo
 */
@Slf4j
@Service
public class MsgSendService {
    @Autowired
    private MsgChannel msgChannel;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private WorkInfoService workInfoService;

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(FlowMsgDto msg) {
        try {
            //发送系统消息
            UserMessageAcceptDto SYS_MSG = new UserMessageAcceptDto().setMsgType(MsgTypeConstant.SYS_MSG).setSysMsgDto(
                    new SysMsgDto().setAcceptors(msg.getUsers()).setDataId(msg.getDataId()).setDataType(msg.getDataType()).setMessage(msg.getMessage())
                            .setDataJson(msg.getDataJson()).setMessageTime(new Date()));
            msgChannel.userMessageOutput().send(MessageBuilder.withPayload(SYS_MSG)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());

            //发送短信
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            baseUserReqDto.setUserIdList(msg.getUsers());
            List<BaseUserDto> baseUserDtos = systemToFlowService.lookUpUserList(baseUserReqDto);

            if (CollectionUtils.isNotEmpty(baseUserDtos)) {
                List<String> phones = baseUserDtos.stream().map(BaseUserDto::getPhoneNumber).collect(Collectors.toList());
                SmsMsgDto smsMsgDto = new SmsMsgDto();
                smsMsgDto.setTelNumbers(phones);
                smsMsgDto.setParams(Lists.newArrayList(msg.getMessage()));
                smsMsgDto.setTemplateId(msg.getTemplateId());
                UserMessageAcceptDto SMS_MSG = new UserMessageAcceptDto().setMsgType(MsgTypeConstant.SMS).setSmsMsgDto(smsMsgDto);
                msgChannel.userMessageOutput().send(MessageBuilder.withPayload(SMS_MSG)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
            } else {
                log.error("发送短信失败用户信息为空，{}", msg);
            }

        } catch (Exception e) {
            log.error("流程实例结束监听器发送消息失败", e);
        }
    }

    /**
     * 流程相关发送消息
     *
     * @param processInstanceId
     * @param dataType
     */
    public void sendProcess(String processInstanceId, String dataType) {
        //发送消息
        LambdaQueryWrapper<WorkInfoEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(WorkInfoEntity::getProcInstId, processInstanceId);
        WorkInfoEntity noticeEntity = workInfoService.getOne(queryWrapper);
        if (noticeEntity == null) {
            log.error("流程实例结束监听器未找到流程实例信息");
            return;
        }

        FlowMsgDto flowMsgDto = new FlowMsgDto();
        flowMsgDto.setDataId(noticeEntity.getId());
        flowMsgDto.setDataType(dataType);
        flowMsgDto.setMessage(noticeEntity.getWorkName());
        flowMsgDto.setUsers(Lists.newArrayList(noticeEntity.getActualUserId(), noticeEntity.getCreateUser()));
        flowMsgDto.setDataJson(JsonUtil.writeValueAsString(noticeEntity));
        this.sendMsg(flowMsgDto);
    }


    /**
     * 根据类型获取短信id
     */
    public String getTemplateId(String dataType) {
        return "1";
    }

    /**
     * task发送消息
     *
     * @param processInstanceId 流程定义ID
     * @param nodeType          节点类型
     * @param flag              是否退回
     */
    public void sendProcess(String processInstanceId, String nodeType, boolean flag, String userId) {
        String msgType = getTaskMsgType(nodeType, flag);
        if (StringUtils.isNotBlank(msgType)) {
            //发送消息
            LambdaQueryWrapper<WorkInfoEntity> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(WorkInfoEntity::getProcInstId, processInstanceId);
            WorkInfoEntity noticeEntity = workInfoService.getOne(queryWrapper);
            if (noticeEntity == null) {
                log.error("流程实例结束监听器未找到流程实例信息");
                return;
            }

            FlowMsgDto flowMsgDto = new FlowMsgDto();
            flowMsgDto.setDataId(noticeEntity.getId());
            flowMsgDto.setDataType(msgType);
            flowMsgDto.setMessage(noticeEntity.getWorkName());
            flowMsgDto.setUsers(Lists.newArrayList(Long.valueOf(userId)));
            flowMsgDto.setDataJson(JsonUtil.writeValueAsString(noticeEntity));
        } else {
            log.error("未找到对应的消息类型,processInstanceId:{},nodeType:{},flag:{}", processInstanceId, nodeType, flag);
        }
    }

    private String getTaskMsgType(String nodeType, boolean flag) {
        String type = null;

        if (flag) {
            if (StringUtils.equalsAnyIgnoreCase(nodeType, BpmnModelUtils.Type.ROOT.getType())) {
                type = MsgDataType.WORK_INFO_RETURN_INITIATE;
            }
            if (StringUtils.equalsAnyIgnoreCase(nodeType, BpmnModelUtils.Type.USER_TASK.getType())) {
                type = MsgDataType.WORK_INFO_RETURN_APPROVAL;
            }
            if (StringUtils.equalsAnyIgnoreCase(nodeType, BpmnModelUtils.Type.APPROVE_USER_TASK.getType())) {
                type = MsgDataType.WORK_INFO_RETURN_PROCESS;
            }
        } else {
            if (StringUtils.equalsAnyIgnoreCase(nodeType, BpmnModelUtils.Type.USER_TASK.getType())) {
                type = MsgDataType.WORK_INFO_APPROVAL;
            }
            if (StringUtils.equalsAnyIgnoreCase(nodeType, BpmnModelUtils.Type.APPROVE_USER_TASK.getType())) {
                type = MsgDataType.WORK_INFO_PROCESS;
            }
            if (StringUtils.equalsAnyIgnoreCase(nodeType, BpmnModelUtils.Type.COMMENT.getType())) {
                type = MsgDataType.WORK_INFO_EVALUATE;
            }
        }

        return type;
    }

    /**
     * 发送抄送消息
     *
     * @param processInstanceId
     * @param ccUserIds
     */
    public void sendCCMsg(String processInstanceId, List<Long> ccUserIds) {
        //发送消息
        LambdaQueryWrapper<WorkInfoEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(WorkInfoEntity::getProcInstId, processInstanceId);
        WorkInfoEntity noticeEntity = workInfoService.getOne(queryWrapper);
        if (noticeEntity == null) {
            log.error("流程实例结束监听器未找到流程实例信息");
            return;
        }

        FlowMsgDto flowMsgDto = new FlowMsgDto();
        flowMsgDto.setDataId(noticeEntity.getId());
        flowMsgDto.setDataType(MsgDataType.WORK_INFO_COPY);
        flowMsgDto.setMessage(noticeEntity.getWorkName());
        flowMsgDto.setUsers(ccUserIds);
        flowMsgDto.setDataJson(JsonUtil.writeValueAsString(noticeEntity));
    }
}
