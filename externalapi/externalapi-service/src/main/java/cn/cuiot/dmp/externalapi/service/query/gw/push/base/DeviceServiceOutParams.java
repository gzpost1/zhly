package cn.cuiot.dmp.externalapi.service.query.gw.push.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-14
 */
@Data
@NoArgsConstructor
public class DeviceServiceOutParams<T> {
    private String code;
    private List<DataItem<T>> data;
    private String messageId;
}
