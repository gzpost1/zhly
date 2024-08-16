package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import cn.cuiot.dmp.video.service.vendor.enums.VsuapResCode;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * 云智眼-公共返回resp
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@Data
public class VsuapBaseResp<T> implements Serializable {
    private static final long serialVersionUID = -37394910718825278L;
    /**
     * 接口响应码
     */
    private Integer code;
    /**
     * 接口响应错误码
     */
    private Integer errCode;
    /**
     * 接口响应信息
     */
    @JsonAlias({"message", "msg"}) //AK、SK错误的情况返回的json和正常请求返回的json字段不一样
    private String message;
    /**
     * 结果总数
     */
    private Integer total;
    private T data;

    public Boolean isSuccess() {
        return VsuapResCode.SUCCESS.getCode().equals(code);
    }
}
