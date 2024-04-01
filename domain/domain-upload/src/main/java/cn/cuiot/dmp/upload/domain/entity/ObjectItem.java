package cn.cuiot.dmp.upload.domain.entity;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2023/10/13 18:12
 */
@Data
public class ObjectItem implements Serializable {

    private String bucketName;

    private String objectName;

    private long size;

    private Map<String, String> userMetadata;
}
