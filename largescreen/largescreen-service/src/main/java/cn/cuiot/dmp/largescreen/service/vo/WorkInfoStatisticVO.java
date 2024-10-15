package cn.cuiot.dmp.largescreen.service.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkInfoStatisticVO implements Serializable {

    private static final long serialVersionUID = -4473200991504484522L;

    /**
     * 临时工单
     */
    private Long tempWork;


    /**
     * 循环工单
     */
    private Long circleWork;


    /**
     * 已完成工单
     */
    private Long finishWork;

    /**
     * 待完成
     */
    private Long working;

    /**
     * top5 工单类型
     */
    private List<WorkTypeStatisticResVO> topWorkType;


}
