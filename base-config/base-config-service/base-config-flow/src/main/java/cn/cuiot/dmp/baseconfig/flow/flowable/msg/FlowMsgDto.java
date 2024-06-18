package cn.cuiot.dmp.baseconfig.flow.flowable.msg;

import lombok.Data;

import java.util.List;

/**
 * @Description 消息发送
 * @Date 2024/6/17 17:44
 * @Created by libo
 */
@Data
public class FlowMsgDto {
    /**
     * 发送人
     */
    private List<Long> users;
    /**
     * 数据id
     */
    private Long dataId;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据json
     */
    private String dataJson;

    /**
     * 模版id
     */
    private String templateId;
}
