package cn.cuiot.dmp.externalapi.service.vo.gw;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 格物门禁分页 VO
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@Data
public class GwEntranceGuardPageVo {

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
    private String deptPathName;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 门禁名称
     */
    private String name;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    private String buildingName;

    /**
     * 门禁品牌id
     */
    private Long brandId;

    /**
     * 门禁品牌名称
     */
    private String brandName;

    /**
     * 门禁型号id
     */
    private Long modelId;

    /**
     * 门禁品牌名称
     */
    private String modelName;

    /**
     * 门禁SN
     */
    private String sn;

    /**
     * 通行方向
     */
    private Integer direction;

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

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）接口返回
     */
    private String equipStatus;

    /**
     * 创建时间
     */
    private Date createTime;
}
