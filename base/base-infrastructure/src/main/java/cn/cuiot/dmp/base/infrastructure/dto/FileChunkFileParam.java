package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: wuyongchong
 * @date: 2023/10/13 19:15
 */
@Data
public class FileChunkFileParam implements Serializable {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 目录,img、excel、video、audio、app
     */
    @NotBlank(message = "目录不能为空")
    private String dir;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    /**
     * 文件对象
     */
    @NotNull(message = "文件对象不能为空")
    private MultipartFile file;

    /**
     * 预签URL有效期（秒）
     */
    private Integer expires;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 当前分片索引
     */
    private Integer chunk;

    /**
     * 分片总数
     */
    private Integer chunkTotal;


}
