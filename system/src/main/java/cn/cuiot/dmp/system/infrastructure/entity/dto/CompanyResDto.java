package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author jz
 * @classname CompanyResDto
 * @description 公司信息响应dto
 * @date 2023/06/29
 */
@Data
public abstract class CompanyResDto {

    /**
     * 公司详情响应dto
     */
    @Data
    public static class CompanyDetailResDto extends CompanyResDto {

        /**
         * 公司名称
         */
        private String companyName;

        /**
         * 统一社会信用代码
         */
        private String socialCreditCode;

    }

    /**
     * 补录提醒响应dto
     */
    @Data
    public static class FillInRemindResDto extends CompanyResDto {

        /**
         * 是否提示（0：不提示，1：提示）
         */
        private String remind;

        /**
         * 提示内容
         */
        private String content;

    }

}
