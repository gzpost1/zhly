package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
public class LabelManageCreateDTO implements Serializable {

    private static final long serialVersionUID = -4692157869326788297L;



    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 标签管理类型
     * @see LabelManageTypeEnum
     */
    @NotNull(message = "标签管理类型不能为空")
    private Byte labelManageType;

    /**
     * 标签列表
     */
    @NotEmpty(message = "标签列表不能为空")
    private List<String> labelList;

}
