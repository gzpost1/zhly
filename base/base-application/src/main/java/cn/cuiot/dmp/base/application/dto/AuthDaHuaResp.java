package cn.cuiot.dmp.base.application.dto;

import lombok.Data;


/**
 * @author pengjian
 * @create 2024/7/18 17:53
 */
@Data
public class AuthDaHuaResp {

    private String result;

    private String code;

    private String msg;

    private Object data;
}
