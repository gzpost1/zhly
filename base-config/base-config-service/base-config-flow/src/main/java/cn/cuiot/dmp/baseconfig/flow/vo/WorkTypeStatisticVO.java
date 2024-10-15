package cn.cuiot.dmp.baseconfig.flow.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkTypeStatisticVO implements Serializable {

    private static final long serialVersionUID = -6376824329211305701L;

    private String workType;

    private Long count;

}