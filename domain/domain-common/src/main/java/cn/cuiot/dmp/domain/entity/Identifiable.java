package cn.cuiot.dmp.domain.entity;

import cn.cuiot.dmp.domain.types.id.Identifier;

/**
 * @Author yth
 * @Date 2023/8/28 09:21
 * @Version V1.0
 */
public interface Identifiable<ID extends Identifier> {
    ID getId();
}