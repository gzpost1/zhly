package cn.cuiot.dmp.externalapi.service.query;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author pengjian
 * @create 2024/9/5 15:49
 */
@Data
public class PortraitAccessDto {

    /**
     * 设备号
     */
    private String deviceNo;

    /**
     * 设备权限信息
     */
    private JSONObject accessOptions;
}
