package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wuyongchong
 * @date: 2023/10/20 17:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileObjectResponse implements Serializable {
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
}
