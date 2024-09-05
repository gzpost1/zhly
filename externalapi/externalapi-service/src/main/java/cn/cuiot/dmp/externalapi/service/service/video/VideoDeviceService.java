package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public IPage<VideoDeviceEntity> queryEnableDevicePage(Page<VideoDeviceEntity> page, Integer state) {
        LambdaQueryWrapper<VideoDeviceEntity> wrapper = new LambdaQueryWrapper<>();
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
        List<String> deviceIds = data.stream()
                .map(VsuapDeviceListResp::getDeviceId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        // 根据设备列表查询数据
        List<VideoDeviceEntity> dbList = list(new LambdaQueryWrapper<VideoDeviceEntity>()
                .eq(VideoDeviceEntity::getCompanyId, platfromInfoRespDTO.getCompanyId())
                .in(VideoDeviceEntity::getDeviceId, deviceIds));
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

        LambdaQueryWrapper<VideoDeviceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoDeviceEntity::getCompanyId, companyId);
        wrapper.eq(Objects.nonNull(query.getBuildingId()), VideoDeviceEntity::getBuildingId, query.getBuildingId());
        wrapper.eq(Objects.nonNull(query.getState()), VideoDeviceEntity::getState, query.getState());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), VideoDeviceEntity::getDeviceName, query.getDeviceName());
        wrapper.orderByDesc(VideoDeviceEntity::getCreateTime);
        //分页查询设备信息
        IPage<VideoPageVo> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            VideoPageVo vo = new VideoPageVo();
            BeanUtils.copyProperties(vo, item);
            return vo;
        });

        List<VideoPageVo> records;
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(records = iPage.getRecords())) {

            //查询流数据
            List<String> deviceIds = records.stream().map(VideoPageVo::getDeviceId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
            List<VideoPlayEntity> playEntityList = videoPlayService.queryFlvByDeviceIds(deviceIds, companyId);
            Map<String, VideoPlayEntity> playMap = playEntityList.stream().collect(Collectors.toMap(VideoPlayEntity::getDeviceId, e -> e, (key1, key2) -> key1));

            //查询楼盘信息
            List<Long> buildings = records.stream().map(VideoPageVo::getBuildingId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
            List<HousesArchivesVo> housesArchivesVos = queryBuildingInfo(buildings);
            Map<Long, HousesArchivesVo> buildingMap = housesArchivesVos.stream().collect(Collectors.toMap(HousesArchivesVo::getId, e -> e));

            //设置数据
            for (VideoPageVo item : records) {
                item.setDeviceTypeName(VsuapDeviceTypeEnum.queryDescByCode(item.getDeviceType()));
                item.setStateName(VsuapDeviceStateEnum.queryDescByCode(item.getState()));
                item.setFlv(playMap.containsKey(item.getDeviceId()) ? playMap.get(item.getDeviceId()).getFlv() : null);
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

        List<VideoDeviceEntity> deviceEntities = list(new LambdaQueryWrapper<VideoDeviceEntity>()
                .eq(VideoDeviceEntity::getId, dto.getId())
                .eq(VideoDeviceEntity::getCompanyId, companyId));
        if (CollectionUtils.isEmpty(deviceEntities)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        VideoDeviceEntity videoDeviceEntity = deviceEntities.get(0);
        videoDeviceEntity.setDeviceName(dto.getDeviceName());
        videoDeviceEntity.setBuildingId(dto.getBuildingId());

        updateById(videoDeviceEntity);
    }

    /**
     * 监控设备批量设置楼盘
     *
     * @Param query 参数
     */
    public void batchSetBuildingId(VideoBatchSetBuildingIdQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<VideoDeviceEntity> deviceEntities = list(new LambdaQueryWrapper<VideoDeviceEntity>()
                .eq(VideoDeviceEntity::getCompanyId, companyId)
                .in(VideoDeviceEntity::getId, query.getId()));
        if (CollectionUtils.isEmpty(deviceEntities)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }
        deviceEntities.forEach(item -> item.setBuildingId(query.getBuildingId()));

        updateBatchById(deviceEntities);
    }

    /**
     * 查询楼盘信息
     */
    private List<HousesArchivesVo> queryBuildingInfo(List<Long> buildingIds) {
        IdsReq idsReq = new IdsReq();
        idsReq.setIds(buildingIds);
        return systemApiService.queryHousesList(idsReq);
    }
}
