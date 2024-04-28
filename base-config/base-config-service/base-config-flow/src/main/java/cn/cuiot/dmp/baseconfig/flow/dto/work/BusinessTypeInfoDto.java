package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BusinessTypeInfoDto {

    /**
     * 业务类型
     */
    private Byte businessType;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务实例id
     */
    private Long procInstId;
}