package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengjian
 * @create 2024/9/5 11:39
 */
@Data
public class DeviceListDto {

    /**
     * 设备序列号
     */
    private String deviceNo;

    /**
     * 设备名称
     */
    private String Name;

    /**
     * 门禁选项
     */
    private Map  accessOptions;

    /**
     * 获取到门禁选项
     * @return
     */
    public Map getAccessOptions(){
        Map accessMap  = new HashMap();
        accessMap.put("facePermission","刷脸权限");
        accessMap.put("idCardPermission","刷卡权限");
        accessMap.put("faceAndCardPermission","人卡合一权限");
        accessMap.put("idCardFacePermission","人证比对权限");
        accessMap.put("passwordPermission","密码权限");
        return accessMap;
    }

}
