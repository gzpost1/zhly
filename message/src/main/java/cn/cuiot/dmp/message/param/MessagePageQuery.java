package cn.cuiot.dmp.message.param;//	模板

import cn.cuiot.dmp.query.PageQuery;
import lombok.Data;

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
    private Byte readStatus;
}
