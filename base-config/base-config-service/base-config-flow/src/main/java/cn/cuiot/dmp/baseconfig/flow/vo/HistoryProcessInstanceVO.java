package cn.cuiot.dmp.baseconfig.flow.vo;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.UserEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:47
 */
@Data
public class HistoryProcessInstanceVO {
    private String processInstanceId;
    private String processDefinitionName;
    private UserInfo startUser;
    private UserEntity users;
    private Date startTime;
    private Date endTime;
    private String currentActivityName;
    private String businessStatus;
    private String duration;
}
