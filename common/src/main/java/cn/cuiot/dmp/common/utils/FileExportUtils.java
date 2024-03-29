package cn.cuiot.dmp.common.utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 导出文件工具类
 *
 * @author lixf
 */
public class FileExportUtils {
    /**
     * 文件导出标识
     */    public static final String EXPORT_FLAG = "export";

    /**
     * 文件格式类型
     */
    protected static final List<String> FILE_CONTENT_LIST = Arrays.asList("application/zip",
            "application/octet-stream",
            "application/vnd.ms-excel",
            "application/pdf",
            "application/msword");

    private FileExportUtils() {

    }

    /**
     * 是否导出文件请求
     *
     * @param request 请求
     * @return 是否导出文档
     */
    public static boolean isExportFile(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains(EXPORT_FLAG);
    }

    /**
     * 是否文件类型
     *
     * @param response 输出
     * @return 是否文件类型
     */
    public static boolean isFileContentType(HttpServletResponse response) {
        String contentType = response.getContentType();
        return FILE_CONTENT_LIST.stream().anyMatch(contentType::contains);
    }

    public static boolean isFileContentType(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        return FILE_CONTENT_LIST.stream().anyMatch(contentType::contains);
    }

    /**
     * 从请求头的content-disposition中获取filename
     *
     * @param response 请求
     * @return 文件名
     * @throws IOException 异常
     */
    public static String getFileName(HttpServletResponse response) throws IOException {
        String filename = null;
        Collection<String> parts = response.getHeaderNames();
        for (String part : parts) {
            if (!"content-disposition".equalsIgnoreCase(part)) {
                continue;
            }
            String headerValue = response.getHeader("content-disposition");
            String[] contentDisposition = headerValue != null ? headerValue.split(";") : new String[]{};
            for (String value : contentDisposition) {
                if (value.trim().startsWith("filename")) {
                    filename = value.substring(value.indexOf('=') + 1).trim().replace("\"", "");
                    filename = URLDecoder.decode(filename, "UTF-8");
                    return filename;
                }
            }
        }
        return filename;
    }

}
