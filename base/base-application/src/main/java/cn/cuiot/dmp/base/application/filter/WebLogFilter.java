package cn.cuiot.dmp.base.application.filter;

import cn.cuiot.dmp.base.application.dto.ResponseWrapper;
import cn.cuiot.dmp.base.application.utils.FileExportUtils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 单点登录过滤器
 *
 * @author lixf
 */
@Slf4j
public class WebLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 只过滤带export标识的
        if (FileExportUtils.isExportFile((HttpServletRequest) request)) {
            ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) response);
            request.setAttribute(FileExportUtils.EXPORT_FLAG, true);
            chain.doFilter(request, wrapper);

            byte[] bodyBytes = wrapper.getBodyBytes();

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bodyBytes);
            outputStream.flush();
            outputStream.close();
        } else {
            chain.doFilter(request, response);
        }
    }

}
