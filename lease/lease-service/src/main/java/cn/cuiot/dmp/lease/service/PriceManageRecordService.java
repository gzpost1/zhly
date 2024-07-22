package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.price.PriceManageRecordPageQueryDTO;
import cn.cuiot.dmp.lease.entity.PriceManageRecordEntity;
import cn.cuiot.dmp.lease.mapper.PriceManageRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Slf4j
@Service
public class PriceManageRecordService extends ServiceImpl<PriceManageRecordMapper, PriceManageRecordEntity> {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 保存定价管理操作记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePriceManageRecord(PriceManageRecordEntity priceManageRecordEntity) {
        save(priceManageRecordEntity);
    }

    /**
     * 通过定价管理id查询定价管理记录
     */
    public PageResult<PriceManageRecordEntity> queryByPriceId(PriceManageRecordPageQueryDTO pageQueryDTO) {
        AssertUtil.notNull(pageQueryDTO.getPriceId(), "定价管理id不能为空");
        LambdaQueryWrapper<PriceManageRecordEntity> queryWrapper = new LambdaQueryWrapper<PriceManageRecordEntity>()
                .eq(PriceManageRecordEntity::getPriceId, pageQueryDTO.getPriceId())
                .orderByDesc(PriceManageRecordEntity::getOperateTime);
        IPage<PriceManageRecordEntity> priceManageRecordEntityIPage = page(new Page<>(pageQueryDTO.getPageNo(),
                pageQueryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(priceManageRecordEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        fillUserName(priceManageRecordEntityIPage.getRecords());
        PageResult<PriceManageRecordEntity> pageResult = new PageResult<>();
        pageResult.setList(priceManageRecordEntityIPage.getRecords());
        pageResult.setCurrentPage((int) priceManageRecordEntityIPage.getCurrent());
        pageResult.setPageSize((int) priceManageRecordEntityIPage.getSize());
        pageResult.setTotal(priceManageRecordEntityIPage.getTotal());
        return pageResult;
    }

    /**
     * 使用用户id列表查询出，对应的用户名称
     *
     * @param priceManageRecordEntities 定价管理记录列表
     */
    private void fillUserName(List<PriceManageRecordEntity> priceManageRecordEntities) {
        if (CollectionUtils.isEmpty(priceManageRecordEntities)) {
            return;
        }
        Set<Long> userIdList = new HashSet<>();
        priceManageRecordEntities.forEach(o -> {
            if (Objects.nonNull(o.getOperatorId())) {
                userIdList.add(o.getOperatorId());
            }
        });
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserIdList(new ArrayList<>(userIdList));
        List<BaseUserDto> baseUserDtoList = systemApiFeignService.lookUpUserList(reqDto).getData();
        Map<Long, String> userMap = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
        priceManageRecordEntities.forEach(o -> {
            if (Objects.nonNull(o.getOperatorId()) && userMap.containsKey(o.getOperatorId())) {
                o.setOperatorName(userMap.get(o.getOperatorId()));
            }
        });
    }

}
