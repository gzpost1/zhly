package cn.cuiot.dmp.baseconfig.custommenu.dto;
import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * @Description 分页查询条件
 * @Date 2024/4/28 19:34
 * @Created by libo
 */
@Data
public class TbFlowTaskInfoQuery extends PageQuery {
    /**
     * 任务名称
     */
    private String name;

    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 业务分类
     */
    private Long businessTypeId;
}
