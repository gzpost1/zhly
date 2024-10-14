package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 门禁设备信息
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:20
 */
@Data
public class HaikangAcsDeviceExportVo implements Serializable {

    /**
     * 资源名称
     */
    @Excel(name = "设备名称")
    private String name;

    /**
     * 资源唯一编码
     */
    @Excel(name = "设备编码")
    private String indexCode;

    /**
     * 区域名称
     */
    @Excel(name = "区域名称")
    private String regionName;

    /**
     * 设备型号
     */
    @Excel(name = "设备型号")
    private String deviceModel;

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
    private Integer faceCapacity;

    /**
     * 门容量
     */
    @Excel(name = "门容量")
    private Integer doorCapacity;

    /**
     * 指纹容量
     */
    @Excel(name = "指纹容量")
    private Integer fingerCapacity;

    /**
     * 设备卡容量
     */
    @Excel(name = "设备卡容量")
    private Integer cardCapacity;

    /**
     * 指静脉容量
     */
    @Excel(name = "指静脉容量")
    private Integer veinCapacity;

    /**
     * 厂商
     */
    @Excel(name = "厂商")
    private String manufacturer;


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
     * 设备状态（0离线，1在线）
     */
    @Excel(name = "设备状态",replace = {"在线_1", "离线_0"})
    private String status;

    /**
     * 门禁点数量
     */
    @Excel(name = "门禁点")
    private Integer doorCou;

    /**
     * 读卡器数量
     */
    @Excel(name = "读卡器")
    private Integer readerCou;

}
