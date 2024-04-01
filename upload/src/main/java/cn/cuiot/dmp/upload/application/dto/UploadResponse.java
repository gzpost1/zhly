package cn.cuiot.dmp.upload.application.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wuyongchong
 * @date: 2023/10/13 19:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse implements Serializable {

    /**
     * 状态值 300-未完成 200-已完成
     */
    private String status;

    /**
     * 桶名
     */
    private String bucketName;

    /**
     * 对象名
     */
    private String objectName;

    /**
     * 地址
     */
    private String url;

    /**
     * 预签地址
     */
    private String presignedObjectUrl;
    /**
     * 任务id
     */
    private String taskId;

}
