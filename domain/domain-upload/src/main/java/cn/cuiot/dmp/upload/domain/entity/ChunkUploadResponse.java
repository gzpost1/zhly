package cn.cuiot.dmp.upload.domain.entity;

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
public class ChunkUploadResponse implements Serializable {

    /**
     * 状态值 300-未完成 200-已完成
     */
    private String status;

    /**
     * 地址
     */
    private String url;

    /**
     * 桶名
     */
    private String bucketName;

    /**
     * 对象名
     */
    private String objectName;

}
