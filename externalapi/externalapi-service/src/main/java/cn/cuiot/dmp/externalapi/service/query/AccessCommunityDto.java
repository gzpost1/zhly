package cn.cuiot.dmp.externalapi.service.query;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;


/**
 * @author pengjian
 * @create 2024/9/4 16:53
 */
@Data
public class AccessCommunityDto {

    /**
     * 设备序列号
     */
    @Excel(name = "设备序列号", orderNum = "0", width = 20)
    private String deviceNo;


    /**
     * 名字
     */
    @Excel(name = "门禁名称", orderNum = "1", width = 20)
    private String name;


    /**
     * 设备状态,1:未绑定 2:已绑定 3:已禁用
     */
    @Excel(name = "设备状态", orderNum = "3", width = 20 , replace = {"未绑定_1", "已绑定_2", "已禁用_3"})
    private String state;


    /**
     * 设备在线状态, 1:在线 2：不在线
     */
    @Excel(name = "在线状态", orderNum = "4", width = 20 , replace = {"在线_1", "不在线_2"})
    private String onlineState;


    /**
     * 楼盘id
     */
    private Long communityId;


    /**
     * 楼盘名称
     */
    @Excel(name = "所属楼盘", orderNum = "2", width = 20)
    private String communityName;
}
