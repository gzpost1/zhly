package cn.cuiot.dmp.system.application.param.command;

import lombok.Data;

/**
 * @Description 更新用户命令
 * @Author 犬豪
 * @Date 2023/9/5 12:12
 * @Version V1.0
 */
@Data
public class UpdateUserCommand {
    private Long id;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 最后上线ip
     */
    private String lastOnlineIp;
}
