package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoDeviceEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoPlayEntity;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoDeviceMapper;
import cn.cuiot.dmp.externalapi.service.query.video.VideoBatchSetBuildingIdQuery;
import cn.cuiot.dmp.externalapi.service.query.video.VideoPageQuery;
import cn.cuiot.dmp.externalapi.service.query.video.VideoUpdateDTO;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapDeviceListResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.enums.VsuapDeviceStateEnum;
import cn.cuiot.dmp.externalapi.service.vendor.video.enums.VsuapDeviceTypeEnum;
import cn.cuiot.dmp.externalapi.service.vo.video.VideoPageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 监控-设备信息 业务层
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@Service
public class VideoDeviceService extends ServiceImpl<VideoDeviceMapper, VideoDeviceEntity> {

    @Autowired
    private VideoPlayService videoPlayService;
    @Autowired
    private SystemApiService systemApiService;
    @Autowired
    private ApiArchiveService apiArchiveService;

    /**
     * 停用设备（不填deviceId修改全部设备）
     *
     * @Param deviceId 第三方设备id
     * @Param companyId 企业id
     */
    public void disableDevice(String deviceId, Long companyId) {
        LambdaUpdateWrapper<VideoDeviceEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(VideoDeviceEntity::getStatus, EntityConstants.DISABLED);
        wrapper.eq(StringUtils.isNotBlank(deviceId), VideoDeviceEntity::getDeviceId, deviceId);
        wrapper.eq(VideoDeviceEntity::getCompanyId, companyId);

        update(wrapper);
    }

    /**
     * 查询在线的设备
     *
     * @return List<VideoDeviceEntity> 列表
     */
    public IPage<VideoDeviceEntity> queryEnableDevicePage(Page<VideoDeviceEntity> page, Integer state, Long companyId) {
        LambdaQueryWrapper<VideoDeviceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(companyId), VideoDeviceEntity::getCompanyId, companyId);
        wrapper.eq(VideoDeviceEntity::getStatus, EntityConstants.ENABLED);
        wrapper.eq(VideoDeviceEntity::getState, state);

        return page(page, wrapper);
    }

    /**
     * 同步设备数据
     *
     * @Param data 参数
     */
    public void syncDevices(List<VsuapDeviceListResp> data, PlatfromInfoRespDTO platfromInfoRespDTO) {
        // 获取设备id列表
        List<String> deviceIds = data.stream().map(VsuapDeviceListResp::getDeviceId).filter(StringUtils::isNotBlank).collect(Collectors.toList());

        // 根据设备列表查询数据
        List<VideoDeviceEntity> dbList = list(new LambdaQueryWrapper<VideoDeviceEntity>().eq(VideoDeviceEntity::getCompanyId, platfromInfoRespDTO.getCompanyId()).in(VideoDeviceEntity::getDeviceId, deviceIds));
        Map<String, VideoDeviceEntity> dbMap = dbList.stream().collect(Collectors.toMap(VideoDeviceEntity::getDeviceId, e -> e));

        // 构建VideoDeviceEntity
        List<VideoDeviceEntity> collect = data.stream().map(item -> {
            VideoDeviceEntity aDefault = dbMap.getOrDefault(item.getDeviceId(), new VideoDeviceEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setDeviceName(aDefault.getDeviceName());
            aDefault.setStatus(EntityConstants.ENABLED);
            aDefault.setCompanyId(platfromInfoRespDTO.getCompanyId());
            aDefault.setSecret(Sm4.encryption(platfromInfoRespDTO.getData()));
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }

    /**
     * 后台分页查询
     *
     * @return IPage
     * @Param
     */
    public IPage<VideoPageVo> queryForPage(VideoPageQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<Long> buildingIds = Lists.newArrayList();
        if (Objects.isNull(query.getBuildingId())) {
            //获取当前账号自己的组织及其下属组织的楼盘id
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = apiArchiveService.lookupBuildingArchiveByDepartmentList(dto);
            if (CollectionUtils.isNotEmpty(archives)) {
                List<Long> collect = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
                buildingIds.addAll(collect);
            }
        }else {
            buildingIds.add(query.getBuildingId());
        }

        //构建查询条件
        LambdaQueryWrapper<VideoDeviceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoDeviceEntity::getCompanyId, companyId);
        wrapper.eq(Objects.nonNull(query.getState()), VideoDeviceEntity::getState, query.getState());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), VideoDeviceEntity::getDeviceName, query.getDeviceName());
        //查询所属组织下的楼盘以及未设置楼盘的数据
        wrapper.and(qwp -> qwp.in(CollectionUtils.isNotEmpty(buildingIds), VideoDeviceEntity::getBuildingId, buildingIds)
                .or()
                .isNull(VideoDeviceEntity::getBuildingId));
        //排序
        wrapper.orderByDesc(VideoDeviceEntity::getCreateTime);

        //分页查询设备信息
        IPage<VideoPageVo> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            VideoPageVo vo = new VideoPageVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        List<VideoPageVo> records;
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(records = iPage.getRecords())) {

            //查询流数据
            List<String> deviceIds = records.stream().map(VideoPageVo::getDeviceId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<VideoPlayEntity> playEntityList = videoPlayService.queryFlvByDeviceIds(deviceIds, companyId);
            Map<String, VideoPlayEntity> playMap = playEntityList.stream().collect(Collectors.toMap(VideoPlayEntity::getDeviceId, e -> e, (key1, key2) -> key1));

            //查询楼盘信息
            List<Long> buildings = records.stream().map(VideoPageVo::getBuildingId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<BuildingArchive> buildingArchives = queryBuildingInfo(buildings);
            Map<Long, BuildingArchive> buildingMap = buildingArchives.stream().collect(Collectors.toMap(BuildingArchive::getId, e -> e));

            //设置数据
            for (VideoPageVo item : records) {
                item.setDeviceTypeName(VsuapDeviceTypeEnum.queryDescByCode(item.getDeviceType()));
                item.setStateName(VsuapDeviceStateEnum.queryDescByCode(item.getState()));
                item.setFlv(playMap.containsKey(item.getDeviceId()) ? playMap.get(item.getDeviceId()).getFlv() : null);
                item.setHls(playMap.containsKey(item.getDeviceId()) ? playMap.get(item.getDeviceId()).getHls() : null);
                item.setBuildingName(buildingMap.containsKey(item.getBuildingId()) ? buildingMap.get(item.getBuildingId()).getName() : null);
            }
        }
        return iPage;
    }

    /**
     * 更新
     *
     * @Param dto 参数
     */
    public void update(VideoUpdateDTO dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<VideoDeviceEntity> deviceEntities = list(new LambdaQueryWrapper<VideoDeviceEntity>().eq(VideoDeviceEntity::getId, dto.getId()).eq(VideoDeviceEntity::getCompanyId, companyId));
        if (CollectionUtils.isEmpty(deviceEntities)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        VideoDeviceEntity videoDeviceEntity = deviceEntities.get(0);
        videoDeviceEntity.setDeviceName(dto.getDeviceName());
        videoDeviceEntity.setBuildingId(dto.getBuildingId());

        updateById(videoDeviceEntity);
    }

    /**
     * 删除设备
     */
    public void delete(List<Long> ids) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<VideoDeviceEntity> list = list(new LambdaQueryWrapper<VideoDeviceEntity>().eq(VideoDeviceEntity::getCompanyId, companyId).in(VideoDeviceEntity::getId, ids));
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        //判断设备id列表是否都属于该企业的设备
        List<VideoDeviceEntity> collect = list.stream().filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            List<String> deviceNames = collect.stream().map(VideoDeviceEntity::getDeviceName).collect(Collectors.toList());
            throw new BusinessException(ResultCode.ERROR, "监控【" + String.join(",", deviceNames) + "】不属于该企业");
        }

        removeByIds(ids);
    }

    /**
     * 监控设备批量设置楼盘
     *
     * @Param query 参数
     */
    public void batchSetBuildingId(VideoBatchSetBuildingIdQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<VideoDeviceEntity> deviceEntities = list(new LambdaQueryWrapper<VideoDeviceEntity>().eq(VideoDeviceEntity::getCompanyId, companyId).in(VideoDeviceEntity::getId, query.getId()));
        if (CollectionUtils.isEmpty(deviceEntities)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }
        deviceEntities.forEach(item -> item.setBuildingId(query.getBuildingId()));

        updateBatchById(deviceEntities);
    }

    /**
     * 查询楼盘信息
     */
    private List<BuildingArchive> queryBuildingInfo(List<Long> buildingIds) {
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(buildingIds);
        return systemApiService.buildingArchiveQueryForList(req);
    }
}
