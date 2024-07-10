package cn.cuiot.dmp.upload.domain.entity;

import java.io.InputStream;
import java.io.Serializable;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2023/10/23 14:29
 */
@Data
public class ChunkUploadRequest implements Serializable {

    /**
     * 桶名称
     */
    private String bucketName;
    /**
     * 目录,img、excel、video、audio、app
     */
    private String dir;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件流对象
     */
    private InputStream inputStream;

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

    /**
     * 是否私有读
     */
    private Boolean privateRead;

    /**
     * 是否原名上传
     */
    private Boolean originName=false;
}
