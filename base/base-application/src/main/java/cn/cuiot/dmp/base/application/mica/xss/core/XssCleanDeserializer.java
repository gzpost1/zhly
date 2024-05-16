package cn.cuiot.dmp.base.application.mica.xss.core;

import cn.cuiot.dmp.base.application.mica.xss.config.MicaXssProperties;
import cn.cuiot.dmp.base.application.mica.xss.utils.JsoupXssUtil;
import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * jackson xss 处理
 *
 * @author: wuyongchong
 * @date: 2024/5/16 10:59
 */
@Slf4j
public class XssCleanDeserializer extends XssCleanDeserializerBase {

    @Override
    public String clean(String name, String text) throws IOException {
        if (text == null) {
            return null;
        }
        // 读取 xss 配置
        MicaXssProperties properties = SpringContextHolder.getBean(MicaXssProperties.class);
        if (properties == null) {
            return text;
        }
        // 读取 XssCleaner bean
        XssCleaner xssCleaner = SpringContextHolder.getBean(XssCleaner.class);
        if (xssCleaner == null) {
            return JsoupXssUtil.trim(text, properties.isTrimText());
        }
        String value = xssCleaner
                .clean(name, JsoupXssUtil.trim(text, properties.isTrimText()), XssType.JACKSON);
        log.debug("Json property name:{} value:{} cleaned up by mica-xss, current value is:{}.",
                name, text, value);
        return value;
    }
}
