package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEventEntity;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwSmogEventMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogEventQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogEventPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 格物烟雾报警器事件信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-24
 */
@Service
public class GwSmogEventService extends ServiceImpl<GwSmogEventMapper, GwSmogEventEntity> {

    @Autowired
    private SystemApiService systemApiService;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private ExcelExportService excelExportService;

    public IPage<GwSmogEventPageVo> queryForPage(GwSmogEventQuery query){
        IPage<GwSmogEventPageVo> page = baseMapper.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()),query);
        List<GwSmogEventPageVo> records = page.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return page;
        }
        //转译楼盘
        List<Long> buildingIds = records.stream().map(vo -> vo.getBuildingId()).distinct().collect(Collectors.toList());
        Map<Long, String> buildingMap = Optional.ofNullable(queryBuildingInfo(buildingIds)).orElse(Lists.newArrayList())
                .stream().collect(Collectors.toMap(BuildingArchive::getId, BuildingArchive::getName));
        //转译部门
        List<Long> deptIds = records.stream().map(vo -> vo.getDeptId()).distinct().collect(Collectors.toList());
        Map<Long, String> deptMap = Optional.ofNullable(queryDeptList(deptIds)).orElse(Lists.newArrayList())
                .stream().collect(Collectors.toMap(DepartmentDto::getId, DepartmentDto::getName));

        for(GwSmogEventPageVo vo : records){
            vo.setBuildingName(buildingMap.get(vo.getBuildingId()));
            vo.setDeptName(deptMap.get(vo.getDeptId()));
        }
        return page;
    }
    /**
     * 查询楼盘信息
     */
    private List<BuildingArchive> queryBuildingInfo(List<Long> buildingIds) {
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(buildingIds);
        return systemApiService.buildingArchiveQueryForList(req);
    }

    /**
     * 获取组织信息
     */
    private List<DepartmentDto> queryDeptList(List<Long> deptIds) {
        DepartmentReqDto dto = new DepartmentReqDto();
        dto.setDeptIdList(deptIds);
        return apiSystemService.lookUpDepartmentList(dto);
    }

    /**
     * 烟雾报警器设备告警导出
     * @param query
     */
    public void export(GwSmogEventQuery query){
        excelExportService.excelExport(ExcelDownloadDto.<GwSmogEventQuery>builder().loginInfo(LoginInfoHolder
                        .getCurrentLoginInfo()).query(query)
                .title("烟雾报警器设备告警导出").fileName("烟雾报警器设备告警导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("烟雾报警器设备告警导出")
                .build(), GwSmogEventPageVo.class, this::queryExport);

    }

    /**
     * 门禁列表导出
     * @param downloadDto
     * @return
     */
    public IPage<GwSmogEventPageVo> queryExport(ExcelDownloadDto<GwSmogEventQuery> downloadDto){
        GwSmogEventQuery pageQuery = downloadDto.getQuery();
        IPage<GwSmogEventPageVo> data = this.queryForPage(pageQuery);
        return data;
    }
}
