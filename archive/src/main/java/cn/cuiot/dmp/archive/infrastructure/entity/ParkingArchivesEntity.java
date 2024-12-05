package cn.cuiot.dmp.archive.infrastructure.entity;

import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 车位档案表
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_parking_archives", autoResultMap = true)
public class ParkingArchivesEntity extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 车位编号
     */
    private String code;

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 所属楼盘名称
     */
    @TableField(exist = false)
    private String loupanIdName;

    /**
     * 所属区域（下拉选择自定义配置中数据）
     */
    private Long area;

    /**
     * 所属区域（下拉选择自定义配置中数据）
     */
    @TableField(exist = false)
    private String areaName;

    /**
     * 使用情况（下拉选择自定义配置中数据）
     */
    private Long usageStatus;

    /**
     * 使用情况（下拉选择自定义配置中数据）
     */
    @TableField(exist = false)
    private String usageStatusName;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 车位类型（下拉选择自定义配置中数据）
     */
    private Long parkingType;

    /**
     * 车位类型（下拉选择自定义配置中数据）
     */
    @TableField(exist = false)
    private String parkingTypeName;

    /**
     * 备注（支持输入汉字、数字、符号，最长长度为100位字符）
     */
    private String remarks;

    /**
     * 图片（支持png/jpg/jpeg，小于50M，最多上传1张，点击【添加】按钮，调用系统【打开】，单选上传文件）
     */
    private String image;

    /**
     * 档案二维码id
     */
    @TableField(exist = false)
    private Long qrCodeId;
    /**
     * 给前端装图片
     */
    @TableField(exist = false)
    private List<String> imageList;

    @TableField(exist = false)
    public BuildingArchivesVO buildingArchivesVO;
}
