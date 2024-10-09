package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupEntity;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardAccessRecordQuery;
import cn.cuiot.dmp.externalapi.service.service.PersonGroupService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardAccessRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardAccessRecordEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwEntranceGuardAccessRecordMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 格物门禁-通过记录 业务层
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@Service
public class GwEntranceGuardAccessRecordService extends ServiceImpl<GwEntranceGuardAccessRecordMapper, GwEntranceGuardAccessRecordEntity> {

    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private SystemApiService systemApiService;
    @Autowired
    private PersonGroupService personGroupService;

    /**
     * 分页
     */
    public IPage<GwEntranceGuardAccessRecordVO> queryForPage(GwEntranceGuardAccessRecordQuery query) {
        // 企业id
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        IPage<GwEntranceGuardAccessRecordVO> iPage = baseMapper.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()), query);

        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(iPage.getRecords())) {
            buildPageVo(iPage.getRecords());
        }

        return iPage;
    }

    private void buildPageVo(List<GwEntranceGuardAccessRecordVO> list) {
        // 获取各类信息的 Map
        Map<Long, String> buildingArchiveMap = getDataMap(list, GwEntranceGuardAccessRecordVO::getBuildingId, this::queryBuildingInfo, BuildingArchive::getId, BuildingArchive::getName);
        Map<Long, String> deptMap = getDataMap(list, GwEntranceGuardAccessRecordVO::getDeptId, this::queryDeptList, DepartmentDto::getId, DepartmentDto::getPathName);
        Map<Long, String> personGroupMap = getDataMap(list, GwEntranceGuardAccessRecordVO::getPersonGroupId, this::queryPersonGroupList, PersonGroupEntity::getId, PersonGroupEntity::getName);

        // 设置 Vo 对象的值
        for (GwEntranceGuardAccessRecordVO vo : list) {
            vo.setBuildingName(buildingArchiveMap.getOrDefault(vo.getBuildingId(), null));
            vo.setDeptPathName(deptMap.getOrDefault(vo.getDeptId(), null));
            vo.setPersonGroupName(personGroupMap.getOrDefault(vo.getPersonGroupId(), null));
        }
    }

    /**
     * 通用方法：根据 VO 属性获取数据 Map
     */
    private <T> Map<Long, String> getDataMap(List<GwEntranceGuardAccessRecordVO> list,
                                             Function<GwEntranceGuardAccessRecordVO, Long> idGetter,
                                             Function<List<Long>, List<T>> queryFunction,
                                             Function<T, Long> keyMapper,
                                             Function<T, String> valueMapper) {
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
     * 查询分组信息
     */
    public List<PersonGroupEntity> queryPersonGroupList(List<Long> personGroupIds) {
        return personGroupService.listByIds(personGroupIds);
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
