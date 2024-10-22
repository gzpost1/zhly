package cn.cuiot.dmp.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2024-10-22 11:12
 * @Author by Mujun~
 */
@Data
public class ContractBoardVo implements Serializable {
    /**
     * 楼盘总数
     */
    private Integer loupanNum;
    /**
     * 房屋总数
     */
    private Integer houseNum;
    /**
     * 已租房屋
     */
    private Integer leaseHouseNum;
    /**
     * 未租房屋
     */
    private Integer unleaseHouseNum;

    /**
     * 意向合同数量
     */
    private Integer intentionNum;
    /**
     * 租赁合同数量
     */
    private Integer leaseNum;



}
