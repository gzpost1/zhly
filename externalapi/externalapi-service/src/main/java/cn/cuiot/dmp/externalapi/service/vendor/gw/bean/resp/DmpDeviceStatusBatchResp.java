package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceStatusBatchResp implements Serializable {

    private static final long serialVersionUID = -6153287581148513892L;

    /**
     * 列表
     */
    private List<DmpDeviceStatusResp> deviceStatusList;

}
