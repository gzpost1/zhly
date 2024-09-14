package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonAuthorizeQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.AuthorizePushParams;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
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
    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;
    @Autowired
    private GwEntranceGuardConfigService configService;
    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;

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

        // 获取对接参数
        GWEntranceGuardBO configInfo = configService.getConfigInfo(companyId);

        //人员所属企业校验
        gwEntranceGuardPersonService.checkPerson(personIds, companyId);

        List<GwEntranceGuardAuthorizeEntity> authorizeList = list(
                new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                        .eq(GwEntranceGuardAuthorizeEntity::getCompanyId, companyId)
                        .in(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, personIds));
        Map<Long, List<GwEntranceGuardAuthorizeEntity>> map = authorizeList.stream()
                .collect(Collectors.groupingBy(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId));

        personIds.forEach(item -> {
            // 需求需要增加授权的列表
            List<Long> needSaveEntranceGuardIds;
            // 需要删除的授权列表
            List<Long> needDeletEentranceGuardIds = null;
            if (map.containsKey(item)) {
                List<GwEntranceGuardAuthorizeEntity> list = map.get(item);
                List<Long> dbEntranceGuardIds = list.stream().map(GwEntranceGuardAuthorizeEntity::getEntranceGuardId)
                        .collect(Collectors.toList());

                needSaveEntranceGuardIds = entranceGuardIds.stream()
                        .filter(e -> !dbEntranceGuardIds.contains(e))
                        .collect(Collectors.toList());

                needDeletEentranceGuardIds = dbEntranceGuardIds.stream()
                        .filter(e -> !entranceGuardIds.contains(e))
                        .collect(Collectors.toList());
            } else {
                needSaveEntranceGuardIds = entranceGuardIds;
            }

            //批量保存
            saveBatch(needSaveEntranceGuardIds, item, companyId, configInfo);

            //批量删除
            removeBatch(needDeletEentranceGuardIds, item, companyId, configInfo);

        });
    }

    private void saveBatch(List<Long> entranceGuardId, Long personId, Long companyId, GWEntranceGuardBO configInfo) {
        if (CollectionUtils.isNotEmpty(entranceGuardId)) {
            //获取门禁id列表
            List<GwEntranceGuardEntity> gwEntranceGuardList = gwEntranceGuardService.listByIds(entranceGuardId);

            gwEntranceGuardList.forEach(item -> {
                InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();
                req.setKey(GwEntranceGuardServiceKeyConstant.AUTHORIZE);
                //设置参数
                AuthorizePushParams authorizePushParams = new AuthorizePushParams();
                authorizePushParams.buildAuthorizeAddPushParams(gwEntranceGuardPersonService.getById(personId), item.getSn());
                req.setArguments(JsonUtil.writeValueAsString(authorizePushParams));

                req.setIotId(item.getIotId());

                dmpDeviceRemoteService.invokeDeviceService(req, configInfo);
            });


            List<GwEntranceGuardAuthorizeEntity> collect = entranceGuardId.stream().map(e -> {
                GwEntranceGuardAuthorizeEntity entity = new GwEntranceGuardAuthorizeEntity();
                entity.setCompanyId(companyId);
                entity.setEntranceGuardId(e);
                entity.setEntranceGuardPersonId(personId);
                return entity;
            }).collect(Collectors.toList());

            saveBatch(collect);
        }
    }

    private void removeBatch(List<Long> entranceGuardId, Long personId, Long companyId, GWEntranceGuardBO configInfo) {
        if (CollectionUtils.isNotEmpty(entranceGuardId)) {

            //删除权限
            InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();
            req.setKey(GwEntranceGuardServiceKeyConstant.AUTHORIZE);
            //设置参数
            AuthorizePushParams authorizePushParams = new AuthorizePushParams();
            authorizePushParams.buildAuthorizeDeletePushParams(personId);
            req.setArguments(JsonUtil.writeValueAsString(authorizePushParams));
            dmpDeviceRemoteService.invokeDeviceService(req, configInfo);

            remove(new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                    .eq(GwEntranceGuardAuthorizeEntity::getCompanyId, companyId)
                    .eq(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, personId)
                    .in(GwEntranceGuardAuthorizeEntity::getEntranceGuardId, entranceGuardId));
        }
    }
}
