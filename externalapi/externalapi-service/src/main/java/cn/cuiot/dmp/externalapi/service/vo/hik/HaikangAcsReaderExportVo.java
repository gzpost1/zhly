package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsReaderEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 门禁读卡器信息
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:20
 */
@Data
public class HaikangAcsReaderExportVo implements Serializable {

    /**
     * 资源名称
     */
    @Excel(name = "读卡器名称")
    private String name;

    /**
     * 资源唯一编码
     */
    @Excel(name = "读卡器编码")
    private String indexCode;

    /**
     * 设备型号
     */
    @Excel(name = "设备型号")
    private String deviceModel;

    /**
     * 区域名称
     */
    @Excel(name = "区域名称")
    private String regionName;

    /**
     * 所属区域
     */
    @Excel(name = "所属区域")
    private String regionIndexCode;

    /**
     * 所属区域目录名，以"/"分隔
     */
    @Excel(name = "所属区域目录名")
    private String regionPathName;


    /**
     * 人脸容量
     */
    @Excel(name = "人脸容量")
    private Integer acsReaderFaceCapacity;

    /**
     * 卡容量
     */
    @Excel(name = "卡容量")
    private Integer acsReaderCardCapacity;


    /**
     * 指纹容量
     */
    @Excel(name = "指纹容量")
    private Integer acsReaderFingerCapacity;

    /**
     * 所属设备名称
     */
    @Excel(name = "所属设备名称")
    private String parentIndexName;

    /**
     * 父级资源编号
     */
    @Excel(name = "所属设备编号")
    private String parentIndexCode;


    /**
     * 所属门禁点名称
     */
    @Excel(name = "关联门禁点名称")
    private String channelIndexName;


    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Integer sort;


    /**
     * 通信方式，0：韦根; 1：RS232; 2：RS485
     */
    @Excel(name = "通信方式",replace = {"韦根_0", "RS232_1", "RS485_2"})
    private String communicationMode;


    /**
     * 创建时间
     */
    @Excel(name = "创建时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 读卡器状态（0离线，1在线）
     */
    @Excel(name = "读卡器状态",replace = {"在线_1", "离线_0"})
    private String status;
}
