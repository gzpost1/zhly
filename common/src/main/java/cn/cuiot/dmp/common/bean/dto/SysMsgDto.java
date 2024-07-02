package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 系统消息
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/14 10:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysMsgDto extends SysMsgBaseDto {

    /**
     * 接收人（非空）
     */
    private List<Long> acceptors;

    @Override
    public SysMsgDto setSendId(Long sendId) {
        super.setSendId(sendId);
        return this;
    }

    @Override
    public SysMsgDto setDataId(Long dataId) {
        super.setDataId(dataId);
        return this;
    }

    @Override
    public SysMsgDto setDataType(String dataType) {
        super.setDataType(dataType);
        return this;
    }

    @Override
    public SysMsgDto setMsgType(String msgType) {
        super.setMsgType(msgType);
        return this;
    }

    @Override
    public SysMsgDto setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public SysMsgDto setDataJson(Object dataJson) {
        super.setDataJson(dataJson);
        return this;
    }

    @Override
    public SysMsgDto setMessageTime(Date messageTime) {
        super.setMessageTime(messageTime);
        return this;
    }
}
