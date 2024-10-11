package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息分页query
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikPersonPageQuery extends PageQuery {

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    private String personName;

    /**
     * 人员编码
     */
    private Long id;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;

    /**
     * 工号，1-32个字符
     */
    private String jobNo;

    /**
     * 人员照片状态（0:未添加，1:已添加）
     */
    private Byte faceDataStatus;

    /**
     * 权限配置状态（0:未配置，1:已配置）
     */
    private Byte authorizeStatus;
}
