package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/14 20:38
 */
@Data
public class BatchBusinessDto {

    private List<String> processInstanceId;

    private String comments;

    private String reason;

    private Byte BusinessType;

}
