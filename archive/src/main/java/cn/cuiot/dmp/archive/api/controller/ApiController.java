package cn.cuiot.dmp.archive.api.controller;//	模板

import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Wrapper;
import java.util.List;

/**
 * 内部服务接口
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
    @Autowired
    private HousesArchivesService  housesArchivesService;

    /**
     * 查询楼盘信息
     */
    @PostMapping("/buildingArchiveQueryForList")
    public List<BuildingArchive> queryForList(@RequestBody @Valid BuildingArchiveReq buildingArchiveReq) {
        return buildingArchivesService.apiQueryForList(buildingArchiveReq);
    }

    /**
     * 根据ID获取楼盘信息
     */
    @PostMapping("/lookupBuildingArchiveInfo")
    public BuildingArchive lookupBuildingArchiveInfo(@RequestBody @Valid IdParam idParam){
        return buildingArchivesService.lookupBuildingArchiveInfo(idParam.getId());
    }

    /**
     * 根据id查询房屋信息
     */
    @PostMapping("/queryHousesList")
    public List<HousesArchivesVo> queryHousesList(@RequestBody @Valid IdsReq ids){
        return housesArchivesService.queryHousesList(ids);
    }

}
