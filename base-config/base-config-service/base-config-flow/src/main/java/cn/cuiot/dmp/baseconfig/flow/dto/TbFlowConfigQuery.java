package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * @Description 流程配置
 * @Date 2024/4/23 16:46
 * @Created by libo
 */
@Data
public class TbFlowConfigQuery extends PageQuery {
    /**
     * 流程名称
     */
    private String name;

    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 所属组织路径
     */
    private String orgPath;

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


    /**
     * 是否允许客户端发起工单 0否 1是
     */
    private Byte isSelectAppUser;
}
