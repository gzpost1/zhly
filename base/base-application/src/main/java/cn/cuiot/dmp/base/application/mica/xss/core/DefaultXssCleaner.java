package cn.cuiot.dmp.base.application.mica.xss.core;

import cn.cuiot.dmp.base.application.mica.xss.config.MicaXssProperties;
import cn.cuiot.dmp.base.application.mica.xss.config.MicaXssProperties.Mode;
import cn.cuiot.dmp.base.application.mica.xss.utils.JsoupXssUtil;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 默认的 xss 清理器
 *
 * @author: wuyongchong
 * @date: 2024/5/16 10:35
 */
public class DefaultXssCleaner implements XssCleaner {

    private final MicaXssProperties properties;

    public DefaultXssCleaner(MicaXssProperties properties) {
        this.properties = properties;
    }

    private static Document.OutputSettings getOutputSettings(MicaXssProperties properties) {
        return new Document.OutputSettings()
                // 2. 转义，没找到关闭的方法，目前这个规则最少
                .escapeMode(Entities.EscapeMode.xhtml)
                // 3. 保留换行
                .prettyPrint(properties.isPrettyPrint());
    }

    @Override
    public String clean(String name, String bodyHtml, XssType type) {
        // 1. 为空直接返回
        if (!StringUtils.hasText(bodyHtml)) {
            return bodyHtml;
        }

        Mode mode = properties.getMode();
        if (Mode.ESCAPE == mode) {
            // html 转义
            return HtmlUtils.htmlEscape(bodyHtml, StandardCharsets.UTF_8.name());
        } else if (Mode.VALIDATE == mode) {
            // 校验
            String validHtml = bodyHtml.replaceAll("\\\\\\\\\\\\\\\"", "\"");
            validHtml = bodyHtml.replaceAll("\\\\\\\"", "\"");
            if (Jsoup.isValid(validHtml, JsoupXssUtil.WHITE_LIST)) {
                return bodyHtml;
            }
            throw type
                    .getXssException(name, bodyHtml, "Xss validate fail, input value:" + bodyHtml);
        } else {
            // 4. 清理后的 html
            String escapedHtml = Jsoup
                    .clean(bodyHtml, "", JsoupXssUtil.WHITE_LIST, getOutputSettings(properties));
            if (properties.isEnableEscape()) {
                return escapedHtml;
            }
            // 5. 反转义
            return Entities.unescape(escapedHtml);
        }
    }

}