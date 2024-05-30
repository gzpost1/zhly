package cn.cuiot.dmp.archive.infrastructure.entity;

import java.math.BigDecimal;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 设备档案表
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_device_archives", autoResultMap = true)
public class DeviceArchivesEntity extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
     * 设备名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Length(max = 30, message = "设备名称不可超过30字")
    private String deviceName;

    /**
     * 设备类别（下拉选择自定义配置中数据）
     */
    private Long deviceCategory;

    /**
     * 设备类别（下拉选择自定义配置中数据）
     */
    @TableField(exist = false)
    private String deviceCategoryName;

    /**
     * 设备系统（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Length(max = 30, message = "设备系统不可超过30字")
    private String deviceSystem;

    /**
     * 设备专业（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Length(max = 30, message = "设备专业不可超过30字")
    private String deviceProfessional;

    /**
     * 安装位置（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Length(max = 30, message = "安装位置不可超过30字")
    private String installationLocation;

    /**
     * 详细位置（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Length(max = 30, message = "详细位置不可超过30字")
    private String locationDetails;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 安装日期（支持选择年月日）
     */
    private LocalDate installationDate;

    /**
     * 邮箱地址（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Length(max = 30, message = "邮箱地址不可超过30字")
    private String email;

    /**
     * 设备状态（具体选项）
     */
    private Long deviceStatus;

    /**
     * 设备状态（具体选项）
     */
    @TableField(exist = false)
    private String deviceStatusName;

    /**
     * 物业服务档次（下拉选项）
     */
    private Long propertyServiceLevel;

    /**
     * 物业服务档次（下拉选项）
     */
    @TableField(exist = false)
    private String propertyServiceLevelName;

    /**
     * 品牌（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "品牌不可超过30字")
    private String brand;

    /**
     * 设备型号（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "设备型号不可超过30字")
    private String deviceModel;

    /**
     * 规格（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "规格不可超过30字")
    private String specification;

    /**
     * 条形码（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "条形码不可超过30字")
    private String barcode;

    /**
     * 接口名称（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "接口名称不可超过30字")
    private String interfaceName;

    /**
     * 参数配置（支持输入汉字、数字、符号、英文，最长长度为200位字符）
     */
    @Length(max = 30, message = "参数配置不可超过200字")
    private String parameterConfiguration;

    /**
     * 制造商（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "制造商不可超过30字")
    private String manufacturer;

    /**
     * 联系方式（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "联系方式不可超过30字")
    private String manufacturerContact;

    /**
     * 制造日期（支持选择年月日）
     */
    private LocalDate manufacturingDate;

    /**
     * 供应商（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "供应商不可超过30字")
    private String supplier;

    /**
     * 联系方式（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "联系方式不可超过30字")
    private String supplierContact;

    /**
     * 出厂编号（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "出厂编号不可超过30字")
    private String factoryNumber;

    /**
     * 出厂日期（支持选择年月日）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate factoryDate;

    /**
     * 投运日期（支持选择年月日）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate operationDate;

    /**
     * 质保期限（支持选择年月日）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate warrantyPeriod;

    /**
     * 维保供应商（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "维保供应商不可超过30字")
    private String maintenanceSupplier;

    /**
     * 联系方式（支持输入汉字、数字、符号，最长长度为30位字符）
     */
    @Length(max = 30, message = "联系方式不可超过30字")
    private String maintenanceContact;

    /**
     * 备注（支持输入汉字、数字、符号，最长长度为100位字符）
     */
    @Length(max = 30, message = "备注不可超过100字")
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


}
