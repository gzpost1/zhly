package cn.cuiot.dmp.common.utils;

import com.google.common.collect.Maps;
import com.idrsolutions.image.png.PngCompressor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片压缩工具
 * @author: wuyongchong
 * @date: 2023/10/20 15:52
 */
@Slf4j
public class ImgScaleUtil {

    public static Map<String, String> scaleImg(File localFile, String localDirPath,
                                               String sinalName, String suffix) throws IOException {

        Integer width = getWidth(localFile);

        Map<String, Integer> sizeMap = Maps.newHashMap();
        sizeMap.put(sinalName + "-mini." + suffix, 150);
        sizeMap.put(sinalName + "-small." + suffix, 375);
        sizeMap.put(sinalName + "-default." + suffix, 750);

        Map<String, String> resultMap = Maps.newHashMap();
        //拿不到宽度直接返回，不压缩
        if (Objects.isNull(width)) {
            log.warn("scaleImg 拿不到width :{}", localFile.getPath());
            return resultMap;
        }
        if (width <= 3000) {
            for (Map.Entry<String, Integer> entry : sizeMap.entrySet()) {
                FileInputStream inStream = null;
                OutputStream outputStream = null;
                try {
                    inStream = new FileInputStream(localFile);
                    outputStream = new FileOutputStream(localDirPath + Matcher
                            .quoteReplacement(File.separator) + entry
                            .getKey());
                    outputStream.flush();
                    if (width > entry.getValue()) {
                        Thumbnails.of(inStream).width(entry.getValue()).keepAspectRatio(true)
                                .outputQuality(suffix.contains("png") ? 1 : 0.8)
                                .imageType(BufferedImage.TYPE_INT_ARGB_PRE)
                                .outputFormat(suffix)
                                .toOutputStream(outputStream);
                    } else {
                        Files.copy(localFile.toPath(), outputStream);
                    }
                } catch (Exception ex) {
                    log.warn("scaleImg fail:{}", localFile.getPath());
                    return resultMap;
                } finally {
                    if (inStream != null) {
                        inStream.close();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (suffix.contains("png")) {
                    PngCompressor.compress(
                            new File(localDirPath + Matcher.quoteReplacement(File.separator) + entry
                                    .getKey()),
                            new File(localDirPath + Matcher.quoteReplacement(File.separator) + entry
                                    .getKey()));
                }
                resultMap.put(entry.getKey(),
                        localDirPath + Matcher.quoteReplacement(File.separator) + entry
                                .getKey());
            }
        }
        return resultMap;
    }

    public static Integer getWidth(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            if (image == null) {
                log.warn("----上传的文件ImageIO读取为空--" + file.getName());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("----上传的文件不能用ImageIO读取--" + e.getMessage());
        }
        Integer width = image == null ? 760 : image.getWidth();
        log.info("宽度：" + width);
        return width;
    }

}
