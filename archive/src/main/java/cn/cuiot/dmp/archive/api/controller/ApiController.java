package cn.cuiot.dmp.archive.api.controller;//	模板

import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 14:26
 */
@InternalApi
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private BuildingArchivesService buildingArchivesService;

    @PostMapping("/buildingArchiveQueryForList")
    public List<BuildingArchive> queryForList(@RequestBody @Valid BuildingArchiveReq buildingArchiveReq) {
        return buildingArchivesService.apiQueryForList(buildingArchiveReq);
    }
}
