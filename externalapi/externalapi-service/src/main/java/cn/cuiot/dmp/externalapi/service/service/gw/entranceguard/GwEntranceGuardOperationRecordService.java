package cn.cuiot.dmp.externalapi.service.service.gw.entranceguard;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardOperationQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.entranceguard.GwEntranceGuardOperationPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.GwEntranceGuardOperationRecordEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.entranceguard.GwEntranceGuardOperationRecordMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 格物门禁-操作记录 业务层
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Service
public class GwEntranceGuardOperationRecordService extends ServiceImpl<GwEntranceGuardOperationRecordMapper, GwEntranceGuardOperationRecordEntity> {

    @Autowired
    private SystemApiService systemApiService;

    public IPage<GwEntranceGuardOperationPageVO> queryForPage(GwEntranceGuardOperationQuery query) {
        //企业id
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<GwEntranceGuardOperationPageVO> page = baseMapper.queryForPage(new Page<>(query.getPageNo(), query.getPageSize()), query);

        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            //设置操作人名称
            List<Long> userIds = page.getRecords().stream()
                    .map(GwEntranceGuardOperationPageVO::getOperatorId).distinct().collect(Collectors.toList());
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            baseUserReqDto.setUserIdList(userIds);
            List<BaseUserDto> baseUserDtoList = systemApiService.lookUpUserList(baseUserReqDto);
            Map<Long, BaseUserDto> map = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, e -> e));

            page.getRecords()
                    .forEach(item -> item.setOperatorName(map.containsKey(item.getOperatorId()) ? map.get(item.getOperatorId()).getName() : null));
        }

        return page;
    }
}
