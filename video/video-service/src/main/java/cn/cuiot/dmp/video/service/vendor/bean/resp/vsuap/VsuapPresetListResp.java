
package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-预置位列表查询resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapPresetListResp {
    /**
     * 预置位id
     */
    private Integer presetId;
    /**
     * 预置位名称
     */
    private String presetName;

}
