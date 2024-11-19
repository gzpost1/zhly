package cn.cuiot.dmp.system.infrastructure.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 后台-初始化管理-企业模板 分页VO
 *
 * @Author: zc
 * @Date: 2024-11-05
 */
@Data
public class CompanyTemplateVO {

    /**
     * id
     */
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 企业编码
     */
    private String companyCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
