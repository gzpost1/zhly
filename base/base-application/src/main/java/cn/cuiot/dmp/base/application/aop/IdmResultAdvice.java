package cn.cuiot.dmp.base.application.aop;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.base.application.utils.FileExportUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author jiangze
 * @classname IdmResultConfig
 * @description 统一处理返回结果
 * @date 2020-06-13
 */
@RestControllerAdvice(basePackages = "cn.cuiot.dmp")
public class IdmResultAdvice implements ResponseBodyAdvice {

    /**
     * 确定是否支持
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    /**
     * 前置处理
     *
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String contentType = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse().getContentType();
        boolean isFileType = FileExportUtils.isFileContentType(contentType);
        if (isFileType) {
            return null;
        }

        // 防止二次封装
        if (o instanceof IdmResDTO) {
            return o;
        } else {
            return new IdmResDTO<>(ResultCode.SUCCESS, o);
        }
    }
}
