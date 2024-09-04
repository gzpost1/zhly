package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/9/3 16:43
 */
@Data
public class ParkResultVO <T>{

    private String resMsg;

    private String resCode;

    private T data;
}
