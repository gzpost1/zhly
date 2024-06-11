package cn.cuiot.dmp.archive.application.param.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/6/7 15:15
 */
@Data
public class HouseKeeperDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属楼盘ID
     */
    @NotNull(message = "所属楼盘不能为空")
    private Long communityId;

    /**
     * 所属员工ID
     */
    @NotNull(message = "绑定员工不能为空")
    private Long staffId;


    /**
     * 管家昵称
     */
    private String nickName;


    /**
     * 联系方式
     */
    private String contactWay;

}
