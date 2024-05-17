package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 字典类型
 * @author: wuyongchong
 * @date: 2024/4/29 9:29
 */
@Data
public class DictTypeVO implements Serializable {

    /**
     * 编码
     */
    private String code;
    /**
     * 字典列表
     */
    private List<DictDataVO> items;
}
