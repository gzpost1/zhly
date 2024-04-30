package cn.cuiot.dmp.system.infrastructure.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author cwl
 * @classname GetOrganizationVO
 * @description 获取账户详情 出参
 * @date 2021/12/28
 */
@Data
public class GetOrganizationVO {

    /**
     * 主键id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     * 管理姓名
     */
    private String adminName;

    /**
     * 登录名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 组织id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /**
     * 组织名称
     */
    private String deptName;

    /**
     * 账户状态(0：禁用、 1：启用）
     */
    private Integer status;

    /**
     *  orgTypeId 账户类型
     **/
    private Integer orgTypeId;

    /**
     *  orgTypeId     @账户类型名称
     **/
    private String orgTypeName;

    /**
     * 菜单跟节点id
     */
    private List<String> menuList;

    /**
     *  label    用户标签（1:商务楼宇（写字楼等）,2:厂园区（工业、科技、物流等园区厂区）,3:商业综合体（购物中心、百货市场等）,
     *                   4:专业市场（建材、汽配、农贸等）,5:九小场所,6:联通管理方,7:其它商企（网吧、便利店、中小独栋企业等）
     **/
    private Integer  label;

    /**
     * 其他标签名称
     */
    private String  otherLabelName;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;


    /**
     * 企业有效期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expStartDate;

    /**
     * 企业有效期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expEndDate;


    /**
     * 备注
     */
    private String description;
}
