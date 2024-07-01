package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 基础 VO
 *
 * @date 2022/1/11 19:48
 * @author horadirm
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseVO implements Serializable {

    private static final long serialVersionUID = -778904778982209738L;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

}
