package cn.cuiot.dmp.base.application.mica.xss.interceptor;

import cn.cuiot.dmp.base.application.mica.xss.config.MicaXssProperties;
import cn.cuiot.dmp.base.application.mica.xss.core.XssCleanIgnore;
import cn.cuiot.dmp.base.application.mica.xss.core.XssHolder;
import cn.cuiot.dmp.base.application.mica.xss.core.XssIgnoreVo;
import cn.cuiot.dmp.base.application.mica.xss.utils.ClassUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * xss 处理拦截器
 *
 * @author: wuyongchong
 * @date: 2024/5/16 11:02
 */
@RequiredArgsConstructor
public class XssCleanInterceptor implements AsyncHandlerInterceptor {

    private final PathMatcher matcher = new AntPathMatcher();
    private final MicaXssProperties xssProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        // 1. 非控制器请求直接跳出
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 2. 没有开启
        if (!xssProperties.isEnabled()) {
            return true;
        }
        // 判断是否需要跳过
        List<String> pathExcludePatterns = xssProperties.getPathExcludePatterns();
        String requestURL = request.getRequestURI();
        boolean needExclude = pathExcludePatterns.stream()
                .anyMatch(pattern -> matcher.match(pattern, requestURL));
        if (needExclude) {
            XssHolder.setIgnore(new XssIgnoreVo());
            return true;
        }
        // 3. 处理 XssIgnore 注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        XssCleanIgnore xssCleanIgnore = ClassUtil
                .getAnnotation(handlerMethod, XssCleanIgnore.class);
        if (xssCleanIgnore != null) {
            XssHolder.setIgnore(new XssIgnoreVo(xssCleanIgnore.value()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        XssHolder.remove();
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        XssHolder.remove();
    }
}