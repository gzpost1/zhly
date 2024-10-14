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
 * 门禁读卡器信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_haikang_acs_reader")
public class HaikangAcsReaderEntity implements Serializable {

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
     * 资源类型，reader：门禁读卡器
     */
    private String resourceType;


    /**
     * 资源唯一编码
     */
    private String indexCode;


    /**
     * 资源名称
     */
    private String name;


    /**
     * 所属区域
     */
    private String regionIndexCode;


    /**
     * 所属区域目录,以@符号分割，包含本节点
     */
    private String regionPath;


    /**
     * 设备IP
     */
    private String ip;


    /**
     * 设备端口
     */
    private String port;


    /**
     * 主动设备编号
     */
    private String deviceCode;


    /**
     * 设备驱动
     */
    private String deviceKey;


    /**
     * 设备型号
     */
    private String deviceModel;


    /**
     * 设备系列
     */
    private String deviceType;


    /**
     * 版本号
     */
    private String dataVersion;


    /**
     * 网域
     */
    private String netZoneId;


    /**
     * 拨码
     */
    private String deployId;


    /**
     * 通信方式，0：韦根; 1：RS232; 2：RS485
     */
    private String communicationMode;


    /**
     * 父级资源编号
     */
    private String parentIndexCode;

    /**
     * 门禁点编码
     */
    private String channelIndexCode;


    /**
     * 显示顺序
     */
    private Integer sort;


    /**
     * 设备能力，详见数据字典A.44
     */
    private String capability;


    /**
     * 标签
     */
    private String comId;


    /**
     * 门禁点序号
     */
    private String doorNo;


    /**
     * 卡容量
     */
    private Integer acsReaderCardCapacity;


    /**
     * 指纹容量
     */
    private Integer acsReaderFingerCapacity;


    /**
     * 人脸容量
     */
    private Integer acsReaderFaceCapacity;


    /**
     * 所属区域目录名，以"/"分隔
     */
    private String regionPathName;


    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 描述
     */
    private String description;


    /**
     * 设备状态（1-在线，0-离线）
     */
    private Byte status;


    /**
     * 状态采集时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectTime;

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
