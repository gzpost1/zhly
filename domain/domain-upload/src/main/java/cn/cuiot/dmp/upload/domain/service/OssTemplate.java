package cn.cuiot.dmp.upload.domain.service;

import cn.cuiot.dmp.upload.domain.entity.BucketItem;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadRequest;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadResponse;
import cn.cuiot.dmp.upload.domain.entity.ObjectItem;
import cn.cuiot.dmp.upload.domain.exception.OssException;
import cn.cuiot.dmp.upload.domain.storage.FileStorage;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 文件操作模板
 * @author: wuyongchong
 * @date: 2024/4/1 20:02
 */
@Slf4j
@Service
@Component
public class OssTemplate {

    /**
     * 默认预签URL有效期
     */
    private final static Integer DEFAULT_EXPIRE_SECONDS=604800;

    @Autowired
    private List<FileStorage> fileStorageList;

    public FileStorage getFileStorage() {
        if (CollectionUtils.isEmpty(fileStorageList)) {
            throw new RuntimeException("getFileStorage empty");
        }
        return fileStorageList.get(0);
    }

    /**
     * 获取桶列表
     */
    public List<BucketItem> listBuckets() throws Exception {
        try {
            List<BucketItem> bucketItems = getFileStorage().listBuckets();
            return bucketItems;
        } catch (Exception e) {
            log.error("listBuckets error,throwable:{}",e);
            throw new OssException("获取桶列表失败");
        }
    }

    /**
     * 创建桶
     */
    public void createBucket(String bucketName) {
        try {
            getFileStorage().createBucket(bucketName);
        } catch (Exception e) {
            log.error("createBucket error,bucketName:{},throwable:{}", bucketName, e);
            throw new OssException("创建桶失败");
        }
    }

    /**
     * 判断Bucket是否存在, true: 存在, false: 不存在
     */
    public boolean bucketExists(String bucketName) {
        try {
            return getFileStorage().bucketExists(bucketName);
        } catch (Exception e) {
            log.error("bucketExists error,bucketName:{},throwable:{}", bucketName, e);
            throw new OssException("判断Bucket是否存在失败");
        }
    }

    /**
     * 删除桶
     */
    public void removeBucket(String bucketName) {
        try {
            getFileStorage().removeBucket(bucketName);
        } catch (Exception e) {
            log.error("removeBucket error,bucketName:{},throwable:{}", bucketName, e);
            throw new OssException("删除桶失败");
        }
    }

    /**
     * 设置Bucket的策略
     */
    public void setBucketPolicy(String bucketName,String policy) {
        try {
            getFileStorage().setBucketPolicy(bucketName,policy);
        } catch (Exception e) {
            log.error("setBucketPolicy error,bucketName:{},throwable:{}", bucketName, e);
            throw new OssException("设置Bucket策略失败");
        }
    }

    /**
     * 获得Bucket的策略
     */
    public String getBucketPolicy(String bucketName) {
        try {
            return getFileStorage().getBucketPolicy(bucketName);
        } catch (Exception e) {
            log.error("getBucketPolicy error,bucketName:{},throwable:{}", bucketName, e);
            throw new OssException("获得Bucket的策略失败");
        }
    }

    /**
     * 简单上传
     */
    public String putObject(String bucketName, String objectName, InputStream stream,
            String contextType,Boolean privateRead) {
        try {
            return getFileStorage().putObject(bucketName, objectName, stream, contextType,privateRead);
        } catch (Exception e) {
            log.error("putObject error,bucketName:{},objectName:{},throwable:{}", bucketName,
                    objectName, e);
            throw new OssException("上传失败");
        }finally {
            if(null!=stream){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件流
     */
    public InputStream getObject(String bucketName, String objectName) {
        try {
            return getFileStorage().getObject(bucketName, objectName);
        } catch (Exception e) {
            log.error("getObject error,bucketName:{},objectName:{},throwable:{}", bucketName,
                    objectName, e);
            throw new OssException("获取文件流失败");
        }
    }

    /**
     * 获取文件URL
     */
    public String getObjectUrl(String bucketName, String objectName, String urlType,
            Integer expires) {
        try {
            expires = Objects.isNull(expires)?DEFAULT_EXPIRE_SECONDS:expires;
            return getFileStorage().getObjectUrl(bucketName, objectName, urlType, expires);
        } catch (Exception e) {
            log.error(
                    "getObjectUrl error,bucketName:{},objectName:{},urlType:{},expires:{}.throwable:{}",
                    bucketName,
                    objectName, urlType, expires, e);
            throw new OssException("获取文件URL失败");
        }
    }

    /**
     * 判断object是否存在, true: 存在, false: 不存在
     */
    public boolean objectExists(String bucketName, String objectName) {
        try {
            return getFileStorage().objectExists(bucketName, objectName);
        } catch (Exception e) {
            log.error("objectExists error,bucketName:{},objectName:{},throwable:{}", bucketName,
                    objectName, e);
            throw new OssException("判断object是否存在失败");
        }
    }

    /**
     * 删除文件
     */
    public void removeObject(String bucketName, String objectName) {
        try {
            getFileStorage().removeObject(bucketName, objectName);
        } catch (Exception e) {
            log.error("removeObject error,bucketName:{},objectName:{},throwable:{}", bucketName,
                    objectName, e);
            throw new OssException("删除失败");
        }
    }

    /**
     * 批量删除文件
     */
    public void removeObjects(String bucketName, List<String> objectNames) {
        try {
            getFileStorage().removeObjects(bucketName, objectNames);
        } catch (Exception e) {
            log.error("removeObjects error,bucketName:{},objectNames:{},throwable:{}", bucketName,
                    objectNames, e);
            throw new OssException("批量删除文件失败");
        }
    }

    /**
     * 获取文件外链
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) {
        try {
            expires = Objects.isNull(expires)?DEFAULT_EXPIRE_SECONDS:expires;
            return getFileStorage().getPresignedObjectUrl(bucketName, objectName, expires);
        } catch (Exception e) {
            log.error("getPresignedObjectUrl error,bucketName:{},objectName:{},throwable:{}",
                    bucketName,
                    objectName, e);
            throw new OssException("获取文件外链失败");
        }
    }

    /**
     * 拷贝文件
     */
    public void copyObject(String bucketName, String objectName, String targetBucketName,
            String targetObjectName) {
        try {
            getFileStorage().copyObject(bucketName, objectName, targetBucketName, targetObjectName);
        } catch (Exception e) {
            log.error(
                    "copyObject error,bucketName:{},objectName:{},targetBucketName:{},targetObjectName:{},throwable:{}",
                    bucketName,
                    objectName, targetBucketName, targetObjectName, e);
            throw new OssException("拷贝文件失败");
        }
    }

    /**
     * 根据文件前缀查询文件
     */
    public List<ObjectItem> getObjectsByPrefix(String bucketName, String prefix,
            boolean recursive) {
        try {
            return getFileStorage().getObjectsByPrefix(bucketName, prefix, recursive);
        } catch (Exception e) {
            log.error("getObjectsByPrefix error,bucketName:{},prefix:{},recursive:{},throwable:{}",
                    bucketName,
                    prefix, recursive, e);
            throw new OssException("根据文件前缀查询文件失败");
        }
    }

    /**
     * 文件分片上传
     */
    public ChunkUploadResponse chunkUpload(ChunkUploadRequest param) {
        try {
            if (!getFileStorage().bucketExists(param.getBucketName())) {
                getFileStorage().createBucket(param.getBucketName());
            }
            return getFileStorage().chunkUpload(param);
        } catch (Exception e) {
            log.error("chunkUpload error,param:{},throwable:{}", param, e);
            throw new OssException("文件上传失败");
        }finally {
            if(null!=param.getInputStream()){
                try {
                    param.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
