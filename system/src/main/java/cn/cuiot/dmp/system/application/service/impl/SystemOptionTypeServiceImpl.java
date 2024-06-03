package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.system.application.constant.SystemOptionTypeConstant;
import cn.cuiot.dmp.system.application.param.dto.SystemOptionTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.SystemOptionTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.SystemOptionTypeVO;
import cn.cuiot.dmp.system.application.service.SystemOptionTypeService;
import cn.cuiot.dmp.system.domain.aggregate.SystemOptionType;
import cn.cuiot.dmp.system.domain.repository.SystemOptionTypeRepository;
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
public class SystemOptionTypeServiceImpl implements SystemOptionTypeService {

    @Autowired
    private SystemOptionTypeRepository systemOptionTypeRepository;

    @Autowired
    private CustomConfigRepository customConfigRepository;

    @Override
    public List<SystemOptionTypeVO> queryForList(SystemOptionTypeQueryDTO queryDTO) {
        SystemOptionType systemOptionType = new SystemOptionType();
        BeanUtils.copyProperties(queryDTO, systemOptionType);
        List<SystemOptionType> systemOptionTypeList = systemOptionTypeRepository.queryForList(systemOptionType);
        if (CollectionUtils.isEmpty(systemOptionTypeList)) {
            return new ArrayList<>();
        }
        // 是否需要初始化自定义配置
        customConfigRepository.initCustomConfig(queryDTO.getCompanyId(), queryDTO.getUserId());
        return systemOptionTypeList.stream()
                // 前端筛选需要过滤二维码档案
                .filter(item -> SystemOptionTypeEnum.archiveTypeFlag(item.getSystemOptionType()) &&
                        !SystemOptionTypeEnum.CODE_ARCHIVE.getCode().equals(item.getSystemOptionType()))
                .map(o -> {
                    SystemOptionTypeVO systemOptionTypeVO = new SystemOptionTypeVO();
                    BeanUtils.copyProperties(o, systemOptionTypeVO);
                    return systemOptionTypeVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SystemOptionTypeVO queryForDetail(Long id) {
        SystemOptionType systemOptionType = systemOptionTypeRepository.queryForDetail(id);
        SystemOptionTypeVO systemOptionTypeVO = new SystemOptionTypeVO();
        BeanUtils.copyProperties(systemOptionType, systemOptionTypeVO);
        return systemOptionTypeVO;
    }

    @Override
    public List<SystemOptionTypeTreeNodeVO> queryForTree(SystemOptionTypeQueryDTO queryDTO) {
        SystemOptionType systemOptionType = new SystemOptionType();
        BeanUtils.copyProperties(queryDTO, systemOptionType);
        List<SystemOptionType> systemOptionTypeList = systemOptionTypeRepository.queryForList(systemOptionType);
        if (CollectionUtils.isEmpty(systemOptionTypeList)) {
            return new ArrayList<>();
        }
        // 是否需要初始化自定义配置
        customConfigRepository.initCustomConfig(queryDTO.getCompanyId(), queryDTO.getUserId());
        List<SystemOptionTypeTreeNodeVO> systemOptionTypeTreeNodeVOList = new ArrayList<>();
        SystemOptionTypeTreeNodeVO rootSystemOptionTypeTreeNodeVO = initRootArchiveTypeVO();
        List<SystemOptionTypeTreeNodeVO> childrenArchiveTypeTreeNodeList = systemOptionTypeList.stream()
                .map(o -> new SystemOptionTypeTreeNodeVO(o.getId().toString(),
                        o.getSystemOptionType(), SystemOptionTypeConstant.DEFAULT_ROOT_ID, o.getName(),
                        SystemOptionTypeConstant.FIRST_LEVEL_TYPE))
                .collect(Collectors.toList());
        rootSystemOptionTypeTreeNodeVO.setChildren(childrenArchiveTypeTreeNodeList);
        systemOptionTypeTreeNodeVOList.add(rootSystemOptionTypeTreeNodeVO);
        return systemOptionTypeTreeNodeVOList;
    }

    private SystemOptionTypeTreeNodeVO initRootArchiveTypeVO() {
        SystemOptionTypeTreeNodeVO rootSystemOptionTypeTreeNodeVO = new SystemOptionTypeTreeNodeVO();
        rootSystemOptionTypeTreeNodeVO.setId(SystemOptionTypeConstant.DEFAULT_ROOT_ID);
        rootSystemOptionTypeTreeNodeVO.setName(SystemOptionTypeConstant.DEFAULT_ROOT_NAME);
        rootSystemOptionTypeTreeNodeVO.setLevelType(SystemOptionTypeConstant.ROOT_LEVEL_TYPE);
        rootSystemOptionTypeTreeNodeVO.setParentId(String.valueOf(SystemOptionTypeConstant.DEFAULT_PARENT_ID));
        return rootSystemOptionTypeTreeNodeVO;
    }

}
