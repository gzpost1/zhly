package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

/**
 * 海康基础返回
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikBaseResp<T> {

    /**
     * 返回码，0 – 成功，其他- 失败，参考[附录E.other.1 资源目录错误码]@[软件产品-综合安防管理平台-附录-附录E 返回码说明-返回码说明#附录E.other.1 资源目录错误码]
     */
    private String code;

    /**
     * 返回描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Boolean isSuccess() {
        return "0".equals(code);
    }
}
