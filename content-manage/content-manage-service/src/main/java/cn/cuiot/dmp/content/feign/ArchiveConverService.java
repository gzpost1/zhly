package cn.cuiot.dmp.content.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 17:18
 */
@Service
@Slf4j
public class ArchiveConverService {

    @Autowired
    private ArchiveFeignService archiveFeignService;

    public HashMap<Long, BuildingArchive> getBuildingArchiveMapByIds(List<Long> buildingIds) {
        if (buildingIds == null || buildingIds.isEmpty()) {
            return new HashMap<>();
        }
        List<BuildingArchive> buildingArchives = archiveFeignService.buildingArchiveQueryForList(new BuildingArchiveReq().setIdList(buildingIds)).getData();
        if (buildingArchives == null || buildingArchives.isEmpty()) {
            return new HashMap<>();
        }
        HashMap<Long, BuildingArchive> collect = buildingArchives.stream().collect(Collectors.toMap(BuildingArchive::getId, buildingArchive -> buildingArchive, (k1, k2) -> k1, HashMap::new));
        log.info("getBuildingArchiveMapByIds-resp:{}",collect);
        return collect;
    }

    /**
     * 查询当前组织及下级组织下的楼盘列表
     */
    public List<Long> lookupBuildingArchiveByDepartmentList(DepartmentReqDto reqDto) {
        List<BuildingArchive> data = archiveFeignService.lookupBuildingArchiveByDepartmentList(reqDto).getData();
        if (CollUtil.isNotEmpty(data)) {
            List<Long> collect = data.stream().map(BuildingArchive::getId).collect(Collectors.toList());
            log.info("lookupBuildingArchiveByDepartmentList-resp:{}",collect);
            return collect;
        }
        return new ArrayList<>();
    }
}
