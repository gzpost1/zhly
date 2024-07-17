package cn.cuiot.dmp.message.param;//	模板

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/7 16:45
 */
@Data
public class MessagePageQuery extends PageQuery {

    /**
     * 是否已读
     */
    @NotNull(message = "是否已读不能为空")
    private Byte readStatus;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 消息时间筛选 - 大于等于
     */
    private Date messageGtTime;

    /**
     * 消息时间筛选 - 小于等于
     */
    private Date messageLeTime;

    /**
     * 消息分类
     */
    private String msgType;

    /**
     * 楼盘id(客户端必传)
     */
    private Long buildingId;

}
