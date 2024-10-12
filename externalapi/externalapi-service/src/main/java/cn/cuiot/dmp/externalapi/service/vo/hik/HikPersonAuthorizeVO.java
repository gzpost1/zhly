package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikAcpsAuthConfigSearchResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorResp;
import lombok.Data;

import java.util.Map;

/**
 * 授权设备
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonAuthorizeVO {

    /**
     * 是否选中（0:否，1:是）
     */
    private Byte isSelect;

    /**
     * 第三方门禁id
     */
    private String thirdDoorId;

    /**
     * 所属区域目录名，符号 @ 分隔
     */
    private String regionPathName;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 构造vo
     *
     * @return HikPersonAuthorizeVO
     * @Param dataItem 参数
     * @Param map 参数
     */
    public static HikPersonAuthorizeVO buildHikPersonAuthorizeVO(HikDoorResp.DataItem dataItem,
                                                                 Map<String, HikAcpsAuthConfigSearchResp.PermissionConfig> map) {
        HikPersonAuthorizeVO vo = new HikPersonAuthorizeVO();
        vo.setThirdDoorId(dataItem.getIndexCode());
        vo.setName(dataItem.getName());
        vo.setRegionPathName(dataItem.getRegionPathName());
        // 数据库存在则设置为已选择
        vo.setIsSelect(map.containsKey(dataItem.getIndexCode()) ? EntityConstants.YES : EntityConstants.NO);
        return vo;
    }
}
