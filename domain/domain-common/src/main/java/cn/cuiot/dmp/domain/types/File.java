package cn.cuiot.dmp.domain.types;

import lombok.Data;

/**
 * @Author 犬豪
 * @Date 2024/1/11 14:49
 * @Version V1.0
 */
@Data
public class File {
    /**
     * 上传到oss返回到fileKey
     */
    private String fileKey;
    private String fileType;
    /**
     * 文件名
     */
    private String fileName;
}
