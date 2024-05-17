package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author wensq
 * @version 1.0
 * @description: 新增组织 入参
 * @date 2022/9/14 18:06
 */
@Data
public class InsertDepartmentDto extends AbstractResourceParam {

    /**
     * 组织名称
     */
    @NotBlank(message = "请输入组织名称")
    @Length(max = 30,message = "组织名称不可超过30字")
    private String departmentName;

    /**
     * 租户id
     */
    private Long pkOrgId;

    /**
     * 创建人
     */
    private String createBy;





}
