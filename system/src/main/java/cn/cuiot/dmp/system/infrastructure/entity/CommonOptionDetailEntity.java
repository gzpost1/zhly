package cn.cuiot.dmp.system.infrastructure.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionDetailEntity implements Serializable {

    private static final long serialVersionUID = -5392269865463888069L;

    /**
     * 常用选项ID
     */
    private Long id;

    /**
     * 常用选项详情
     */
    private String commonOptionDetail;

}
