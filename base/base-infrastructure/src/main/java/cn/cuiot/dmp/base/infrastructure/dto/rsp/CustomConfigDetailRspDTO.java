package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class CustomConfigDetailRspDTO implements Serializable {

    private static final long serialVersionUID = -2040359093585737726L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 自定义配置详情名称
     */
    private String name;

}
