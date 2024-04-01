package cn.cuiot.dmp.upload.domain.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wuyongchong
 * @date: 2023/10/27 13:27
 */
public class ChunkContext implements Serializable {
    private String uploadId;
    private List<ChunkPartETag> partETags;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<ChunkPartETag> getPartETags() {
        return partETags;
    }

    public void setPartETags(List<ChunkPartETag> partETags) {
        this.partETags = partETags;
    }
}
