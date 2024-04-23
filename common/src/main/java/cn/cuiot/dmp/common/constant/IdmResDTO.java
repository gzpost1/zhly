package cn.cuiot.dmp.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @classname IdmResDTO
 * @description 统一响应数据格式DTO
 * @author jiangze
 * @date 2020-06-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdmResDTO<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 执行成功的构造函数
     * @param resultCode 结果状态码
     * @param data 数据
     * @return
     */
    public IdmResDTO(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    /**
     * 执行失败的构造函数
     * @param resultCode 结果状态码
     * @return
     */
    public IdmResDTO(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * Api执行失败的构造函数
     * @param code
     * @param message
     */
    public IdmResDTO(String code, String message){
        this.code = code;
        this.message = message;
    }

    public static <T> IdmResDTO<T> success(T data) {
        return new IdmResDTO<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    public static <T> IdmResDTO<T> success() {
        return success(null);
    }

    public static <T> IdmResDTO<T> body(T data){
        return success(data);
    }
}
