package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.AuditParam;
import cn.cuiot.dmp.lease.dto.contract.TbContractIntentionParam;
import cn.cuiot.dmp.lease.entity.BaseContractEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.mapper.TbContractIntentionMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static cn.cuiot.dmp.common.constant.AuditConstant.*;
import static cn.cuiot.dmp.common.constant.AuditConstant.AUDIT_CONFIG_INTENTION_USELESS;
import static cn.cuiot.dmp.lease.service.TbContractBindInfoService.BIND_CONTRACT_INTENTION_TYPE_HOUSE;

/**
 * 意向合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractIntentionService extends BaseMybatisServiceImpl<TbContractIntentionMapper, TbContractIntentionEntity> {

    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    SystemApiFeignService systemApiFeignService;
    @Autowired
    BaseContractService baseContractService;

    @Override
    public List<TbContractIntentionEntity> list(TbContractIntentionEntity params) {
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        List<TbContractIntentionEntity> list = super.list(params);
        list.forEach(c -> {
            baseContractService.fullInfo(c);
        });
        return list;
    }

    @Override
    public PageResult<TbContractIntentionEntity> page(PageQuery param) {
        TbContractIntentionParam params = (TbContractIntentionParam) param;
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        PageResult<TbContractIntentionEntity> page = super.page(param);
        page.getRecords().forEach(c -> {
            baseContractService.fullInfo(c);
        });
        return page;
    }

    @Override
    public TbContractIntentionEntity getById(Serializable id) {
        AssertUtil.notNull(id,"id不能为空");
        TbContractIntentionEntity intentionEntity = super.getById(id);
        baseContractService.fullBindHouseInfo(intentionEntity);
        return intentionEntity;
    }

    public TbContractIntentionEntity getByContractNo(String contractNo) {
        LambdaQueryWrapper<TbContractIntentionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractIntentionEntity::getContractNo, contractNo);
        queryWrapper.last("limit 1");
        TbContractIntentionEntity intentionEntity = baseMapper.selectOne(queryWrapper);
        baseContractService.fullBindHouseInfo(intentionEntity);
        return intentionEntity;
    }



    @Override
    public boolean save(Object o) {
        TbContractIntentionEntity entity = (TbContractIntentionEntity) o;
        if(Objects.isNull(entity.getContractNo())) {
            Long code = SnowflakeIdWorkerUtil.nextId();
            entity.setContractNo(String.valueOf(code));
        }
        super.save(entity);
        bindInfoService.createContractBind(entity);
        return true;
    }


    /**
     * 删除合同同时删除绑定信息
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        bindInfoService.removeByContractId(Long.valueOf(String.valueOf(id)));
        return super.removeById(id);
    }

    @Override
    public boolean updateById(Object entity) {
        TbContractIntentionEntity o = (TbContractIntentionEntity) entity;
        bindInfoService.createContractBind(o);
        return super.updateById(o);
    }
}
