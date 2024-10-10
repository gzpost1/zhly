package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.externalapi.service.constant.HaikangDataDictConstant;
import cn.cuiot.dmp.externalapi.service.converter.hik.HaikangAcsDoorConverter;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangDataDictEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HaikangAcsDoorMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorControlDto;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorQuery;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikDoorControlReq;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
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
            lambdaedQuery.eq(HaikangAcsDoorEntity::getIndexCode, query.getIndexCode());
        }
        if (StringUtils.isNotBlank(query.getDoorNo())) {
            lambdaedQuery.eq(HaikangAcsDoorEntity::getDoorNo, query.getDoorNo());
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
    public void dooControlDoor(HaikangAcsDoorControlDto dto) {
        LambdaQueryWrapper<HaikangAcsDoorEntity> lambdaedQuery = Wrappers.lambdaQuery();
        lambdaedQuery.eq(HaikangAcsDoorEntity::getOrgId, dto.getCompanyId());
        lambdaedQuery.in(HaikangAcsDoorEntity::getIndexCode, dto.getIndexCodes());
        List<HaikangAcsDoorEntity> selectList = Optional.ofNullable(haikangAcsDoorMapper.selectList(lambdaedQuery)).orElse(Lists.newArrayList());
        AssertUtil.isFalse(dto.getIndexCodes().size()>selectList.size(),"操作失败，只能操作本企业的数据");

        HikDoorControlReq req = new HikDoorControlReq();
        req.setControlType(dto.getControlType());
        req.setDoorIndexCodes(dto.getIndexCodes());

        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                dto.getCompanyId());

        hikApiFeignService.doorDoControl(req,hikEntranceGuardBO);

    }

}
