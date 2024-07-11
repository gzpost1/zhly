package cn.cuiot.dmp.base.infrastructure.persistence.handler;

import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * @author zhangjg
 * @date 2023年11月07日 17:28
 */
public class ByteListHandler extends ListTypeHandler<Byte> {
    @Override
    protected TypeReference<List<Byte>> specificType() {
        return new TypeReference<List<Byte>>() {
        };
    }
}
