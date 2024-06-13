package cn.cuiot.dmp.baseconfig.flow.dto.app;

import cn.cuiot.dmp.baseconfig.flow.dto.work.TaskUserInfoDto;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/29 14:17
 */
@Data
public class AppTransferTaskDto {

    /**
     * 对应的任务id需要转交给设 taskId->userId 选择的人
     */
    private List<TaskUserInfoDto> taskUserInfos;
}
