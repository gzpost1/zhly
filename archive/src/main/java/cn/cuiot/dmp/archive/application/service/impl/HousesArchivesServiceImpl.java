package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.HousesArchivesMapper;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DoubleValidator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 房屋档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service
public class HousesArchivesServiceImpl extends ServiceImpl<HousesArchivesMapper, HousesArchivesEntity> implements HousesArchivesService {

    /**
     * 参数校验
     */
    @Override
    public void checkParams(HousesArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房屋名称不可为空");
        }
        if (StringUtils.isBlank(entity.getRoomNum())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房号不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属楼盘不可为空");
        }
        if (StringUtils.isBlank(entity.getCode())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房屋编码不可为空");
        }

        // 规则判断
        if (entity.getName().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房屋名称长度不可超过30");
        }
        if (entity.getRoomNum().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房号长度不可超过30");
        }
        if (entity.getCode().length() > 15){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房屋编码长度不可超过30");
        }
        if (StringUtils.isNotBlank(entity.getFloorAlias()) && entity.getFloorAlias().length() > 4){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"楼层别名长度不可超过4");
        }
        if (StringUtils.isNotBlank(entity.getFloorName()) &&entity.getFloorName().length() > 7){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"楼层别名长度不可超过7");
        }
        if (StringUtils.isNotBlank(entity.getCapacity()) &&entity.getCapacity().length() > 9){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"容量长度不可超过9");
        }
        if (StringUtils.isNotBlank(entity.getQibie()) && entity.getQibie().length() > 11){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"期别长度不可超过11");
        }
        if (Objects.nonNull(entity.getFloorCoefficient()) && entity.getFloorCoefficient() < 0.0 || entity.getFloorCoefficient() > 9999.99){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"楼层系数区间0-9999.99");
        }
        if (Objects.nonNull(entity.getBuildingArea()) && !DoubleValidator.validateDouble(entity.getBuildingArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"建筑面积支持4位小数，最长可输入15位");
        }
        if (Objects.nonNull(entity.getUsableArea()) && !DoubleValidator.validateDouble(entity.getUsableArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"使用面积支持4位小数，最长可输入15位");
        }
        if (Objects.nonNull(entity.getChargeArea()) && !DoubleValidator.validateDouble(entity.getChargeArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"收费面积支持4位小数，最长可输入15位");
        }
        if (Objects.nonNull(entity.getSharedArea()) && !DoubleValidator.validateDouble(entity.getSharedArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"公摊面积支持4位小数，最长可输入15位");
        }
        if (StringUtils.isNotBlank(entity.getOwnershipUnit()) && entity.getOwnershipUnit().length() > 50){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"产权单位长度不可超过50");
        }
    }
}
