package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoAIMethodEntity;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoAIMethodMapper;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapAIMethodListResp;
import cn.cuiot.dmp.util.Sm4;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 监控AI算法 业务层
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
@Service
public class VideoAIMethodService extends ServiceImpl<VideoAIMethodMapper, VideoAIMethodEntity> {

    /**
     * 停用设备（不填deviceId修改全部设备）
     *
     * @Param deviceId 第三方设备id
     */
    public void disableAIMethod(String methodId, Long companyId) {
        LambdaUpdateWrapper<VideoAIMethodEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(VideoAIMethodEntity::getStatus, EntityConstants.DISABLED);
        wrapper.eq(StringUtils.isNotBlank(methodId), VideoAIMethodEntity::getMethodId, methodId);
        wrapper.eq(VideoAIMethodEntity::getCompanyId, companyId);

        update(wrapper);
    }

    /**
     * 查询AI算法列表
     *
     * @Param type 算法类型（1：统计类 2：告警类 ）
     * @return List<VideoAIMethodEntity> 列表
     */
    public IPage<VideoAIMethodEntity> queryEnableAIMethodPage(Page<VideoAIMethodEntity> page, Integer type) {
        LambdaQueryWrapper<VideoAIMethodEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoAIMethodEntity::getStatus, EntityConstants.ENABLED);
        wrapper.eq(Objects.nonNull(type), VideoAIMethodEntity::getType, type);

        return page(page, wrapper);
    }

    /**
     * 同步AI算法数据
     *
     * @Param data 参数
     */
    public void syncAIMethods(List<VsuapAIMethodListResp> data, PlatfromInfoRespDTO platfromInfoRespDTO) {
        // 获取AI算法id列表
        List<String> methodIds = data.stream()
                .map(VsuapAIMethodListResp::getMethodId)
                .collect(Collectors.toList());

        // 根据算法id列表查询数据
        List<VideoAIMethodEntity> dbList = list(new LambdaQueryWrapper<VideoAIMethodEntity>()
                .eq(VideoAIMethodEntity::getCompanyId, platfromInfoRespDTO.getCompanyId())
                .in(VideoAIMethodEntity::getMethodId, methodIds));
        Map<String, VideoAIMethodEntity> dbMap = dbList.stream().collect(Collectors.toMap(VideoAIMethodEntity::getMethodId, e -> e));

        // 构建VideoDeviceEntity
        List<VideoAIMethodEntity> collect = data.stream().map(item -> {
            VideoAIMethodEntity aDefault = dbMap.getOrDefault(item.getMethodId(), new VideoAIMethodEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setStatus(EntityConstants.ENABLED);
            aDefault.setSecret(Sm4.encryption(platfromInfoRespDTO.getData()));
            aDefault.setCompanyId(platfromInfoRespDTO.getCompanyId());
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }
}
