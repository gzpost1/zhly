package cn.cuiot.dmp.externalapi.service.vo.hik;

import lombok.Data;

/**
 * app用户信息分页VO
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonAppPageVO {

    /**
     * 人员编码
     */
    private Long id;

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    private String personName;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;

    /**
     * 人员所属组织名称
     */
    private String orgName;
}
