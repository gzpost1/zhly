package cn.cuiot.dmp.base.infrastructure.persistence.handler;

import com.alibaba.fastjson.TypeReference;
import java.util.List;

/**
 * @author zhangjg
 * @date 2023年11月07日 17:28
 */
public class ScopeHandler extends ListTypeHandler<String>{
    @Override
    protected TypeReference<List<String>> specificType() {
        return new TypeReference<List<String>>() {
        };
    }
}
