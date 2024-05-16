package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/14 20:38
 */
@Data
public class BatchBusinessDto {

    /**
     * 工单中心传
     */
    private List<String> processInstanceId = new ArrayList<>();

    private String comments;

    private String reason;

    private Byte businessType;

    /**
     * 审批中心传
     */
    private List<String> taskIds = new ArrayList<>();

}
