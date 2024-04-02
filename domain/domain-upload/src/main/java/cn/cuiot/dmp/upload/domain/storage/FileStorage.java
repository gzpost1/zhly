package cn.cuiot.dmp.upload.domain.storage;

import cn.cuiot.dmp.upload.domain.entity.BucketItem;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadRequest;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadResponse;
import cn.cuiot.dmp.upload.domain.entity.ObjectItem;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.util.List;

/**
 * 文件操作基类
 * @author: wuyongchong
 * @date: 2024/4/1 19:38
 */
public abstract class FileStorage {

    /**
     * 状态值 300-未完成
     */
    protected static String UN_FINISH="300";

    /**
     * 状态值 200-已完成
     */
    protected static String FINISH="200";

    /**
     * 获取桶列表
     */
    public List<BucketItem> listBuckets() throws Exception {
        return Lists.newArrayList();
    }

    /**
     * 创建桶
     */
    public void createBucket(String bucketName) throws Exception {
    }

    /**
     * 判断Bucket是否存在, true: 存在, false: 不存在
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return false;
    }

    /**
     * 删除桶
     */
    public void removeBucket(String bucketName) throws Exception {

    }

    /**
     * 获得Bucket的策略
     */
    public String getBucketPolicy(String bucketName) throws Exception {
        return null;
    }

    /**
     * 简单上传
     */
    public String putObject(String bucketName, String objectName, InputStream stream,
            String contextType) throws Exception {
        return null;
    }

    /**
     * 获取文件流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return null;
    }

    /**
     * 获取文件URL
     */
    public String getObjectUrl(String bucketName, String objectName, String urlType,
            Integer expires) throws Exception {
        return null;
    }

    /**
     * 判断object是否存在, true: 存在, false: 不存在
     */
    public boolean objectExists(String bucketName, String objectName) throws Exception {
        return false;
    }

    /**
     * 删除object
     */
    public void removeObject(String bucketName, String objectName) throws Exception {

    }

    /**
     * 批量删除object
     */
    public void removeObjects(String bucketName, List<String> objectNames) throws Exception {

    }

    /**
     * 获取文件外链
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires)
            throws Exception {
        return null;
    }

    /**
     * 拷贝文件
     */
    public void copyObject(String bucketName, String objectName, String targetBucketName,
            String targetObjectName) throws Exception {

    }

    /**
     * 根据文件前缀查询文件
     */
    public List<ObjectItem> getObjectsByPrefix(String bucketName, String prefix, boolean recursive)
            throws Exception {
        return Lists.newArrayList();
    }

    /**
     * 文件分片上传
     */
    public ChunkUploadResponse chunkUpload(ChunkUploadRequest param) throws Exception {
        return null;
    }
}
