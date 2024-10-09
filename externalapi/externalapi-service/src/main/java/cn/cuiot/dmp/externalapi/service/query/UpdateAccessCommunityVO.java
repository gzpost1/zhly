package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/4 14:30
 */
@Data
public class UpdateAccessCommunityVO {

    /**
     * 楼盘id
     */
    private Long communityId;

    /**
     * 设备序列号
     */
    @NotNull(message = "设备序列号不能为空")
    private List<String> deviceNos;
}
