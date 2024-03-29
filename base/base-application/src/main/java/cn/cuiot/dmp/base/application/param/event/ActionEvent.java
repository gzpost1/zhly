package cn.cuiot.dmp.base.application.param.event;

import lombok.Data;

/**
 * @Description
 * @Author yth
 * @Date 2023/8/14 14:55
 * @Version V1.0
 */
@Data
public abstract class ActionEvent {

    /**
     * 值参考EventActionEnum
     */
    private String action;

}
