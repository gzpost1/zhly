package cn.cuiot.dmp.upload.application.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2023/10/20 17:57
 */
@Data
public class DownLoadParam implements Serializable {
    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 完整对象名
     */
    @NotBlank(message = "完整对象名不能为空")
    private String objectName;
}
