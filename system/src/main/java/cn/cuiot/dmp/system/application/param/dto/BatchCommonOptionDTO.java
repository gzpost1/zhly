package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class BatchCommonOptionDTO implements Serializable {

    private static final long serialVersionUID = 3001281059526790222L;

    /**
     * 常用选项分类ID
     */
    private Long typeId;

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
