package cn.cuiot.dmp.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2024-10-22 11:12
 * @Author by Mujun~
 */
@Data
public class ContractBoardInfoVo implements Serializable {
    /**
     * 楼盘名称
     */
    private String loupanName;
    /**
     * 已出租数量
     */
    private String leaseNum;
    /**
     * 未出租数量
     */
    private String unleaseNum;

}
