package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.AreaTreeNode;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/14
 */
public interface SysAreaRepository {

    /**
     * 获取地区树
     */
    List<AreaTreeNode> getAreaTree();

    /**
     * 根据区域编码获取区域名称
     */
    String getAreaName(String areaCode);

}
