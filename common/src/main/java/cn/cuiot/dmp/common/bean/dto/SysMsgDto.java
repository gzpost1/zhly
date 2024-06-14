package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/** 系统消息
 * @author hantingyao
 * @Description
 * @data 2024/6/14 10:26
 */
@Data
@Accessors(chain = true)
public class SysMsgDto {

    /**
     * 发送人
     */
    private Long sendId;

    /**
     * 接受人
     */
    private List<Long> acceptors;

    /**
     * 数据ID
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
     * 补充消息，可根据分类另行处理
     */
    private Object dataJson;

    /**
     * 消息发送时间
     */
    private Date messageTime;
}
