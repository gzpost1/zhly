package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatus;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatusVo;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.entity.*;
import cn.cuiot.dmp.lease.mapper.TbContractBindInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_INTENTION_TYPE;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_LEASE_TYPE;

/**
 * 意向合同关联信息 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractBindInfoService extends BaseMybatisServiceImpl<TbContractBindInfoMapper, TbContractBindInfoEntity> {

    @Autowired
    ArchiveFeignService archiveFeignService;
    @Autowired
    TbContractIntentionMoneyService moneyService;
    @Autowired
    TbContractChargeService chargeService;


    /**
     * 根据房屋名称模糊获取合同id集合
     *
     * @param name
     * @return
     */
    public List<Long> queryContractIdsByHouseName(String name) {
        List<Long> ids = baseMapper.queryContractIdsByHouseName(name);
        if(CollectionUtils.isEmpty(ids)){
             ids = Lists.newArrayList();
            ids.add(-999L);
        }
        return ids;
    }
    /**
     * 根据合同id删除关联关系
     */
    public void removeByContractId(Long id) {
        LambdaQueryWrapper<TbContractBindInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractBindInfoEntity::getContractId, id);
        remove(queryWrapper);
    }

    /**
     * 根据合同id查询 房屋绑定信息
     *
     * @param id
     * @return
     */
    public List<HousesArchivesVo> queryBindHouseInfoByContractId(Long id, Integer type) {
        LambdaQueryWrapper<TbContractBindInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractBindInfoEntity::getContractId, id);
        List<TbContractBindInfoEntity> bindList = list(queryWrapper);
        if (CollectionUtils.isEmpty(bindList)) {
            return null;
        }
        List<Long> houseIds = bindList.stream().filter(t -> Objects.equals(t.getType(), type)).map(TbContractBindInfoEntity::getBindId).collect(Collectors.toList());
        List<HousesArchivesVo> houseList = queryRemoteHouseList(houseIds);

        return houseList;
    }

    /**
     * 查询房屋信息
     *
     * @param houseIds
     * @return
     */
    private List<HousesArchivesVo> queryRemoteHouseList(List<Long> houseIds) {
        IdsReq houseIdsReq = new IdsReq();
        houseIdsReq.setIds(houseIds);
        IdmResDTO<List<HousesArchivesVo>> houseResult = archiveFeignService.queryHousesList(houseIdsReq);
        List<HousesArchivesVo> houseList = houseResult.getData();
        return houseList;
    }

    /**
     * 创建合同关联的房屋,意向金(只有意向合同有意向金绑定)
     *
     * @param entity
     */
    public void createContractBind(BaseContractEntity entity) {
        Long id = entity.getId();
        //先移除之前合同关联关系
        removeByContractId(id);
        ArrayList<TbContractBindInfoEntity> saveBindList = Lists.newArrayList();

        //房屋绑定
        int bindHouseType = CONTRACT_INTENTION_TYPE;
        if (entity instanceof TbContractLeaseEntity) {
            bindHouseType = CONTRACT_LEASE_TYPE;
        }
        List<HousesArchivesVo> houseList = entity.getHouseList();
        if (CollectionUtils.isNotEmpty(houseList)) {
            List<Long> houseIds = houseList.stream().map(HousesArchivesVo::getId).collect(Collectors.toList());
            int finalBindHouseType = bindHouseType;
            houseIds.forEach(h -> {
                TbContractBindInfoEntity bindInfoEntity = new TbContractBindInfoEntity();
                bindInfoEntity.setContractId(id);
                bindInfoEntity.setBindId(h);
                bindInfoEntity.setType(finalBindHouseType);
                saveBindList.add(bindInfoEntity);
            });
        }

        //记录意向金
        List<TbContractIntentionMoneyEntity> moneyList = entity.getMoneyList();
        if (CollectionUtils.isNotEmpty(moneyList)) {
            moneyService.removeByContractId(id);
            moneyList.forEach(m -> {
                Long moneyId = SnowflakeIdWorkerUtil.nextId();
                m.setId(moneyId);
                m.setContractId(id);
            });
            moneyService.saveOrUpdateBatch(moneyList);
        }

        //记录绑定关系
        if (CollectionUtils.isNotEmpty(saveBindList)) {
            saveBatch(saveBindList);
        }
        //记录租赁合同费用条款
        List<TbContractChargeEntity> chargeList = entity.getChargeList();
        if(CollectionUtils.isNotEmpty(chargeList)){
            chargeService.removeByContractId(id);
            chargeList.forEach(c->{
                c.setContractId(id);
            });
            chargeService.saveOrUpdateBatch(chargeList);
        }

    }

    public ContractStatusVo queryConctactStatusByHouseIds(List<Long> ids) {
        List<ContractStatus> list = baseMapper.queryConctactStatusByHouseIds(ids);
        ContractStatusVo contractStatusVo = new ContractStatusVo();
        if (CollectionUtils.isNotEmpty(list)) {
            //意向合同
            Map<Long, List<ContractStatus>> intentionMap = list.stream().filter(o -> Objects.equals(o.getType(), CONTRACT_INTENTION_TYPE)).collect(Collectors.groupingBy(ContractStatus::getHouseId));
            //租赁合同
            Map<Long, List<ContractStatus>> leaseMap = list.stream().filter(o -> Objects.equals(o.getType(), CONTRACT_LEASE_TYPE)).collect(Collectors.groupingBy(ContractStatus::getHouseId));
            contractStatusVo.setIntentionMap(intentionMap);
            contractStatusVo.setLeaseMap(leaseMap);
        }
        return contractStatusVo;
    }
}
