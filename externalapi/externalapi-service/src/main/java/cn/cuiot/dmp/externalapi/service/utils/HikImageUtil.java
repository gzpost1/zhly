package cn.cuiot.dmp.externalapi.service.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;

/**
 * 图片工具
 *
 * @Author: zc
 * @Date: 2024-10-12
 */
public class HikImageUtil {

    /**
     * 图片url转base64
     *
     * @return string
     * @Param imgUrl
     */
    public static String imageToBase64Converter(String imgUrl) {
        // 通过 URL 获取图片数据
        try {
            URL url = new URL(imgUrl);
            InputStream inputStream = url.openStream();
            // 将 InputStream 转换为字节数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // 将字节数组转换为 Base64 字符串
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.ERROR, "图片Url转Base64异常.......");
        }
    }
}
