package cn.cuiot.dmp.upload.domain.entity;

import java.io.Serializable;

/**
 * @author: wuyongchong
 * @date: 2023/10/27 13:30
 */
public class ChunkPartETag implements Serializable {
    private Integer partNumber;
    private String eTag;

    public ChunkPartETag() {
    }

    public ChunkPartETag(Integer partNumber, String eTag) {
        this.partNumber = partNumber;
        this.eTag = eTag;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
