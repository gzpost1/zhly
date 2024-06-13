package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/5/29 11:36
 */
@Data
public class TaskUserInfoDto {
    /**
     *任务id
     */
    private Long taskId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
}
