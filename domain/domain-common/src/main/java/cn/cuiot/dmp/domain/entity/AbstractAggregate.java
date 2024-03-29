package cn.cuiot.dmp.domain.entity;

import cn.cuiot.dmp.domain.types.id.Identifier;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:12
 * @Version V1.0
 */
@SuperBuilder
@NoArgsConstructor
public class AbstractAggregate<ID extends Identifier> extends AbstractDomainEntity<ID> implements Aggregate<ID> {}
