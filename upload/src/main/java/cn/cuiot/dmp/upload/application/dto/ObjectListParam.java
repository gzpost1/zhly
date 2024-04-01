package cn.cuiot.dmp.upload.application.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2023/10/20 17:57
 */
@Data
public class ObjectListParam implements Serializable {
    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 对象名列表
     */
    @NotEmpty(message = "对象名列表不能为空")
    private List<String> objectNames;

    /**
     * 预签URL有效期（秒）
     */
    private Integer expires;
}
