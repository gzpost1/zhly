package cn.cuiot.dmp.upload.infrastructure.cache;

import cn.cuiot.dmp.upload.domain.entity.ChunkContext;
import com.alibaba.fastjson.JSON;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * OSS缓存
 * @author: wuyongchong
 * @date: 2024/4/2 9:48
 */
@Slf4j
@Component
public class OssRedisCache {

    /**
     * 缓存前缀
     */
    private final static String CHUNK_CACHE_PREFIX="chunkCachePrefix:";

    /**
     * 缓存时间
     */
    private final static Long CHUNK_CACHE_EXPIRE_HOUR=24L;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分片缓存
     * @param taskId
     * @param chunkContext
     */
    public void putChunkCache(String taskId, ChunkContext chunkContext) {
        stringRedisTemplate.opsForValue().set(CHUNK_CACHE_PREFIX + taskId,
                JSON.toJSONString(chunkContext),
                CHUNK_CACHE_EXPIRE_HOUR,
                TimeUnit.HOURS);
    }

    /**
     * 获取缓存分片
     * @param taskId
     * @return
     */
    public ChunkContext getChunkCache(String taskId) {
        String jsonStr = stringRedisTemplate.opsForValue().get(CHUNK_CACHE_PREFIX + taskId);
        if(StringUtils.isNotBlank(jsonStr)){
            return JSON.parseObject(jsonStr,ChunkContext.class);
        }
        return null;
    }

    /**
     * 删除缓存分片
     * @param taskId
     */
    public void invalidateChunkCache(String taskId) {
        stringRedisTemplate.delete(CHUNK_CACHE_PREFIX+taskId);
    }
}
