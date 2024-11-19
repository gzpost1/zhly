package cn.cuiot.dmp.externalapi.service.service.gw.gasalarm;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmFaultRecordEntity;
import cn.cuiot.dmp.externalapi.service.enums.GwGasAlarmPropertyEnums;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.gw.gasalarm.GwGasAlarmFaultRecordMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmFaultRecordQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm.GwGasAlarmFaultRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 格物水浸报警器故障记录 业务层
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Service
public class GwGasAlarmFaultRecordService extends ServiceImpl<GwGasAlarmFaultRecordMapper, GwGasAlarmFaultRecordEntity> {

    @Autowired
    private ApiArchiveService apiArchiveService;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private SystemApiService systemApiService;

    public IPage<GwGasAlarmFaultRecordVO> queryRecordForPage(GwGasAlarmFaultRecordQuery query) {

        // 设置企业id
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        if (CollectionUtils.isNotEmpty(query.getBuildingIds())) {
            query.setBuildingIds(query.getBuildingIds());
        } else {
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(Objects.nonNull(query.getDeptId()) ? query.getDeptId() : LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = apiArchiveService.lookupBuildingArchiveByDepartmentList(dto);
            if (CollectionUtils.isNotEmpty(archives)) {
                List<Long> collect = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
                query.setBuildingIds(collect);
            }
        }

        IPage<GwGasAlarmFaultRecordVO> iPage = baseMapper.queryRecordForPage(new Page(query.getPageNo(), query.getPageSize()), query);
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(item ->{
                item.setErrorCodeName(GwGasAlarmPropertyEnums.ErrorCode.getNameByCode(item.getErrorCode()));
            });
            buildPageVo(iPage.getRecords());
        }
        return iPage;
    }

    private void buildPageVo(List<GwGasAlarmFaultRecordVO> list) {
        // 获取各类信息的 Map
        Map<Long, Object> buildingArchiveMap = getDataMap(list, GwGasAlarmFaultRecordVO::getBuildingId, this::queryBuildingInfo, BuildingArchive::getId, BuildingArchive::getName);
        Map<Long, Object> deptMap = getDataMap(list, GwGasAlarmFaultRecordVO::getDeptId, this::queryDeptList, DepartmentDto::getId, DepartmentDto::getPathName);

        // 设置 Vo 对象的值
        for (GwGasAlarmFaultRecordVO vo : list) {
            vo.setBuildingName(buildingArchiveMap.getOrDefault(vo.getBuildingId(), null) + "");
            vo.setDeptPathName(deptMap.getOrDefault(vo.getDeptId(), null) + "");
        }
    }

    /**
     * 通用方法：根据 VO 属性获取数据 Map
     */
    private <T> Map<Long, Object> getDataMap(List<GwGasAlarmFaultRecordVO> list,
                                             Function<GwGasAlarmFaultRecordVO, Long> idGetter,
                                             Function<List<Long>, List<T>> queryFunction,
                                             Function<T, Long> keyMapper,
                                             Function<T, Object> valueMapper) {
        List<Long> ids = list.stream().map(idGetter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return Maps.newHashMap();
        }
        return queryFunction.apply(ids).stream().collect(Collectors.toMap(keyMapper, valueMapper));
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
}
