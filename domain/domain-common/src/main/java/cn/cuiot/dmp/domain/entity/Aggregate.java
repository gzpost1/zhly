package cn.cuiot.dmp.domain.entity;

import cn.cuiot.dmp.domain.types.id.Identifier;

/**
 * @Description 聚合根的Marker接口
 * @Author yth
 * @Date 2023/8/28 09:16
 * @Version V1.0
 */
public interface Aggregate<ID extends Identifier> extends DomainEntity<ID> {

}
