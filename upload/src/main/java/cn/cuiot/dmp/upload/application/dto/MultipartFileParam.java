package cn.cuiot.dmp.upload.application.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: wuyongchong
 * @date: 2023/10/20 17:41
 */
@Data
public class MultipartFileParam implements Serializable {
    /**
     * 桶名称
     */
    private String bucketName;

    /**
     *批量文件
     */
    @NotEmpty(message = "文件不能为空")
    private MultipartFile[] files;

    /**
     *目录,img、excel、video、audio、app
     */
    @NotBlank(message = "目录不能为空")
    private String dir;

    /**
     * 预签URL有效期（秒）
     */
    private Integer expires;
}
