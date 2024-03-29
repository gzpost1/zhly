package cn.cuiot.dmp.system.application.param.assembler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @Description 领域层模型 转换 各种其他模型（DTO、VO、EVENT...），不局限于DTO，但DTO最常见
 * @Author 犬豪
 * @Date 2023/9/5 17:31
 * @Version V1.0
 */
public interface Assembler<D, T> {

    T toDTO(D domain);


    default List<T> toDTOList(List<D> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
