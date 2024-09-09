package cn.cuiot.dmp.externalapi.service.query;

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
     * 楼盘名称
     */
    private String communityName;
}
