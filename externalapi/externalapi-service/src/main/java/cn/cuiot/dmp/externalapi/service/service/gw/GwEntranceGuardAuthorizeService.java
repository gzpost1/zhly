package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonAuthorizeQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.AuthorizePushParams;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwEntranceGuardAuthorizeMapper;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardAuthorizeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 格物门禁-授权 业务层
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Slf4j
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
        // 获取当前企业ID
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        // 获取人员ID列表和门禁ID列表
        List<Long> personIds = query.getPersonIds();
        List<Long> entranceGuardIds = query.getEntranceGuardIds();

        // 根据企业ID获取对应的配置参数
        GWEntranceGuardBO configInfo = configService.getConfigInfo(companyId);

        // 校验这些人员是否属于当前企业
        gwEntranceGuardPersonService.checkPerson(personIds, companyId);

        // 查询数据库中已存在的授权信息
        List<GwEntranceGuardAuthorizeEntity> authorizeList = list(
                new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                        .eq(GwEntranceGuardAuthorizeEntity::getCompanyId, companyId)
                        .in(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, personIds)
        );

        // 将已有授权信息按照人员ID进行分组
        Map<Long, List<GwEntranceGuardAuthorizeEntity>> authorizeMap = authorizeList.stream()
                .collect(Collectors.groupingBy(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId));

        // 遍历所有人员ID，处理每个人员的门禁授权情况
        personIds.forEach(personId -> {
            List<Long> needSaveEntranceGuardIds;    // 需要新增授权的门禁ID列表
            List<Long> needDeleteEntranceGuardIds = Collections.emptyList();    // 需要删除授权的门禁ID列表

            // 如果当前人员已有授权记录，则计算需要新增和删除的门禁ID
            if (authorizeMap.containsKey(personId)) {
                List<Long> dbEntranceGuardIds = authorizeMap.get(personId).stream()
                        .map(GwEntranceGuardAuthorizeEntity::getEntranceGuardId)
                        .collect(Collectors.toList());

                // 计算需要新增的门禁ID（差集）
                needSaveEntranceGuardIds = getDifference(entranceGuardIds, dbEntranceGuardIds);
                // 计算需要删除的门禁ID（差集）
                needDeleteEntranceGuardIds = getDifference(dbEntranceGuardIds, entranceGuardIds);
            } else {
                // 如果没有授权记录，则所有的门禁ID都需要新增授权
                needSaveEntranceGuardIds = entranceGuardIds;
            }

            // 批量新增授权
            processBatch(needSaveEntranceGuardIds, personId, companyId, configInfo, true);

            // 批量删除授权
            processBatch(needDeleteEntranceGuardIds, personId, companyId, configInfo, false);
        });
    }

    private List<Long> getDifference(List<Long> list1, List<Long> list2) {
        // 计算 list1 与 list2 的差集，返回不在 list2 中的元素
        return list1.stream().filter(e -> !list2.contains(e)).collect(Collectors.toList());
    }

    /**
     * 批量处理授权或取消授权操作
     *
     * @param entranceGuardIds    需要处理的门禁ID列表
     * @param personId            人员ID
     * @param companyId           企业ID
     * @param configInfo          门禁配置参数
     * @param isSaveOperation     true表示新增授权，false表示取消授权
     */
    private void processBatch(List<Long> entranceGuardIds, Long personId, Long companyId, GWEntranceGuardBO configInfo, boolean isSaveOperation) {
        if (CollectionUtils.isNotEmpty(entranceGuardIds)) {
            // 获取门禁ID对应的门禁实体信息
            List<GwEntranceGuardEntity> gwEntranceGuardList = gwEntranceGuardService.listByIds(entranceGuardIds);

            // 保存未能成功处理授权的设备名称列表
            List<String> errorDevices = new ArrayList<>();
            // 保存成功处理授权的门禁ID列表
            List<Long> successDeviceIds = new ArrayList<>();

            // 遍历所有门禁设备，处理授权或取消授权
            gwEntranceGuardList.forEach(entranceGuard -> {
                // 如果设备Key为空，记录设备名称作为错误设备
                if (StringUtils.isBlank(entranceGuard.getDeviceKey())) {
                    errorDevices.add(entranceGuard.getName());
                } else {
                    try {
                        // 调用设备远程服务进行授权或取消授权
                        InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();
                        req.setKey(GwEntranceGuardServiceKeyConstant.AUTHORIZE);

                        if (isSaveOperation) {
                            // 构建新增授权的参数
                            AuthorizePushParams authorizePushParams = AuthorizePushParams.buildAuthorizeAddPushParams(gwEntranceGuardPersonService.getById(personId), entranceGuard.getSn());
                            req.setArguments(JsonUtil.writeValueAsString(authorizePushParams));
                        } else {
                            // 构建取消授权的参数
                            AuthorizePushParams authorizePushParams = AuthorizePushParams.buildAuthorizeDeletePushParams(personId);
                            req.setArguments(JsonUtil.writeValueAsString(authorizePushParams));
                        }

                        req.setIotId(entranceGuard.getIotId());

                        configInfo.setDeviceKey(entranceGuard.getDeviceKey());
                        configInfo.setRequestId(GwBusinessTypeConstant.ENTRANCE_GUARD + "-" + req.getKey() + "-" + entranceGuard.getId() + "-" + System.currentTimeMillis());
                        dmpDeviceRemoteService.invokeDeviceService(req, configInfo);

                        // 如果操作成功，记录成功的设备ID
                        successDeviceIds.add(entranceGuard.getId());
                    } catch (Exception e) {
                        // 如果授权或取消授权操作异常，记录错误信息和设备名称
                        log.error("门禁【" + entranceGuard.getId() + "】" + (isSaveOperation ? "授权" : "取消授权") + "异常........", e);
                        errorDevices.add(entranceGuard.getName());
                    }
                }
            });

            // 如果有成功操作的设备，保存或删除相应的授权记录
            if (CollectionUtils.isNotEmpty(successDeviceIds)) {
                if (isSaveOperation) {
                    // 批量保存授权记录
                    List<GwEntranceGuardAuthorizeEntity> collect = successDeviceIds.stream().map(e -> {
                        GwEntranceGuardAuthorizeEntity entity = new GwEntranceGuardAuthorizeEntity();
                        entity.setCompanyId(companyId);
                        entity.setEntranceGuardId(e);
                        entity.setEntranceGuardPersonId(personId);
                        return entity;
                    }).collect(Collectors.toList());
                    saveBatch(collect);
                } else {
                    // 批量删除授权记录
                    remove(new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                            .eq(GwEntranceGuardAuthorizeEntity::getCompanyId, companyId)
                            .eq(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, personId)
                            .in(GwEntranceGuardAuthorizeEntity::getEntranceGuardId, successDeviceIds));
                }
            }

            // 如果有未成功操作的设备，抛出异常并返回错误设备的名称
            if (CollectionUtils.isNotEmpty(errorDevices)) {
                String names = String.join(",", errorDevices);
                throw new BusinessException(ResultCode.ERROR, "门禁【" + names + "】" + (isSaveOperation ? "授权" : "取消授权") + "失败");
            }
        }
    }
}
