package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.File;
import java.io.Serializable;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2023/10/20 13:57
 */
@Data
public class FileUploadParam implements Serializable {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 文件对象
     */
    private File file;

    /**
     * 目录,img、excel、video、audio、app
     */
    private String dir;

    /**
     * 预签URL有效期（秒）
     */
    private Integer expires;

    /**
     * 是否原名上传
     */
    private Boolean originName = false;

    /**
     * 是否压缩图片 1 是 0否
     */
    private Byte compressImg;

    /**
     * 是否私有读
     */
    private Boolean privateRead;
}
