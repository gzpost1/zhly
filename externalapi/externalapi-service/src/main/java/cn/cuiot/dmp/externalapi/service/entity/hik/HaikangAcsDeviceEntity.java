package cn.cuiot.dmp.externalapi.service.entity.hik;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 门禁设备信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_haikang_acs_device")
public class HaikangAcsDeviceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 企业ID
     */
    private Long orgId;


    /**
     * 资源唯一编码
     */
    private String indexCode;


    /**
     * 父级资源编号
     */
    private String parentIndexCode;


    /**
     * 资源类型
     */
    private String resourceType;


    /**
     * 资源名称
     */
    private String name;


    /**
     * 门禁设备类型编码，详见[附录A.7 门禁设备类型]
     */
    private String devTypeCode;


    /**
     * 门禁设备类型型号，详见[附录A.7 门禁设备类型]
     */
    private String devTypeDesc;


    /**
     * 主动设备编号
     */
    private String deviceCode;

    /**
     * 设备型号
     */
    private String deviceModel;


    /**
     * 厂商
     */
    private String manufacturer;


    /**
     * 所属区域
     */
    private String regionIndexCode;


    /**
     * 所属区域目录,以@符号分割，包含本节点
     */
    private String regionPath;


    /**
     * 接入协议，详见[附录A.6 编码设备接入协议]
     */
    private String treatyType;


    /**
     * 设备卡容量
     */
    private Integer cardCapacity;


    /**
     * 指纹容量
     */
    private Integer fingerCapacity;


    /**
     * 指静脉容量
     */
    private Integer veinCapacity;


    /**
     * 人脸容量
     */
    private Integer faceCapacity;


    /**
     * 门容量
     */
    private Integer doorCapacity;


    /**
     * 拨码
     */
    private String deployId;


    /**
     * 网域
     */
    private String netZoneId;


    /**
     * 描述
     */
    private String description;


    /**
     * 支持认证方式，数据为十进制
     */
    private String acsReaderVerifyModeAbility;


    /**
     * 区域名称
     */
    private String regionName;


    /**
     * 所属区域目录名，以"/"分隔
     */
    private String regionPathName;


    /**
     * 门禁设备IP
     */
    private String ip;


    /**
     * 门禁设备端口
     */
    private String port;


    /**
     * 设备能力集(含设备上的智能能力)，详见[附录A.44 设备能力集]
     */
    private String capability;


    /**
     * 设备序列号
     */
    private String devSerialNum;


    /**
     * 版本号
     */
    private String dataVersion;


    /**
     * 设备状态（0离线，1在线）
     */
    private Byte status;


    /**
     * 状态采集时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectTime;


    /**
     * 设备系列
     */
    private String deviceType;


    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否删除, 0:否, 1:是
     */
    private Byte deleted;

    /**
     * 数据落地时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dataTime;


}
