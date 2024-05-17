package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionReqDTO implements Serializable {

    private static final long serialVersionUID = 7268771789379933082L;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 常用选项ID列表
     */
    @NotEmpty(message = "常用选项ID列表")
    private List<Long> idList;

}
