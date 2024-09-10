package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author pengjian
 * @create 2024/9/9 16:39
 */
@Data
public class GageManagePageVO {

    /**
     * 车场id
      */
    private Integer parkId;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 通道编号
     */
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
     * 楼盘id
     */
    private Long communityId;

    /**
     * 楼盘名称
     */
    private String communityName;



}
