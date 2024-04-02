package cn.cuiot.dmp.upload.application.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: wuyongchong
 * @date: 2023/10/20 13:57
 */
@Data
public class UploadParam implements Serializable {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     *文件对象
     */
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

    /**
     *目录,img、excel、video、audio、app
     */
    @NotBlank(message = "目录不能为空")
    private String dir;

    /**
     * 预签URL有效期（秒）
     */
    private Integer expires;

    /**
     * 是否压缩图片 1 是 0否
     */
    private Byte compressImg;

    /**
     * 是否私有读
     */
    private Boolean privateRead;
}
