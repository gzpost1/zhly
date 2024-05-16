package cn.cuiot.dmp.base.application.mica.xss.core;

import cn.cuiot.dmp.base.application.mica.xss.config.MicaXssProperties;
import cn.cuiot.dmp.base.application.mica.xss.utils.JsoupXssUtil;
import java.beans.PropertyEditorSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 表单 xss 处理
 *
 * @author: wuyongchong
 * @date: 2024/5/16 10:54
 */
@ControllerAdvice
@ConditionalOnProperty(
        prefix = MicaXssProperties.PREFIX,
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@RequiredArgsConstructor
public class FormXssClean {

    private final MicaXssProperties properties;
    private final XssCleaner xssCleaner;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 处理前端传来的表单字符串
        binder.registerCustomEditor(String.class,
                new StringPropertiesEditor(xssCleaner, properties));
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class StringPropertiesEditor extends PropertyEditorSupport {

        private final XssCleaner xssCleaner;
        private final MicaXssProperties properties;

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null) {
                setValue(null);
            } else if (XssHolder.isEnabled()) {
                String value = xssCleaner
                        .clean(JsoupXssUtil.trim(text, properties.isTrimText()), XssType.FORM);
                setValue(value);
                log.debug("Request parameter value:{} cleaned up by mica-xss, current value is:{}.",
                        text, value);
            } else {
                setValue(JsoupXssUtil.trim(text, properties.isTrimText()));
            }
        }
    }

}
