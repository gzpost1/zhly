package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 目标数据查询返回dto
 *
 * @author: wuyongchong
 * @date: 2024/5/15 10:11
 */
@Data
public class TargetDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    public TargetDto() {
    }

    public TargetDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
