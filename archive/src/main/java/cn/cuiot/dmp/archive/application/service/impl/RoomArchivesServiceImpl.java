package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.service.RoomArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.RoomArchivesMapper;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DoubleValidator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 空间档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service
public class RoomArchivesServiceImpl extends ServiceImpl<RoomArchivesMapper, RoomArchivesEntity> implements RoomArchivesService {

    /**
     * 参数校验
     */
    @Override
    public void checkParams(RoomArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"空间名称不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属楼盘不可为空");
        }
        if (Objects.isNull(entity.getSpaceCategory())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"空间分类不可为空");
        }
        if (Objects.isNull(entity.getProfessionalPurpose())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"专业用途不可为空");
        }
        if (StringUtils.isBlank(entity.getLocationDeviation())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"定位偏差不可为空");
        }
        if (Objects.isNull(entity.getStatus())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"状态不可为空");
        }
    }
}
