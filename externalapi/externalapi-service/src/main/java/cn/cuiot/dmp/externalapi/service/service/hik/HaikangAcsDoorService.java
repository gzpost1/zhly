package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.externalapi.service.constant.HaikangDataDictConstant;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsDoorConverter;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangDataDictEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HaikangAcsDoorMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorControlDto;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorStateQuery;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikDoorControlReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikDoorStatesReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorControlResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorStatesResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorStatesResp.AuthDoor;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikDoorControlVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 门禁点信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
@Service
public class HaikangAcsDoorService extends ServiceImpl<HaikangAcsDoorMapper, HaikangAcsDoorEntity> {

    @Autowired
    private HaikangAcsDoorMapper haikangAcsDoorMapper;

    @Autowired
    private HaikangAcsDoorConverter haikangAcsDoorConverter;

    @Autowired
    private HaikangDataDictService haikangDataDictService;

    @Autowired
    private HikApiFeignService hikApiFeignService;

    @Autowired
    private HikCommonHandle hikCommonHandle;

    /**
     * 反控成功标识
     */
    private final static Integer CONTROL_DOOR_SUCCESS = 0;

    /**
     * 分页查询
     */
    public IPage<HaikangAcsDoorVo> queryForPage(HaikangAcsDoorQuery query) {
        Page<HaikangAcsDoorVo> page = haikangAcsDoorMapper.searchList(
                new Page<HaikangAcsDoorVo>(query.getPageNo(), query.getPageSize()), query);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            patchDataDict(page.getRecords());
        }
        return page;
    }

    /**
     * 查询列表
     */
    public List<HaikangAcsDoorVo> queryForList(HaikangAcsDoorQuery query) {
        List<HaikangAcsDoorVo> list = haikangAcsDoorMapper.searchList(query);
        if (CollectionUtils.isNotEmpty(list)) {
            patchDataDict(list);
        }
        return list;
    }

    /**
     * 填充字典名称
     */
    private void patchDataDict(List<HaikangAcsDoorVo> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<HaikangDataDictEntity> deviceChannelTypeList = haikangDataDictService.getListByDictTypeCode(
                    HaikangDataDictConstant.DEVICE_CHANNEL_TYPE);
            List<HaikangDataDictEntity> resourceTypeList = haikangDataDictService.getListByDictTypeCode(
                    HaikangDataDictConstant.RESOURCE_TYPE);

            dataList.forEach(item -> {
                if (StringUtils.isNotBlank(item.getChannelType())) {
                    item.setChannelTypeName(
                            haikangDataDictService.getNameByCode(deviceChannelTypeList,
                                    item.getChannelType()));
                }
                if (StringUtils.isNotBlank(item.getResourceType())) {
                    item.setResourceTypeName(haikangDataDictService.getNameByCode(resourceTypeList,
                            item.getResourceType()));
                }
            });
        }
    }

    /**
     * 下拉列表查询
     */
    public List<HaikangAcsDoorVo> listForSelect(HaikangAcsDoorQuery query) {
        LambdaQueryWrapper<HaikangAcsDoorEntity> lambdaedQuery = Wrappers.lambdaQuery();

        if (Objects.nonNull(query.getCompanyId())) {
            lambdaedQuery.eq(HaikangAcsDoorEntity::getOrgId, query.getCompanyId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            lambdaedQuery.like(HaikangAcsDoorEntity::getName, query.getName());
        }
        if (StringUtils.isNotBlank(query.getIndexCode())) {
            lambdaedQuery.like(HaikangAcsDoorEntity::getIndexCode, query.getIndexCode());
        }
        if (StringUtils.isNotBlank(query.getDoorNo())) {
            lambdaedQuery.like(HaikangAcsDoorEntity::getDoorNo, query.getDoorNo());
        }
        if (StringUtils.isNotBlank(query.getRegionIndexCode())) {
            lambdaedQuery.eq(HaikangAcsDoorEntity::getRegionIndexCode,
                    query.getRegionIndexCode());
        }
        if (StringUtils.isNotBlank(query.getParentIndexCode())) {
            lambdaedQuery.eq(HaikangAcsDoorEntity::getParentIndexCode,
                    query.getParentIndexCode());
        }
        lambdaedQuery.orderByAsc(HaikangAcsDoorEntity::getCreateTime,
                HaikangAcsDoorEntity::getId);
        List<HaikangAcsDoorEntity> selectList = haikangAcsDoorMapper.selectList(lambdaedQuery);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return haikangAcsDoorConverter.entityListToVoList(selectList);
        }
        return Lists.newArrayList();
    }

    /**
     * 门禁点反控
     */
    public List<HikDoorControlVo> doControlDoor(HaikangAcsDoorControlDto dto) {
        AssertUtil.isTrue(dto.getIndexCodes().size()<=10,"每次批量反控门禁点不能超过10个");
        LambdaQueryWrapper<HaikangAcsDoorEntity> lambdaedQuery = Wrappers.lambdaQuery();
        lambdaedQuery.eq(HaikangAcsDoorEntity::getOrgId, dto.getCompanyId());
        lambdaedQuery.in(HaikangAcsDoorEntity::getIndexCode, dto.getIndexCodes());
        List<HaikangAcsDoorEntity> selectList = Optional.ofNullable(
                haikangAcsDoorMapper.selectList(lambdaedQuery)).orElse(
                Lists.newArrayList());
        AssertUtil.isFalse(dto.getIndexCodes().size() > selectList.size(), "操作失败，越权操作数据");

        HikDoorControlReq req = new HikDoorControlReq();
        req.setControlType(dto.getControlType());
        req.setDoorIndexCodes(dto.getIndexCodes());

        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                dto.getCompanyId());

        List<HikDoorControlResp> respList = hikApiFeignService.doorDoControl(req,
                hikEntranceGuardBO);

        AssertUtil.isTrue(CollectionUtils.isNotEmpty(respList), "操作失败");

        List<HikDoorControlVo> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(respList)) {
            resultList = respList.stream().map(item->{
                HikDoorControlVo vo = new HikDoorControlVo();
                vo.setDoorIndexCode(item.getDoorIndexCode());
                vo.setControlResultCode(item.getControlResultCode());
                vo.setControlResultDesc(item.getControlResultDesc());
                vo.setName(getDoorNameByIndexCode(selectList,item.getDoorIndexCode()));
                return vo;
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    private String getDoorNameByIndexCode(List<HaikangAcsDoorEntity> selectList, String indexCode) {
        Optional<HaikangAcsDoorEntity> findOptional = selectList.stream()
                .filter(item -> item.getIndexCode().equals(indexCode)).findFirst();
        if(findOptional.isPresent()){
            return findOptional.get().getName();
        }
        return null;
    }

    /**
     * 查询门禁点状态
     */
    public Byte queryDoorState(HaikangAcsDoorStateQuery query) {
        HikDoorStatesReq req = new HikDoorStatesReq();
        req.setDoorIndexCodes(Lists.newArrayList(query.getIndexCode()));
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                query.getCompanyId());
        HikDoorStatesResp resp = hikApiFeignService.queryDoorStates(req,
                hikEntranceGuardBO);
        Optional<AuthDoor> findOptional = Optional.ofNullable(resp.getAuthDoorList())
                .orElse(Lists.newArrayList()).stream()
                .filter(item -> item.getDoorIndexCode().equals(query.getIndexCode())).findFirst();
        if (findOptional.isPresent()) {
            Integer doorState = findOptional.get().getDoorState();
            if (Objects.nonNull(doorState)) {
                return doorState.byteValue();
            }
        }
        return null;
    }

    /**
     * 根据资源码获取数据
     */
    public HaikangAcsDoorEntity selectByIndexCode(Long orgId,String indexCode) {
        LambdaQueryWrapper<HaikangAcsDoorEntity> queryWrapper = Wrappers.<HaikangAcsDoorEntity>lambdaQuery()
                .eq(HaikangAcsDoorEntity::getOrgId, orgId)
                .eq(HaikangAcsDoorEntity::getIndexCode, indexCode);
        List<HaikangAcsDoorEntity> selectList = haikangAcsDoorMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0);
        }
        return null;
    }

    /**
     * 落地到数据库
     */
    public void saveToDB(List<HaikangAcsDoorEntity> entityList) {
        List<HaikangAcsDoorEntity> addList = Lists.newArrayList();
        List<HaikangAcsDoorEntity> updateList = Lists.newArrayList();
        for (HaikangAcsDoorEntity entity : entityList) {
            HaikangAcsDoorEntity existEntity = selectByIndexCode(entity.getOrgId(),entity.getIndexCode());
            if (Objects.isNull(existEntity)) {
                entity.setDeleted(EntityConstants.NOT_DELETED);
                addList.add(entity);
            } else {
                entity.setId(existEntity.getId());
                updateList.add(entity);
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            super.updateBatchById(updateList);
        }
        if (CollectionUtils.isNotEmpty(addList)) {
            super.saveBatch(addList);
        }
    }

    public void updateDoorState(Long orgId,String doorIndexCode, Byte doorState, Byte authState) {
        if (StringUtils.isNotBlank(doorIndexCode)) {
            HaikangAcsDoorEntity entity = selectByIndexCode(orgId,doorIndexCode);
            if (Objects.nonNull(entity)) {
                LambdaUpdateWrapper<HaikangAcsDoorEntity> lambdaedUpdate = Wrappers.lambdaUpdate();
                lambdaedUpdate.eq(HaikangAcsDoorEntity::getId, entity.getId());
                lambdaedUpdate.set(HaikangAcsDoorEntity::getAuthState, authState);
                if (Objects.nonNull(doorState)) {
                    lambdaedUpdate.set(HaikangAcsDoorEntity::getDoorState, doorState);
                }
                lambdaedUpdate.set(HaikangAcsDoorEntity::getDataTime, new Date());
                haikangAcsDoorMapper.update(null, lambdaedUpdate);
            }
        }
    }
}
