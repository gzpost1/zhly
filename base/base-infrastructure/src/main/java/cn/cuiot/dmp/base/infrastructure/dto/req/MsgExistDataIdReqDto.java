package cn.cuiot.dmp.base.infrastructure.dto.req;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/14 15:19
 */
@Data
@Accessors(chain = true)
public class MsgExistDataIdReqDto {

    /**
     * 接受人
     */
    private Long accepter;

    /**
     * 数据类型
     */
    private String dataType;
}
