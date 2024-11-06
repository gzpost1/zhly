package cn.cuiot.dmp.externalapi.service.entity.park;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 识别记录上报记录
 * @author pengjian
 * @since 2024-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_identification_record")
public class IdentificationRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件Guid,唯一不重复标志
     */
    @TableId("event_guid")
    private String eventGuid;


    /**
     * 设备内网ip
     */
    private String deviceIp;


    /**
     * 设备id
     */
    private String deviceNo;


    /**
     * 人员guid 或者STRANGERBABY
     */
    private String admitGuid;


    /**
     * 识别模式,1:人像识别, 2:刷卡识别 ,3:人卡合一 4,人证比对 5:按钮开门 6：远程开门 7:密码识别 8：人+密码开门 9:口罩测温检测 10:二维码识别 11: 刷身份证 12:指纹识别
     */
    private Byte recMode;


    /**
     * 现场照url
     */
    private String filePath;


    /**
     * 识别记录时间戳
     */
    @TableField(exist = false)
    private String showTime;

    /**
     * 识别时间
     */
    private Date showDate;

    /**
     * 活体结果 1:活体判断成功 2:活体判断失败 3:未进行活体判断
     */
    private Byte aliveType;


    /**
     * 识别分数
     */
    private Integer recScore;


    /**
     * 软件版本号
     */
    private String deviceVersion;


    /**
     * 设备来源
     */
    private Byte source;


    /**
     * 人员比对结果,1:比对成功 2:比对失败
     */
    private Byte type;


    /**
     * 识别卡号
     */
    private String cardNo;


    /**
     * 设备名称
     */
    private String deviceName;


    /**
     * 比对模式,1:本地识别 2:云端识别
     */
    private Byte recType;


    /**
     * 有效日期判断 1:有效期内 2:有效期外 3:未进行有效期判断
     */
    private Byte permissionTimeType;


    /**
     * 有效时间段判断 1:时间段内 2:时间段外 3:未进行时间段判断
     */
    private Byte passTimeType;


    /**
     * 识别模式判断 1. 模式正确 2.模式不正确
     */
    private Byte recModeType;


    /**
     * 保留字段
     */
    private String storageId;


    /**
     * 当前时间戳
     */
    private Long timestamp;


    /**
     * 识别主体姓名
     */
    private String admitName;


    /**
     * 企业id
     */
    private Long companyId;


    /**
     * 楼盘id
     */
    private Long communityId;


    /**
     * 创建时间
     */
    private Date createTime;



}
