package cn.cuiot.dmp.externalapi.service.vo.gw;

import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author xulei
 * @create 2024-10-29 10:18
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GwCommonPropertyVo extends DmpDevicePropertyResp {

    /**
     * key值名称
     */
    private String keyName;

    public GwCommonPropertyVo(String key, String keyName) {
        super(key);
        this.keyName = keyName;
    }
}
