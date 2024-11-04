package cn.cuiot.dmp.externalapi.service.entity.gw;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyResp;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 格物烟雾报警器设备属性
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tb_gw_smog_data",autoResultMap = true)
public class GwSmogDataEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *  id
     */
    @TableId("id")
    private Long id;


    /**
     * 烟雾报警器设备ID
     */
    private Long deviceId;


    /**
     * 设备属性
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<DmpDevicePropertyResp> deviceData;



}
