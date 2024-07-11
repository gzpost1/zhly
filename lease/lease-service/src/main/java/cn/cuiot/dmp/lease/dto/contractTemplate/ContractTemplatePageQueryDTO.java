package cn.cuiot.dmp.lease.dto.contractTemplate;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTemplatePageQueryDTO extends PageQuery {

    private static final long serialVersionUID = -463264846583608022L;

    /**
     * 合同模板编码
     */
    private String id;

    /**
     * 合同模板名称
     */
    private String name;

    /**
     * 合同性质（系统配置自定义）
     */
    private Long natureId;

    /**
     * 合同类型（系统配置自定义）
     */
    private Long typeId;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
