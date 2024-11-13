package cn.cuiot.dmp.system.application.service.initialize;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.system.infrastructure.entity.RoleEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 同步企业角色
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class RoleSyncService extends DataSyncService<RoleEntity> {

    @Resource
    private RoleDao roleDao;
    @Resource
    private OrgMenuDao orgMenuDao;

    @Override
    public List<RoleEntity> fetchData(Long companyId) {
        List<RoleEntity> list = roleDao.lookUpRoleList(companyId, null);
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【角色】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<RoleEntity>> preprocessData(List<RoleEntity> data, Long targetCompanyId) {
        return data.stream().map(item -> {
            RoleEntity entity = new RoleEntity();
            BeanUtils.copyProperties(item, entity);
            entity.setRoleKey(String.valueOf(SnowflakeIdWorkerUtil.nextId()));
            entity.setId(SnowflakeIdWorkerUtil.nextId());
            entity.setOrgId(targetCompanyId + "");
            return new SyncCompanyRelationDTO<>(entity, item.getId());
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<RoleEntity>> data, Long targetCompanyId) {
        data.forEach(item -> roleDao.insertRole(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<RoleEntity>> targetData, SyncCompanyDTO dto) {

        Long sourceCompanyId = dto.getSourceCompanyId();
        Long targetCompanyId = dto.getTargetCompanyId();

        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = LoginInfoHolder.getCurrentUserId();

        // 新增组织角色关联表数据
        targetData.forEach(item -> {
            Map<String, Object> paramsMap = new HashMap<>(3);
            paramsMap.put("roleId", item.getEntity().getId());
            paramsMap.put("orgId", item.getEntity().getOrgId());
            paramsMap.put("createdBy", currentUserId);
            paramsMap.put("id", SnowflakeIdWorkerUtil.nextId());
            roleDao.insertOrgRole(paramsMap);
        });

        // 查询目标企业选择的菜单
        List<String> targetMenuListByOrgId = orgMenuDao.getMenuListByOrgId(targetCompanyId + "");

        // 新增角色菜单关联表数据,只保存创建企业时选择的菜单
        targetData.forEach(item -> {
            List<String> menuIds = roleDao.findMenuPksByRolePk(item.getOldId());
            if (CollectionUtils.isNotEmpty(menuIds)) {
                menuIds = menuIds.stream().filter(targetMenuListByOrgId::contains).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(menuIds)) {
                    roleDao.insertMenuRole(item.getEntity().getId(), menuIds, currentUserId + "", 2);
                }
            }
        });
    }
}
