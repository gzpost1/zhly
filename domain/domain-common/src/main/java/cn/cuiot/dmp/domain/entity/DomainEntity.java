package cn.cuiot.dmp.domain.entity;

import cn.cuiot.dmp.domain.types.id.Identifier;

/**
 * @Description 实体类的Marker接口
 * @Author yth
 * @Date 2023/8/28 09:22
 * @Version V1.0
 */
public interface DomainEntity<ID extends Identifier> extends Identifiable<ID> {

}
