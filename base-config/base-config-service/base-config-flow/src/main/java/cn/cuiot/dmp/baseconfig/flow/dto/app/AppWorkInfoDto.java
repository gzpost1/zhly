package cn.cuiot.dmp.baseconfig.flow.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/5/29 16:17
 */
@Data
public class  AppWorkInfoDto {

    /**
     * 工单id
     */
    private Long procInstId;

    /**
     * 流程名称
     */
    private  String workName;

    /**
     * 提交人userId
     */
    private Long  createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date  createTime;

    /**
     *1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

    /**
     * 是否超时 0未超时 1 已超时
     */
    private Byte outTime;
}
