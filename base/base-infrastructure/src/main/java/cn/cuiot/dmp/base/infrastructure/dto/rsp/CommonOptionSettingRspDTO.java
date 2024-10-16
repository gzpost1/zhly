package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zc
 * @Date: 2024-06-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonOptionSettingRspDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 常用选项设置名称
     */
    private String name;

    /**
     * 排序
     */
    private Byte sort;
}
