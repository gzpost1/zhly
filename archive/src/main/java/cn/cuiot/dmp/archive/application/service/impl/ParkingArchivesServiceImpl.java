package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.service.ParkingArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ParkingArchivesMapper;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 车位档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service
public class ParkingArchivesServiceImpl extends ServiceImpl<ParkingArchivesMapper, ParkingArchivesEntity> implements ParkingArchivesService {

    /**
     * 参数校验
     */
    @Override
    public void checkParams(ParkingArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getCode())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"车位编号不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属楼盘不可为空");
        }
        if (Objects.isNull(entity.getArea())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属区域不可为空");
        }
        if (Objects.isNull(entity.getUsageStatus())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"使用情况不可为空");
        }
        if (Objects.isNull(entity.getStatus())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"状态不可为空");
        }
        if (Objects.isNull(entity.getParkingType())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"车位类型不可为空");
        }
    }
}
