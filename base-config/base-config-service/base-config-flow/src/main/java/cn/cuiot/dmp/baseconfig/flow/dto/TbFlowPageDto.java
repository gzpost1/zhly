package cn.cuiot.dmp.baseconfig.flow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 分页对象
 * @Date 2024/4/23 17:11
 * @Created by libo
 */
@Data
public class TbFlowPageDto implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 业务分类
     */
    private Long businessTypeId;

    /**
     * 业务分类名称
     */
    private String businessTypeName;

    /**
     * 所属组织
     */
    private List<Long> orgIds;

    /**
     * 所属组织名称
     */
    private String orgName;

    /**
     * 流程说明
     */
    private String remark;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;
}
