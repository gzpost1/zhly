package cn.cuiot.dmp.base.application.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;

/**
 * @author guoying
 * @className IpUtil
 * @description 获取请求的ip
 * @date 2020-09-03 15:14:10
 */
public class IpUtil {

    private IpUtil() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP4 = "127.0.0.1";
    private static final String LOCALHOST_IP6 = "0:0:0:0:0:0:0:1";
    private static final String SEPARATOR = ",";
    private static final int IP_ADDRESS_LENGTH = 15;

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;

        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCALHOST_IP4.equals(ipAddress) || LOCALHOST_IP6.equals(ipAddress)) {
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > IP_ADDRESS_LENGTH) {
                if (ipAddress.indexOf(SEPARATOR) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }

}
