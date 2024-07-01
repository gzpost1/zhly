package cn.cuiot.dmp.digitaltwin.service.dto;

import lombok.Data;

/**
 * 格物消防-设备信息（联系人）dto
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
public class GwFirefightDeviceNotifierDto {
    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 优先级
     */
    private String priority;
}
