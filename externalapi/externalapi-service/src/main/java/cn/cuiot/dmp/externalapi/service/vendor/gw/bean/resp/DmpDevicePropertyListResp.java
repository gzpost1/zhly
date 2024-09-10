package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDevicePropertyListResp implements Serializable {

    private static final long serialVersionUID = -5548789906309921007L;

    /**
     * 设备属性列表
     */
    private List<DmpDevicePropertyResp> list;

}
