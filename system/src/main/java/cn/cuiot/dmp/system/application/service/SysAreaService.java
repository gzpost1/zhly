package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.AreaTreeNodeVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/14
 */
public interface SysAreaService {

    /**
     * 获取地区树
     */
    List<AreaTreeNodeVO> getAreaTree();

    /**
     * 根据区域编码获取区域名称
     */
    String getAreaName(String areaCode);

}
