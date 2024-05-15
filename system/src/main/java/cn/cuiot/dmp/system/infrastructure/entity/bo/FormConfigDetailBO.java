package cn.cuiot.dmp.system.infrastructure.entity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/15
 */
@Data
public class FormConfigDetailBO implements Serializable {

    private static final long serialVersionUID = 4629438315852566722L;

    /**
     * 组件名称
     */
    private String name;

    /**
     * 组件属性
     */
    private FormConfigDetailPropBO props;

    @Data
    public static class FormConfigDetailPropBO {

        /**
         * 是否必填
         */
        private Boolean required;

        /**
         * 常用选项ID
         */
        private String typeId;

    }
}
