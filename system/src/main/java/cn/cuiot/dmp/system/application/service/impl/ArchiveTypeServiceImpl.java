package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeVO;
import cn.cuiot.dmp.system.application.service.ArchiveTypeService;
import cn.cuiot.dmp.system.domain.aggregate.ArchiveType;
import cn.cuiot.dmp.system.domain.repository.ArchiveTypeRepository;
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

    @Override
    public List<ArchiveTypeVO> queryForList(ArchiveType archiveType) {
        List<ArchiveType> archiveTypeList = archiveTypeRepository.queryForList(archiveType);
        if (CollectionUtils.isEmpty(archiveTypeList)) {
            return new ArrayList<>();
        }
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

}
