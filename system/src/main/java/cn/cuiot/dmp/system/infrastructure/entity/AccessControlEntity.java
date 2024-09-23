package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门禁管理
 * @author pengjian
 * @since 2024-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_access_control")
public class AccessControlEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备序列号
     */
    @TableId("id")
    private String deviceNo;


    /**
     * 名字
     */
    private String name;


    /**
     * 设备状态,1:未绑定 2:已绑定 3:已禁用
     */
    private String state;


    /**
     * 设备在线状态, 1:在线 2：不在线
     */
    private String onlineState;


    /**
     * 楼盘id
     */
    private Long communityId;


    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 更新人
     */
    private Long updateUser;



}
