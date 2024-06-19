package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.lease.entity.TbContractBindInfoEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionMoneyEntity;
import cn.cuiot.dmp.lease.mapper.TbContractBindInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 意向合同关联信息 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractBindInfoService extends BaseMybatisServiceImpl<TbContractBindInfoMapper, TbContractBindInfoEntity> {
    //1 意向合同 房屋 2.意向合同 意向金 3.租赁合同
    public static final Long CONTRACT_INTENTION_TYPE_HOUSE = 1L;
    public static final Long CONTRACT_INTENTION_TYPE_MONEY = 2L;
    public static final Long CONTRACT_LEASE_TYPE_HOUSE = 3L;
    @Autowired
    ArchiveFeignService archiveFeignService;
    @Autowired
    TbContractIntentionMoneyService moneyService;


    /**
     * 根据合同id删除关联关系
     */
    public void removeByContractId(Long id) {
        LambdaQueryWrapper<TbContractBindInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractBindInfoEntity::getIntentionId, id);
        remove(queryWrapper);
    }

    /**
     * 根据合同id查询 房屋绑定信息
     *
     * @param id
     * @return
     */
    public List<HousesArchivesVo> queryBindHouseInfoByContractId(Long id) {
        LambdaQueryWrapper<TbContractBindInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractBindInfoEntity::getIntentionId, id);
        List<TbContractBindInfoEntity> bindList = list(queryWrapper);
        if (CollectionUtils.isEmpty(bindList)) {
            return null;
        }
        List<Long> houseIds = bindList.stream().filter(t -> Objects.equals(t.getType(), CONTRACT_INTENTION_TYPE_HOUSE)).map(TbContractBindInfoEntity::getBindId).collect(Collectors.toList());
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
     * 创建合同关联的房屋,意向金
     * @param entity
     */
    public void createContractIntentionBind(TbContractIntentionEntity entity) {
        Long id = entity.getId();
        ArrayList<TbContractBindInfoEntity> saveBindList = Lists.newArrayList();
        TbContractBindInfoEntity bindInfoEntity = new TbContractBindInfoEntity();
        bindInfoEntity.setIntentionId(entity.getId());
        List<HousesArchivesVo> houseList = entity.getHouseList();
        List<TbContractIntentionMoneyEntity> moneyList = entity.getMoneyList();
        if (CollectionUtils.isNotEmpty(houseList)) {
            List<Long> houseIds = houseList.stream().map(HousesArchivesVo::getId).collect(Collectors.toList());
            houseIds.forEach(h -> {
                bindInfoEntity.setBindId(h);
                bindInfoEntity.setType(CONTRACT_INTENTION_TYPE_HOUSE);
                saveBindList.add(bindInfoEntity);
            });
        }
        if (CollectionUtils.isNotEmpty(moneyList)) {
            moneyList.forEach(m -> {
                bindInfoEntity.setBindId(m.getId());
                bindInfoEntity.setType(CONTRACT_INTENTION_TYPE_MONEY);
                saveBindList.add(bindInfoEntity);
            });
        }
        //记录意向金
        if (CollectionUtils.isNotEmpty(moneyList)) {
            moneyService.saveBatch(moneyList);
            //每次操作意向金都清除没有关联的意向金信息
            moneyService.clearMoney();
        }
        removeByContractId(id);

        if (CollectionUtils.isNotEmpty(saveBindList)) {
            saveBatch(saveBindList);
        }
    }
}
