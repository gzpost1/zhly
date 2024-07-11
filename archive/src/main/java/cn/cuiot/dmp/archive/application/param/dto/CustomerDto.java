package cn.cuiot.dmp.archive.application.param.dto;

import cn.cuiot.dmp.common.bean.dto.NameUrlDto;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 客户保存参数
 *
 * @author: wuyongchong
 * @date: 2024/6/12 11:17
 */
@Data
public class CustomerDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户名称
     */
    @NotBlank(message = "请输入客户名称")
    @Length(max = 30, message = "客户名称限30字")
    private String customerName;


    /**
     * 客户类型
     */
    @NotBlank(message = "请选择客户类型")
    private String customerType;


    /**
     * 客户分类
     */
    private String customerCate;


    /**
     * 客户级别
     */
    private String customerLevel;


    /**
     * 联系人
     */
    @NotBlank(message = "请输入联系人姓名")
    @Length(max = 30, message = "联系人限30字")
    private String contactName;


    /**
     * 联系人手机号
     */
    @NotBlank(message = "请输入联系人手机号")
    @Length(max = 30, message = "联系人手机号限11位")
    private String contactPhone;


    /**
     * 公司性质
     */
    private String companyNature;


    /**
     * 所属行业
     */
    private String companyIndustry;


    /**
     * 信用等级
     */
    private String creditLevel;


    /**
     * 客户星级
     */
    private Double customerStar;

    /**
     * 邮箱
     */
    @Length(max = 100, message = "邮箱限100字")
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件号码
     */
    @Length(max = 50, message = "证件号码限50字")
    private String certificateCdoe;

    /**
     * 地址
     */
    @Length(max = 100, message = "地址限100字")
    private String address;

    /**
     * 附件
     */
    private List<String> attachments;

    /**
     * 备注
     */
    @Length(max = 200, message = "备注限200字")
    private String remark;

    /**
     * 房屋列表
     */
    @Valid
    @NotEmpty(message = "房屋信息不能为空")
    private List<CustomerHouseDto> houseList;

    /**
     * 成员列表
     */
    @Valid
    private List<CustomerMemberDto> memberList;

    /**
     * 车辆列表
     */
    @Valid
    private List<CustomerVehicleDto> vehicleList;

    /**
     * 企业ID-前端不用传
     */
    private Long companyId;

}
