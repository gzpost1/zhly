package cn.cuiot.dmp.system.infrastructure.entity.vo;

import java.util.List;
import lombok.Data;


/**
 * @author cwl
 * @classname SelectCascadeVO
 * @description 级联下拉框通用对象
 * @date 2021/12/28
 */
@Data
public class SelectCascadeVO {

    /**
     * 唯一键,就是id
     */
    private String key;

    /**
     * 显示给用户看的名称
     */
    private String value;

    /**
     * 类型，用来识别当前节点的类型
     */
    private String type;

    /**
     * 层级
     */
    private Integer level;

    List<SelectCascadeVO> children;

    /**
     * 0为没有下一级，1为有下一级
     */
    private String hasChild;
}
