package cn.cuiot.dmp.base.infrastructure.syslog;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 对象内容
 * @author: wuyongchong
 * @date: 2024/6/17 14:21
 */
@Setter
@Getter
public class OptTargetData implements Serializable {

    /**
     * 对象名称
     */
    private String dataName;
    /**
     * 对象ID
     */
    private String dataId;

    public OptTargetData() {
    }

    public OptTargetData(String dataName, String dataId) {
        this.dataName = dataName;
        this.dataId = dataId;
    }
}
