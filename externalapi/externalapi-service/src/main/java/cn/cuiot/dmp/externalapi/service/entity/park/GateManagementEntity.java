package cn.cuiot.dmp.externalapi.service.entity.park;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 道闸管理
 * @author pengjian
 * @since 2024-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gate_management")
public class GateManagementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通道编号
     */
    @TableId("node_id")
    private Integer nodeId;


    /**
     * 通道名称
     */
    private String nodeName;


    /**
     * 使用类别 0停车场内1入口2出口3中间入口4中间出口
     */
    private Integer useType;


    /**
     * 道闸状态1常抬0正常-1状态未知
     */
    private Integer status;



    /**
     * 车场id
     */
    private Integer parkId;

}
