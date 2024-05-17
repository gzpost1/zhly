package cn.cuiot.dmp.base.application.mica.xss.core;

import cn.cuiot.dmp.base.application.mica.xss.exception.FromXssException;
import cn.cuiot.dmp.base.application.mica.xss.exception.JacksonXssException;

/**
 * xss 数据处理类型
 * @author: wuyongchong
 * @date: 2024/5/16 10:26
 */
public enum  XssType {

    /**
     * 表单
     */
    FORM() {
        @Override
        public RuntimeException getXssException(String name, String input, String message) {
            return new FromXssException(input, message);
        }
    },

    /**
     * body json
     */
    JACKSON() {
        @Override
        public RuntimeException getXssException(String name, String input, String message) {
            return new JacksonXssException(name, input, message);
        }
    };

    /**
     * 获取 xss 异常
     *
     * @param name    属性名
     * @param input   input
     * @param message message
     * @return XssException
     */
    public abstract RuntimeException getXssException(String name, String input, String message);

}
