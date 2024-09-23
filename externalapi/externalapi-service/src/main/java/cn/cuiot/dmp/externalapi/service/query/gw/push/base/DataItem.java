package cn.cuiot.dmp.externalapi.service.query.gw.push.base;

import lombok.Data;

/**
 * @Author: zc
 * @Date: 2024-09-13
 */
@Data
public class DataItem<T> {
    private T value;
    private String key;
}
