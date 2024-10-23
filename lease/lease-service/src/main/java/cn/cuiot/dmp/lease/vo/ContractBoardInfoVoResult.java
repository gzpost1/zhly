package cn.cuiot.dmp.lease.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description TODO
 * @Date 2024-10-22 11:12
 * @Author by Mujun~
 */
@Data
public class ContractBoardInfoVoResult implements Serializable {
    /**
     * 统计信息数量信息
     */
    private ContractBoardVo contractBoardVo;
    /**
     * 柱状楼盘信息
     */
    private List<ContractBoardInfoVo> contractBoardInfoVo;

}
