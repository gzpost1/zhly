package cn.cuiot.dmp.content.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 17:18
 */
@Service
public class ArchiveConverService {

    @Autowired
    private ArchiveFeignService archiveFeignService;

    public HashMap<Long, BuildingArchive> getBuildingArchiveMapByIds(List<Long> buildingIds) {
        if (buildingIds == null || buildingIds.isEmpty()) {
            return new HashMap<>();
        }
        List<BuildingArchive> buildingArchives = archiveFeignService.buildingArchiveQueryForList(new BuildingArchiveReq().setIdList(buildingIds));
        if (buildingArchives == null || buildingArchives.isEmpty()) {
            return new HashMap<>();
        }
        return buildingArchives.stream().collect(Collectors.toMap(BuildingArchive::getId, buildingArchive -> buildingArchive, (k1, k2) -> k1, HashMap::new));
    }
}
