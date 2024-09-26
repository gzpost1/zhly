package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.infrastructure.constants.PlatfromInfoRedisKeyConstants;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.entity.park.PlatfromInfoEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.PlatfromInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author pengjian
 * @since 2024-07-18
 */
@Service
public class PlatfromInfoService extends ServiceImpl<PlatfromInfoMapper, PlatfromInfoEntity> {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 过期时间，单位（秒） 60分钟
     */
    public static final int EXPIRED_TIME = 60 * 60;

    public Page<PlatfromInfoRespDTO> queryForPage(PlatfromInfoReqDTO dto) {
        return baseMapper.queryForPage(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
    }

    public List<PlatfromInfoRespDTO> queryForList(PlatfromInfoReqDTO dto) {
        return baseMapper.queryForList(dto);
    }

    /**
     * 从缓存获取短信内容（时效一个小时）
     *
     * @return PlatfromInfoRespDTO
     * @Param dto 参数
     */
    public PlatfromInfoRespDTO queryPlatfromSmsRedis(PlatfromInfoReqDTO dto) {
        if (Objects.isNull(dto.getCompanyId())) {
            throw new BusinessException(ResultCode.ERROR, "企业id不能为空");
        }

        String jsonStr = redisUtil.get(PlatfromInfoRedisKeyConstants.PLATFROM_INFO_SMS + dto.getCompanyId());
        if (StringUtils.isNotBlank(jsonStr)) {
            return JsonUtil.readValue(jsonStr, PlatfromInfoRespDTO.class);
        }

        List<PlatfromInfoEntity> smsList = list(new LambdaQueryWrapper<PlatfromInfoEntity>()
                .eq(PlatfromInfoEntity::getCompanyId, dto.getCompanyId())
                .eq(PlatfromInfoEntity::getPlatformId, FootPlateInfoEnum.SMS_WOCLOUD.getId() + ""));

        if (CollectionUtils.isNotEmpty(smsList)) {
            PlatfromInfoEntity platfromInfoEntity = smsList.get(0);
            PlatfromInfoRespDTO respDTO = new PlatfromInfoRespDTO();
            BeanUtils.copyProperties(platfromInfoEntity, respDTO);

            // 设置缓存数据
            redisUtil.set(PlatfromInfoRedisKeyConstants.PLATFROM_INFO_SMS + dto.getCompanyId(), JsonUtil.writeValueAsString(respDTO), EXPIRED_TIME);

            return respDTO;
        }
        return null;
    }
}
