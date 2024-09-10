package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonAuthorizeQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwEntranceGuardAuthorizeMapper;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardAuthorizeEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 格物门禁-授权 业务层
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Service
public class GwEntranceGuardAuthorizeService extends ServiceImpl<GwEntranceGuardAuthorizeMapper, GwEntranceGuardAuthorizeEntity> {

    @Autowired
    private GwEntranceGuardPersonService gwEntranceGuardPersonService;

    /**
     * 授权信息
     *
     * @return list
     * @Param 门禁用户id
     */
    public List<GwEntranceGuardAuthorizeEntity> queryAuthorize(Long personId, Long companyId) {
        return list(new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                .eq(GwEntranceGuardAuthorizeEntity::getCompanyId, companyId)
                .eq(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, personId));
    }

    /**
     * 批量授权
     */
    public void batchAuthorize(GwEntranceGuardPersonAuthorizeQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 人员id列表
        List<Long> personIds = query.getPersonIds();
        // 门禁id列表
        List<Long> entranceGuardIds = query.getEntranceGuardIds();

        //人员所属企业校验
        gwEntranceGuardPersonService.checkPerson(personIds, companyId);

        List<GwEntranceGuardAuthorizeEntity> authorizeList = list(
                new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                        .eq(GwEntranceGuardAuthorizeEntity::getCompanyId, companyId)
                        .in(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, personIds));
        Map<Long, List<GwEntranceGuardAuthorizeEntity>> map = authorizeList.stream()
                .collect(Collectors.groupingBy(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId));

        personIds.forEach(item ->{
            // 需求需要增加授权的列表
            List<Long> needSaveIds = null;
            // 需要删除的授权列表
            List<Long> needDeleteIds = null;
            if (map.containsKey(item)) {
                List<GwEntranceGuardAuthorizeEntity> list = map.get(item);
                List<Long> dbEntranceGuardIds = list.stream().map(GwEntranceGuardAuthorizeEntity::getEntranceGuardId)
                        .collect(Collectors.toList());

                needSaveIds = entranceGuardIds.stream()
                        .filter(e -> !dbEntranceGuardIds.contains(item))
                        .collect(Collectors.toList());

                needDeleteIds = dbEntranceGuardIds.stream()
                        .filter(e -> !entranceGuardIds.contains(item))
                        .collect(Collectors.toList());
            }else {
                needSaveIds = entranceGuardIds;
            }

            if (CollectionUtils.isNotEmpty(needSaveIds)) {

            }
            if (CollectionUtils.isNotEmpty(needDeleteIds)) {

            }
        });
    }
}
