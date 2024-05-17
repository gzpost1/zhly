package cn.cuiot.dmp.base.application.mica.xss.core;

import cn.cuiot.dmp.base.application.mica.xss.config.MicaXssProperties;
import cn.cuiot.dmp.base.application.mica.xss.utils.JsoupXssUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * jackson xss 处理
 *
 * @author: wuyongchong
 * @date: 2024/5/16 11:00
 */
@Slf4j
@RequiredArgsConstructor
public class JacksonXssClean extends XssCleanDeserializerBase {

    private final MicaXssProperties properties;
    private final XssCleaner xssCleaner;

    @Override
    public String clean(String name, String text) throws IOException {
        if (text == null) {
            return null;
        }
        // 判断是否忽略
        if (XssHolder.isIgnore(name)) {
            return JsoupXssUtil.trim(text, properties.isTrimText());
        }
        String value = xssCleaner
                .clean(name, JsoupXssUtil.trim(text, properties.isTrimText()), XssType.JACKSON);
        log.debug("Json property name:{} value:{} cleaned up by mica-xss, current value is:{}.",
                name, text, value);
        return value;
    }

}