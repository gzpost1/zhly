package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.param.vo.AreaTreeNodeVO;
import cn.cuiot.dmp.system.application.service.SysAreaService;
import cn.cuiot.dmp.system.domain.aggregate.AreaTreeNode;
import cn.cuiot.dmp.system.domain.repository.SysAreaRepository;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String SYS_AREA_TREE_KEY_NEW = "sys:area:getAreaTree";

    @Override
    public List<AreaTreeNodeVO> getAreaTree() {
        String cache = stringRedisTemplate.opsForValue().get(SYS_AREA_TREE_KEY_NEW);
        List<AreaTreeNode> dictAreaTrees;
        if (StrUtil.isNotBlank(cache)) {
            log.info("getAreaTree using cache");
            dictAreaTrees = JsonUtil.readValue(cache, new TypeReference<List<AreaTreeNode>>() {
            });
        } else {
            dictAreaTrees = sysAreaRepository.getAreaTree();
            stringRedisTemplate.opsForValue().set(SYS_AREA_TREE_KEY_NEW, JsonUtil.writeValueAsString(dictAreaTrees),
                    12 * 60 * 60, TimeUnit.SECONDS);
        }
        if (CollectionUtils.isEmpty(dictAreaTrees)) {
            return new ArrayList<>();
        }
        List<AreaTreeNodeVO> areaTreeNodeVOList = dictAreaTrees.stream()
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
