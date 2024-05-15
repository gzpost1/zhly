package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.param.vo.AreaTreeNodeVO;
import cn.cuiot.dmp.system.application.service.SysAreaService;
import cn.cuiot.dmp.system.domain.aggregate.AreaTreeNode;
import cn.cuiot.dmp.system.domain.repository.SysAreaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Slf4j
@Service
public class SysAreaServiceImpl implements SysAreaService {

    @Autowired
    private SysAreaRepository sysAreaRepository;

    @Override
    public List<AreaTreeNodeVO> getAreaTree() {
        List<AreaTreeNode> areaTreeNodeList = sysAreaRepository.getAreaTree();
        if (CollectionUtils.isEmpty(areaTreeNodeList)) {
            return new ArrayList<>();
        }
        List<AreaTreeNodeVO> areaTreeNodeVOList = areaTreeNodeList.stream()
                .map(o -> {
                    AreaTreeNodeVO areaTreeNodeVO = new AreaTreeNodeVO();
                    BeanUtils.copyProperties(o, areaTreeNodeVO);
                    return areaTreeNodeVO;
                })
                .collect(Collectors.toList());
        return TreeUtil.makeTree(areaTreeNodeVOList);
    }

    @Override
    public String getAreaName(String areaCode) {
        return sysAreaRepository.getAreaName(areaCode);
    }

}
