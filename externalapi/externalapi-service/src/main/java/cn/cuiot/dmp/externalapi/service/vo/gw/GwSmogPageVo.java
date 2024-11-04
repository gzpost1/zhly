package cn.cuiot.dmp.externalapi.service.vo.gw;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 格物烟雾报警器分页 VO
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@Data
public class GwSmogPageVo {

    /**
     * id
     */
    private Long id;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 所属组织
     */
    @Excel(name = "所属组织", orderNum = "1", width = 20)
    private String deptName;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", orderNum = "0", width = 20)
    private String name;


    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    @Excel(name = "所属楼盘", orderNum = "2", width = 20)
    private String buildingName;

    /**
     * 设备IMEI
     */
    @Excel(name = "设备IMEI", orderNum = "3", width = 20)
    private String imei;


    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启停用状态 1-启用 0-停用
     */
    private Byte status;

    @Excel(name = "启用状态", orderNum = "4", width = 20)
    private String statusName;

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）接口返回
     */
    private String equipStatus;


    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）接口返回
     */
    @Excel(name = "门禁状态", orderNum = "5", width = 20)
    private String equipStatusName;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间",orderNum = "6",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
