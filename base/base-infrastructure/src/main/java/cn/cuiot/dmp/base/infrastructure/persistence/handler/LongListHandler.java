package cn.cuiot.dmp.base.infrastructure.persistence.handler;

import com.alibaba.fastjson.TypeReference;
import java.util.List;

/**
 * @author zhangjg
 * @date 2023年11月07日 17:28
 */
public class LongListHandler extends ListTypeHandler<Long>{
    @Override
    protected TypeReference<List<Long>> specificType() {
        return new TypeReference<List<Long>>() {
        };
    }
}
