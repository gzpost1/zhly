package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.ArchiveTypeConstant;
import cn.cuiot.dmp.system.application.param.dto.ArchiveTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeVO;
import cn.cuiot.dmp.system.application.service.ArchiveTypeService;
import cn.cuiot.dmp.system.domain.aggregate.ArchiveType;
import cn.cuiot.dmp.system.domain.repository.ArchiveTypeRepository;
import cn.cuiot.dmp.system.domain.repository.CustomConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Slf4j
@Repository
public class ArchiveTypeServiceImpl implements ArchiveTypeService {

    @Autowired
    private ArchiveTypeRepository archiveTypeRepository;

    @Autowired
    private CustomConfigRepository customConfigRepository;

    @Override
    public List<ArchiveTypeVO> queryForList(ArchiveTypeQueryDTO queryDTO) {
        ArchiveType archiveType = new ArchiveType();
        BeanUtils.copyProperties(queryDTO, archiveType);
        List<ArchiveType> archiveTypeList = archiveTypeRepository.queryForList(archiveType);
        if (CollectionUtils.isEmpty(archiveTypeList)) {
            return new ArrayList<>();
        }
        // 是否需要初始化自定义配置
        customConfigRepository.initCustomConfig(queryDTO.getCompanyId(), queryDTO.getUserId());
        return archiveTypeList.stream()
                .map(o -> {
                    ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
                    BeanUtils.copyProperties(o, archiveTypeVO);
                    return archiveTypeVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArchiveTypeVO queryForDetail(Long id) {
        ArchiveType archiveType = archiveTypeRepository.queryForDetail(id);
        ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
        BeanUtils.copyProperties(archiveType, archiveTypeVO);
        return archiveTypeVO;
    }

    @Override
    public List<ArchiveTypeTreeNodeVO> queryForTree(ArchiveTypeQueryDTO queryDTO) {
        ArchiveType archiveType = new ArchiveType();
        BeanUtils.copyProperties(queryDTO, archiveType);
        List<ArchiveType> archiveTypeList = archiveTypeRepository.queryForList(archiveType);
        if (CollectionUtils.isEmpty(archiveTypeList)) {
            return new ArrayList<>();
        }
        List<ArchiveTypeTreeNodeVO> archiveTypeTreeNodeVOList = new ArrayList<>();
        ArchiveTypeTreeNodeVO rootArchiveTypeTreeNodeVO = initRootArchiveTypeVO();
        List<ArchiveTypeTreeNodeVO> childrenArchiveTypeTreeNodeList = archiveTypeList.stream()
                .map(o -> new ArchiveTypeTreeNodeVO(o.getId().toString(),
                        o.getArchiveType(), ArchiveTypeConstant.DEFAULT_ROOT_ID, o.getName(),
                        ArchiveTypeConstant.FIRST_LEVEL_TYPE))
                .collect(Collectors.toList());
        rootArchiveTypeTreeNodeVO.setChildren(childrenArchiveTypeTreeNodeList);
        archiveTypeTreeNodeVOList.add(rootArchiveTypeTreeNodeVO);
        return archiveTypeTreeNodeVOList;
    }

    private ArchiveTypeTreeNodeVO initRootArchiveTypeVO() {
        ArchiveTypeTreeNodeVO rootArchiveTypeTreeNodeVO = new ArchiveTypeTreeNodeVO();
        rootArchiveTypeTreeNodeVO.setId(ArchiveTypeConstant.DEFAULT_ROOT_ID);
        rootArchiveTypeTreeNodeVO.setName(ArchiveTypeConstant.DEFAULT_ROOT_NAME);
        rootArchiveTypeTreeNodeVO.setLevelType(ArchiveTypeConstant.ROOT_LEVEL_TYPE);
        rootArchiveTypeTreeNodeVO.setParentId(String.valueOf(ArchiveTypeConstant.DEFAULT_PARENT_ID));
        return rootArchiveTypeTreeNodeVO;
    }

}
