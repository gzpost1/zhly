package cn.cuiot.dmp.archive.infrastructure.entity;

import java.math.BigDecimal;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 空间档案表
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_room_archives", autoResultMap = true)
public class RoomArchivesEntity extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 空间名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    private String name;

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 空间分类（下拉选择自定义配置中数据）
     */
    private Long spaceCategory;

    /**
     * 详细位置（详细地址支持输入汉字、英文、符号、数字，长度支持50字符）
     */
    private String location;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 专业用途（下拉选择自定义配置中数据）
     */
    private Long professionalPurpose;

    /**
     * 空间面积（支持输入数字，最多支持输入7位数字）
     */
    private Long spaceArea;

    /**
     * 经营性质（下拉选择自定义配置中数据）
     */
    private Long businessNature;

    /**
     * 产权单位（支持输入汉字、数字、符号，最长长度为50位数字）
     */
    private String ownershipUnit;

    /**
     * 产权属性（下拉选择自定义配置中数据）
     */
    private Long ownershipAttribute;

    /**
     * 资源类型（下拉选项）
     */
    private Long resourceType;

    /**
     * 物业服务档次（下拉选项）
     */
    private Long propertyServiceLevel;

    /**
     * 空间说明（支持输入汉字、数字、符号，最长长度为500位字符）
     */
    private String spaceDescription;

    /**
     * 定位方式（下拉选项）
     */
    private Long locationMethod;

    /**
     * 定位偏差（支持输入汉字、数字、符号，最长长度为3位数字）
     */
    private String locationDeviation;

    /**
     * 状态（默认启用，点击关闭）
     */
    private Integer status;

    /**
     * 图片（支持png/jpg/jpeg，小于50M，最多上传3张）
     */
    private String images;


}
