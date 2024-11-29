package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujianyu
 * @description 空间档案查询条件
 * @since 2024-05-15 10:39
 */
@Data
public class RoomArchivesQuery extends PageQuery {

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 空间名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    private String name;

    /**
     * 产权单位（支持输入汉字、数字、符号，最长长度为50位数字）
     */
    private String ownershipUnit;

    /**
     * 资源类型（下拉选项）
     */
    private Long resourceType;

    /**
     * 状态（默认启用，点击关闭）
     */
    private Integer status;

    /**
     * 专业用途（下拉选择自定义配置中数据）
     */
    private Long professionalPurpose;

    /**
     * 定位方式（下拉选项）
     */
    private Long locationMethod;

    /**
     * 空间分类（下拉选择自定义配置中数据）
     */
    private Long spaceCategory;

    /**
     * 经营性质（下拉选择自定义配置中数据）
     */
    private Long businessNature;

    /**
     * 产权属性（下拉选择自定义配置中数据）
     */
    private Long ownershipAttribute;

    /**
     * 部门id
     */
    private Long departmentId;

}
