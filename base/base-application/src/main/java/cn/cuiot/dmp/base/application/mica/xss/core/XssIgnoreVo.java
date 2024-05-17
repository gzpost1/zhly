package cn.cuiot.dmp.base.application.mica.xss.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/**
 * 忽略存储
 * @author: wuyongchong
 * @date: 2024/5/16 10:56
 */
@Getter
@RequiredArgsConstructor
public class XssIgnoreVo {
    /**
     * 跳过的属性名
     */
    private final String[] names;

    public XssIgnoreVo() {
        this(new String[0]);
    }
}
