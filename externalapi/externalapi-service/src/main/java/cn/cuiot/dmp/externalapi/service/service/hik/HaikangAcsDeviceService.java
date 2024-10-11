package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsDeviceConverter;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDeviceEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HaikangAcsDeviceMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDeviceQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangRegionQuery;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikRegionReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikRegionResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikRegionResp.DataItem;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 门禁设备信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
@Service
public class HaikangAcsDeviceService extends
        ServiceImpl<HaikangAcsDeviceMapper, HaikangAcsDeviceEntity> {

    @Autowired
    private HaikangAcsDeviceMapper haikangAcsDeviceMapper;

    @Autowired
    private HaikangAcsDeviceConverter haikangAcsDeviceConverter;

    @Autowired
    private HikApiFeignService hikApiFeignService;

    @Autowired
    private HikCommonHandle hikCommonHandle;

    /**
     * 区域查询默认分页大小
     */
    private final static Long DEFAULT_PAGE_SIZE = 1000L;

    /**
     * 分页查询
     */
    public IPage<HaikangAcsDeviceVo> queryForPage(HaikangAcsDeviceQuery query) {
        Page<HaikangAcsDeviceVo> page = haikangAcsDeviceMapper.searchList(
                new Page<HaikangAcsDeviceVo>(query.getPageNo(), query.getPageSize()), query);
        return page;
    }

    /**
     * 查询列表
     */
    public List<HaikangAcsDeviceVo> queryForList(HaikangAcsDeviceQuery query) {
        List<HaikangAcsDeviceVo> list = haikangAcsDeviceMapper.searchList(query);
        return list;
    }

    /**
     * 下拉列表查询
     */
    public List<HaikangAcsDeviceVo> listForSelect(HaikangAcsDeviceQuery query) {
        LambdaQueryWrapper<HaikangAcsDeviceEntity> lambdaedQuery = Wrappers.lambdaQuery();

        if (Objects.nonNull(query.getCompanyId())) {
            lambdaedQuery.eq(HaikangAcsDeviceEntity::getOrgId, query.getCompanyId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            lambdaedQuery.like(HaikangAcsDeviceEntity::getName, query.getName());
        }
        if (StringUtils.isNotBlank(query.getIndexCode())) {
            lambdaedQuery.eq(HaikangAcsDeviceEntity::getIndexCode, query.getIndexCode());
        }
        if (StringUtils.isNotBlank(query.getRegionIndexCode())) {
            lambdaedQuery.eq(HaikangAcsDeviceEntity::getRegionIndexCode,
                    query.getRegionIndexCode());
        }
        if (Objects.nonNull(query.getStatus())) {
            lambdaedQuery.eq(HaikangAcsDeviceEntity::getStatus, query.getStatus());
        }
        lambdaedQuery.orderByAsc(HaikangAcsDeviceEntity::getCreateTime,
                HaikangAcsDeviceEntity::getId);
        List<HaikangAcsDeviceEntity> selectList = haikangAcsDeviceMapper.selectList(lambdaedQuery);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return haikangAcsDeviceConverter.entityListToVoList(selectList);
        }
        return Lists.newArrayList();
    }

    /**
     * 查询区域
     */
    public List<DataItem> queryRegions(HaikangRegionQuery query) {
        HikRegionReq req = new HikRegionReq();

        req.setPageNo(query.getPageNo());
        if (Objects.isNull(req.getPageNo())) {
            req.setPageNo(1L);
        }
        req.setPageSize(query.getPageSize());
        if (Objects.isNull(req.getPageSize())) {
            req.setPageSize(DEFAULT_PAGE_SIZE);
        }
        req.setResourceType(req.getResourceType());
        if (StringUtils.isBlank(req.getResourceType())) {
            req.setResourceType("region");
        }
        req.setIsSubRegion(req.getIsSubRegion());
        if (Objects.isNull(req.getIsSubRegion())) {
            req.setIsSubRegion(true);
        }

        req.setRegionType(query.getRegionType());

        req.setRegionName(query.getRegionName());

        req.setCascadeFlag(req.getCascadeFlag());
        if (Objects.isNull(req.getCascadeFlag())) {
            req.setCascadeFlag(0);
        }

        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                query.getCompanyId());

        HikRegionResp hikRegionResp = hikApiFeignService.queryRegions(req, hikEntranceGuardBO);
        return hikRegionResp.getList();
    }


    /**
     * 根据资源码获取数据
     */
    public HaikangAcsDeviceEntity selectByIndexCode(String indexCode) {
        LambdaQueryWrapper<HaikangAcsDeviceEntity> queryWrapper = Wrappers.<HaikangAcsDeviceEntity>lambdaQuery()
                .eq(HaikangAcsDeviceEntity::getIndexCode, indexCode);
        List<HaikangAcsDeviceEntity> selectList = haikangAcsDeviceMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0);
        }
        return null;
    }

    /**
     * 落地到数据库
     */
    public void saveToDB(List<HaikangAcsDeviceEntity> entityList) {
        List<HaikangAcsDeviceEntity> addList = Lists.newArrayList();
        List<HaikangAcsDeviceEntity> updateList = Lists.newArrayList();
        for (HaikangAcsDeviceEntity entity : entityList) {
            HaikangAcsDeviceEntity existEntity = selectByIndexCode(entity.getIndexCode());
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

    /**
     * 更新在线状态
     */
    public void updateOnlineStatus(String indexCode, Date collectTime, Byte status) {
        if (StringUtils.isNotBlank(indexCode) && Objects.nonNull(status)) {
            HaikangAcsDeviceEntity entity = selectByIndexCode(indexCode);
            if (Objects.nonNull(entity)) {
                LambdaUpdateWrapper<HaikangAcsDeviceEntity> lambdaedUpdate = Wrappers.lambdaUpdate();
                lambdaedUpdate.eq(HaikangAcsDeviceEntity::getId, entity.getId());
                lambdaedUpdate.set(HaikangAcsDeviceEntity::getStatus, status);
                lambdaedUpdate.set(HaikangAcsDeviceEntity::getDataTime, new Date());
                if (Objects.nonNull(collectTime)) {
                    lambdaedUpdate.set(HaikangAcsDeviceEntity::getCollectTime, collectTime);
                }
                haikangAcsDeviceMapper.update(null, lambdaedUpdate);
            }
        }
    }

}
