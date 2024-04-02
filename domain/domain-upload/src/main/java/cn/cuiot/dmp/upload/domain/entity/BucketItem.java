package cn.cuiot.dmp.upload.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 桶实体
 * @author: wuyongchong
 * @date: 2024/4/2 9:20
 */
@Data
public class BucketItem implements Serializable {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 创建时间
     */
    private Date creationDate;
}
