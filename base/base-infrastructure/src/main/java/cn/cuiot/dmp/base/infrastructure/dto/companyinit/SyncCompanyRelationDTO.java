package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-11-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncCompanyRelationDTO<T> implements Serializable {

    /**
     * 替换的实体
     */
    private T entity;

    /**
     * 老数据的id
     */
    private Long oldId;
}
