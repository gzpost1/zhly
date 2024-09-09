package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 门禁（宇泛）设备响应信息
 *
 * @date 2024/8/21 14:00
 * @author gxp
 */
@Data
public class UniUbiDeviceRespInfo implements Serializable {
    private static final long serialVersionUID = 7207499119905554894L;

    /**
     *
     */
    private String projectGuid;

    /**
     * 设备序列号
     */
    private String deviceNo;

    /**
     *
     */
    private String deviceModel;

    /**
     * 设备备注,开发者自定义传入
     */
    private String tag;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备状态,1:未绑定 2:已绑定 3:已禁用
     */
    private String state;

    /**
     * 设备在线状态, 1:在线 2：不在线
     */
    private String onlineState;

    /**
     * 设备应用版本号
     */
    private String versionNo;

    /**
     * 设备最后活跃时间
     */
    private String lastActiveTime;

    /**
     * 设备创建时间
     */
    private String createTime;

    /**
     *
     */
    private String recType;

    /**
     *
     */
    private String sceneGuid;

    /**
     *
     */
    private String sceneName;

    /**
     * 设备来源
     */
    private String source;

    /**
     *
     */
    private String addition;

    /**
     *
     */
    private String password;

    /**
     * 设备所属应用
     */
    private String appId;

    /**
     *
     */
    private String ip;

    /**
     *
     */
    private String thirdDeviceId;

    /**
     *
     */
    private String productKey;

    /**
     *
     */
    private String deviceAbility;


    public String getLastActiveTime() {
        if(StringUtils.isNotBlank(lastActiveTime)){
            return lastActiveTime.replace("T", " ").substring(0, 19);
        }
        return lastActiveTime;
    }

    public String getCreateTime() {
        if(StringUtils.isNotBlank(createTime)){
            return createTime.replace("T", " ").substring(0, 19);
        }
        return createTime;
    }

}
