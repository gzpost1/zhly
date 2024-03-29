package cn.cuiot.dmp.domain.types;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @Description 图片
 * @Author 犬豪
 * @Date 2023/9/20 10:45
 * @Version V1.0
 */
@Data
@NoArgsConstructor
public class Picture {

    public Picture(@NonNull String url) {
        this.url = url;
    }

    public Picture(String url, String fileKey) {
        this.url = url;
        this.fileKey = fileKey;
    }

    /**
     * 图片文件ossKey
     */
    private String fileKey;

    /**
     * 图片访问路径
     */
    private String url;

    /**
     * img_file.ID
     */
    private String fileId;
}
