package cn.cuiot.dmp.upload.infrastructure.util;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件类型安全校验
 *
 * @author: wuyongchong
 * @date: 2024/4/17 10:42
 */
@Slf4j
public class FileTypeUtil {

    /**
     * 默认MIME_TYPES列表
     */
    private static final MimeTypes DEFAULT_MIME_TYPES = MimeTypes.getDefaultMimeTypes();

    /**
     * 文件类型安全校验
     */
    public static boolean validate(MultipartFile file, String suffix) throws IOException {
        String extension = "." + suffix;
        try {
            Tika tika = new Tika();
            String detectedMediaType = tika.detect(file.getBytes(), extension);
            MimeType mimeType = DEFAULT_MIME_TYPES.forName(detectedMediaType);
            String type = mimeType.getType().getType();
            if (StringUtils.equals("image", type)) {
                return true;
            }
            return CollectionUtils.isNotEmpty(mimeType.getExtensions())
                    && mimeType.getExtensions().stream().anyMatch(ext -> ext.equals(extension));
        } catch (Exception e) {
            log.error("mimeType validate error ", e);
        }
        return false;
    }
}
