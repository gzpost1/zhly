package cn.cuiot.dmp.base.infrastructure.model;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.LongListHandler;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 房屋信息
 * </p>
 *
 * @author Mujun
 * @since 2024-06-13
 */
@Data
public class HousesArchivesVo extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 房屋名称
     */
    private String name;

    /**
     * 房号
     */
    private String roomNum;

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 所属楼盘名称
     */
    private String loupanIdName;

    /**
     * 房屋编码
     */
    private String code;

    /**
     * 房屋楼层
     */
    private String floorName;

    /**
     * 楼层别名
     */
    private String floorAlias;

    /**
     * 容量
     */
    private String capacity;

    /**
     * 期别
     */
    private String qibie;

    /**
     * 楼层系数
     */
    private Double floorCoefficient;

    /**
     * 房屋户型
     */
    private Long houseType;

    /**
     * 房屋户型
     */
    private String houseTypeName;

    /**
     * 朝向
     */
    private Long orientation;

    /**
     * 朝向
     */
    private String orientationName;

    /**
     * 建筑面积（支持4位小数，最长可输入15位）
     */
    private Double buildingArea;

    /**
     * 使用面积（支持4位小数，最长可输入15位）
     */
    private Double usableArea;

    /**
     * 收费面积（支持4位小数，最长可输入15位）
     */
    private Double chargeArea;

    /**
     * 公摊面积（支持4位小数，最长可输入15位）
     */
    private Double sharedArea;

    /**
     * 使用率（自动计算）
     */
    private Double utilizationRate;

    /**
     * 使用率（自动计算） + 百分号
     */
    private String utilizationRateName;

    /**
     * 物业业态
     */
    private Long propertyType;

    /**
     * 物业业态
     */
    private String propertyTypeName;

    /**
     * 状态
     */
    private Long status;

    /**
     * 状态
     */
    private String statusName;

    /**
     * 用途
     */
    private Long usageType;

    /**
     * 用途
     */
    private String usageTypeName;

    /**
     * 经营性质
     */
    private Long businessNature;

    /**
     * 经营性质
     */
    private String businessNatureName;

    /**
     * 资源类型
     */
    private Long resourceType;

    /**
     * 资源类型
     */
    private String resourceTypeName;

    /**
     * 产权单位（支持输入汉字、英文、符号、数字，长度支持50字符）
     */
    private String ownershipUnit;

    /**
     * 产权属性
     */
    private Long ownershipAttribute;

    /**
     * 产权属性
     */
    private String ownershipAttributeName;

    /**
     * 交房时间（年月日）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate handoverDate;

    /**
     * 责任人（支持输入汉字、英文、符号、数字，长度支持10字符）
     */
    private String responsiblePerson;

    /**
     * 停车区域
     */
    private Long parkingArea;

    /**
     * 停车区域
     */
    private String parkingAreaName;

    /**
     * 请填写备注（支持输入汉字、英文、符号、数字，长度支持200字符）
     */
    private String remarks;

    /**
     * 户型图（仅限jpg, jpeg, png格式，图片大小不超过50M，5张）
     */
    private List<String> floorPlan;

    /**
     * 附件（支持png, jpg, PDF, word, excel，小于50M）
     */
    private String attachments;

    /**
     * 房源标题（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    private String listingTitle;

    /**
     * 是否推荐（下拉选项是、否）
     */
    private Byte recommended;

    /**
     * 是否推荐（下拉选项是、否）
     */
    @TableField(exist = false)
    private String recommendedName;

    /**
     * 房源图片（仅限jpg、jpeg、png格式，图片大小不超过5M，最多5张图）
     */
    private List<String> listingImages;

    /**
     * 基础服务（显示自定义配置中的选项，支持多选）
     */
    private List<Long> basicServices;

    /**
     * 基础服务（显示自定义配置中的选项，支持多选）
     */
    private String basicServicesName;

    /**
     * 房源描述（支持输入汉字、英文、符号、数字，长度支持500字符）
     */
    private String listingDescription;

    /**
     * 档案二维码id
     */
    private Long qrCodeId;

}
