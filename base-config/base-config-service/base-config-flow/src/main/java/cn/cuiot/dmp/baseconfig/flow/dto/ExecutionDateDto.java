package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author pengjian
 * @create 2024/5/8 9:23
 */
@Data
public class ExecutionDateDto {
    /**
     * 执行时间
     */
    private LocalDate executionDate;
}
