package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 格物消防-通知记录 业务层
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
@Component
public class GwFirefightNoticeRecordService {

    @Autowired
    private RedisUtil redisUtil;

    public static final String CACHE_KEY = "gwFirefight:notice:";

    /**
     * 查询最新数据
     * @Param type 类型
     * @return List
     */
    public String queryDataIdCache(String type) {
        boolean haveKey = redisUtil.hasKey(CACHE_KEY + type);
        if (haveKey) {
            return redisUtil.get(CACHE_KEY + type);
        }
        return null;
    }

    public void setDataIdCache(String dataId, String type) {
        redisUtil.set(CACHE_KEY + type, dataId);
    }
}
