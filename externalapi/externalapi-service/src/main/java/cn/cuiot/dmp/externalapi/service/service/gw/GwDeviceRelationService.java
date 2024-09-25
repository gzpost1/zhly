package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwDeviceRelationMapper;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwDeviceRelationEntity;

import java.util.Objects;

/**
 * 格物设备数据关联表 业务层
 *
 * @Author: zc
 * @Date: 2024-09-13
 */
@Service
public class GwDeviceRelationService extends ServiceImpl<GwDeviceRelationMapper, GwDeviceRelationEntity> {

    /**
     * 保存
     *
     * @Param entity 参数
     */
    public void create(GwDeviceRelationEntity entity) {
        checkData(entity);
        if (StringUtils.isBlank(entity.getBusinessType())) {
            throw new BusinessException(ResultCode.ERROR, "格物设备业务类型数据不能为空");
        }
        if (Objects.isNull(entity.getDataId())) {
            throw new BusinessException(ResultCode.ERROR, "格物设备业务类型数据不能为空");
        }
        GwDeviceRelationEntity gwDeviceRelation = baseMapper.getGwDeviceRelation(entity);

        if (Objects.isNull(gwDeviceRelation)) {
            save(entity);
        }
    }

    /**
     * 删除
     */
    public void delete(GwDeviceRelationEntity entity) {
        checkData(entity);
        remove(new LambdaQueryWrapper<GwDeviceRelationEntity>()
                .eq(GwDeviceRelationEntity::getProductKey, entity.getProductKey())
                .eq(GwDeviceRelationEntity::getDeviceKey, entity.getDeviceKey()));
    }

    /**
     * 详情
     */
    public GwDeviceRelationEntity gwDeviceRelation(GwDeviceRelationEntity entity) {
        checkData(entity);
        return baseMapper.getGwDeviceRelation(entity);
    }

    private void checkData(GwDeviceRelationEntity entity) {
        if (Objects.isNull(entity)) {
            throw new BusinessException(ResultCode.ERROR, "格物设备数据不能为空");
        }
        if (StringUtils.isBlank(entity.getProductKey())) {
            throw new BusinessException(ResultCode.ERROR, "格物设备产品key数据不能为空");
        }
        if (StringUtils.isBlank(entity.getDeviceKey())) {
            throw new BusinessException(ResultCode.ERROR, "格物设备id数据不能为空");
        }
    }
}
