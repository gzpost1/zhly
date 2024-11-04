package cn.cuiot.dmp.externalapi.service.entity.gw;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceCreateResp;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 格物烟雾报警器
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_gw_smog")
public class GwSmogEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *  id
     */
    @TableId("id")
    private Long id;


    /**
     * 企业ID
     */
    private Long companyId;


    /**
     * 部门ID
     */
    private Long deptId;


    /**
     * 设备名称
     */
    private String name;


    /**
     * 楼盘id
     */
    private Long buildingId;


    /**
     * 设备IMEI号，全局唯一
     */
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
     * 状态 1-启用 0-停用
     */
    private Byte status;


    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）
     */
    private String equipStatus;


    /**
     * 设备productKey（接口返回）
     */
    private String productKey;


    /**
     * 设备deviceKey（接口返回）
     */
    private String deviceKey;


    /**
     * 设备平台唯一标识码（接口返回）
     */
    private String iotId;


    /**
     * 设备密钥，设备鉴权登录时使用（接口返回）
     */
    private String deviceSecret;

    /**
     * 构建外部设备信息
     */
    public void buildExternalDeviceInfo(GwSmogEntity entity, DmpDeviceCreateResp device) {
        //第三方设备信息
        entity.setProductKey(device.getProductKey());
        entity.setDeviceKey(device.getDeviceKey());
        entity.setIotId(device.getIotId());
        entity.setDeviceSecret(device.getDeviceSecret());
    }

}
