package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.constant.RegexConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author jz
 * @classname CompanyReqDto
 * @description 企业信息请求dto
 * @date 2023/06/29
 */
@Data
public abstract class CompanyReqDto {

    /**
     * 租户id
     */
    @JsonIgnore
    private String orgId;

    /**
     * 用户id
     */
    @JsonIgnore
    private String userId;

    /**
     * 公司详情请求dto
     */
    @Data
    public static class CompanyDetailReqDto extends CompanyReqDto {

    }

    /**
     * 编辑公司请求dto
     */
    @Data
    public static class UpdateCompanyReqDto extends CompanyReqDto {

        /**
         * 统一社会信用代码
         */
        @NotBlank(message = "社会信用代码不得为空")
        @Length(max = 18, min = 18, message = "社会信用代码长度为18位")
        @Pattern(regexp = RegexConst.SOCIAL_CREDIT_CODE, message = "社会信用代码仅支持大写字母与数字")
        private String socialCreditCode;

        /**
         * 公司名称
         */
        @NotBlank(message = "公司名称不得为空")
        @Length(max = 32, min = 1, message = "公司名称最多支持32个字符")
        private String companyName;

    }

    /**
     * 补录提醒请求dto
     */
    @Data
    public static class FillInRemindReqDto extends CompanyReqDto {


    }

}
