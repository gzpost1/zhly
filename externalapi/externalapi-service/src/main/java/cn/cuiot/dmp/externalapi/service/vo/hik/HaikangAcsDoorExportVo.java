package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import java.io.Serializable;
import lombok.Data;

/**
 * 门禁点信息
 * @author: wuyongchong
 * @date: 2024/10/10 14:20
 */
@Data
public class HaikangAcsDoorExportVo implements Serializable {

    /**
     * 资源名称
     */
    @Excel(name = "门禁点名称")
    private String name;

    /**
     * 资源唯一编码
     */
    @Excel(name = "门禁点编码")
    private String indexCode;

    /**
     * 区域名称
     */
    @Excel(name = "区域名称")
    private String regionName;

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
     * 通道类型名称
     */
    @Excel(name = "通道类型")
    private String channelTypeName;

    /**
     * 门序号
     */
    @Excel(name = "门序号")
    private String doorSerial;


    /**
     * 资源类型名称
     */
    @Excel(name = "源类型")
    private String resourceTypeName;


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
     * 读卡器1
     */
    @Excel(name = "读卡器1")
    private String readerInId;


    /**
     * 读卡器2
     */
    @Excel(name = "读卡器2")
    private String readerOutId;

}
