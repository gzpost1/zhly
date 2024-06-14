package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.mapper.TbContractIntentionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 意向合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractIntentionService extends BaseMybatisServiceImpl<TbContractIntentionMapper, TbContractIntentionEntity> {

    @Autowired
    TbContractIntentionBindInfoService bindInfoService;

    @Override
    public List<TbContractIntentionEntity> list(TbContractIntentionEntity params) {
        List<TbContractIntentionEntity> list = super.list(params);
        list.forEach(c -> {
            fullBindHouseInfo(c);
        });
        return list;
    }

    @Override
    public PageResult<TbContractIntentionEntity> page(PageQuery param) {
        PageResult<TbContractIntentionEntity> page = super.page(param);
        page.getRecords().forEach(c -> {
            fullBindHouseInfo(c);
        });
        return page;
    }

    @Override
    public TbContractIntentionEntity getById(Serializable id) {
        TbContractIntentionEntity intentionEntity = super.getById(id);
        fullBindHouseInfo(intentionEntity);
        return intentionEntity;
    }

    private void fullBindHouseInfo(TbContractIntentionEntity intentionEntity) {
        List<HousesArchivesVo> housesArchivesVos = bindInfoService.queryBindHouseInfoByContractId(intentionEntity.getId());
        if (Objects.nonNull(housesArchivesVos)) {
            intentionEntity.setHouseList(housesArchivesVos);
        }
    }
}
