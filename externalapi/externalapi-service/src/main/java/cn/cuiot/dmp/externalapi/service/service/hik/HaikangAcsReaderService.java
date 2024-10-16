package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsReaderConverter;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsReaderEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HaikangAcsReaderMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsReaderQuery;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderVo;
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
 * 门禁读卡器信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
@Service
public class HaikangAcsReaderService extends
        ServiceImpl<HaikangAcsReaderMapper, HaikangAcsReaderEntity> {

    @Autowired
    private HaikangAcsReaderMapper haikangAcsReaderMapper;

    @Autowired
    private HaikangAcsReaderConverter haikangAcsReaderConverter;

    /**
     * 分页查询
     */
    public IPage<HaikangAcsReaderVo> queryForPage(HaikangAcsReaderQuery query) {
        Page<HaikangAcsReaderVo> page = haikangAcsReaderMapper.searchList(
                new Page<HaikangAcsReaderVo>(query.getPageNo(), query.getPageSize()), query);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            //patchDataDict(page.getRecords());
        }
        return page;
    }

    /**
     * 查询列表
     */
    public List<HaikangAcsReaderVo> queryForList(HaikangAcsReaderQuery query) {
        List<HaikangAcsReaderVo> list = haikangAcsReaderMapper.searchList(query);
        if (CollectionUtils.isNotEmpty(list)) {
            //patchDataDict(list);
        }
        return list;
    }

    /**
     * 下拉列表查询
     */
    public List<HaikangAcsReaderVo> listForSelect(HaikangAcsReaderQuery query) {
        LambdaQueryWrapper<HaikangAcsReaderEntity> lambdaedQuery = Wrappers.lambdaQuery();

        if (Objects.nonNull(query.getCompanyId())) {
            lambdaedQuery.eq(HaikangAcsReaderEntity::getOrgId, query.getCompanyId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            lambdaedQuery.like(HaikangAcsReaderEntity::getName, query.getName());
        }
        if (StringUtils.isNotBlank(query.getIndexCode())) {
            lambdaedQuery.like(HaikangAcsReaderEntity::getIndexCode, query.getIndexCode());
        }
        if (StringUtils.isNotBlank(query.getDeviceModel())) {
            lambdaedQuery.like(HaikangAcsReaderEntity::getDeviceModel, query.getDeviceModel());
        }
        if (StringUtils.isNotBlank(query.getRegionIndexCode())) {
            lambdaedQuery.eq(HaikangAcsReaderEntity::getRegionIndexCode,
                    query.getRegionIndexCode());
        }
        if (StringUtils.isNotBlank(query.getParentIndexCode())) {
            lambdaedQuery.eq(HaikangAcsReaderEntity::getParentIndexCode,
                    query.getParentIndexCode());
        }
        if (StringUtils.isNotBlank(query.getChannelIndexCode())) {
            lambdaedQuery.eq(HaikangAcsReaderEntity::getChannelIndexCode,
                    query.getChannelIndexCode());
        }
        lambdaedQuery.orderByAsc(HaikangAcsReaderEntity::getCreateTime,
                HaikangAcsReaderEntity::getId);
        List<HaikangAcsReaderEntity> selectList = haikangAcsReaderMapper.selectList(lambdaedQuery);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return haikangAcsReaderConverter.entityListToVoList(selectList);
        }
        return Lists.newArrayList();
    }

    /**
     * 根据资源码获取数据
     */
    public HaikangAcsReaderEntity selectByIndexCode(Long orgId,String indexCode) {
        LambdaQueryWrapper<HaikangAcsReaderEntity> queryWrapper = Wrappers.<HaikangAcsReaderEntity>lambdaQuery()
                .eq(HaikangAcsReaderEntity::getOrgId, orgId)
                .eq(HaikangAcsReaderEntity::getIndexCode, indexCode);
        List<HaikangAcsReaderEntity> selectList = haikangAcsReaderMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0);
        }
        return null;
    }

    /**
     * 落地到数据库
     */
    public void saveToDB(List<HaikangAcsReaderEntity> entityList) {
        List<HaikangAcsReaderEntity> addList = Lists.newArrayList();
        List<HaikangAcsReaderEntity> updateList = Lists.newArrayList();
        for (HaikangAcsReaderEntity entity : entityList) {
            HaikangAcsReaderEntity existEntity = selectByIndexCode(entity.getOrgId(),entity.getIndexCode());
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
    public void updateOnlineStatus(Long orgId,String indexCode, Date collectTime, Byte status) {
        if (StringUtils.isNotBlank(indexCode) && Objects.nonNull(status)) {
            HaikangAcsReaderEntity entity = selectByIndexCode(orgId,indexCode);
            if (Objects.nonNull(entity)) {
                LambdaUpdateWrapper<HaikangAcsReaderEntity> lambdaedUpdate = Wrappers.lambdaUpdate();
                lambdaedUpdate.eq(HaikangAcsReaderEntity::getId, entity.getId());
                lambdaedUpdate.set(HaikangAcsReaderEntity::getStatus, status);
                lambdaedUpdate.set(HaikangAcsReaderEntity::getDataTime, new Date());
                if (Objects.nonNull(collectTime)) {
                    lambdaedUpdate.set(HaikangAcsReaderEntity::getCollectTime, collectTime);
                }
                haikangAcsReaderMapper.update(null, lambdaedUpdate);
            }
        }
    }
}
