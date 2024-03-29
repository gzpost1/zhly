package cn.cuiot.dmp.gateway.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author wangyh
 */
public class SecrtUtil {

    private SecrtUtil() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    /**
     * 过滤配置透传URL
     */
    public static Set<String> filterThroughUrl = new CopyOnWriteArraySet<String>();
}
