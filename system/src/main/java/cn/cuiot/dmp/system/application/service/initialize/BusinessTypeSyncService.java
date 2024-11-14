package cn.cuiot.dmp.system.application.service.initialize;

import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.OrganizationReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.OrganizationRespDTO;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.infrastructure.entity.BusinessTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.BusinessTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.BUSINESS_TYPE;
import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.COMPANY_INITIALIZE;

/**
 * 同步企业业务类型
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class BusinessTypeSyncService extends DataSyncService<BusinessTypeEntity> {

    @Resource
    private BusinessTypeMapper businessTypeMapper;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private ApiSystemService apiSystemService;

    @Override
    public List<BusinessTypeEntity> fetchData(Long sourceCompanyId) {
        List<BusinessTypeEntity> list = businessTypeMapper.selectList(
                new LambdaQueryWrapper<BusinessTypeEntity>()
                        .eq(BusinessTypeEntity::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【业务类型】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<BusinessTypeEntity>> preprocessData(List<BusinessTypeEntity> data, Long targetCompanyId) {
        // 企业名称
        String companyName = getCompanyNameById(targetCompanyId);

        Map<Long, FormConfigTypeTreeNodeVO> map = buildFormMap(data);

        List<SyncCompanyRelationDTO<BusinessTypeEntity>> collect = data.stream().map(item -> {
            FormConfigTypeTreeNodeVO nodeVO = map.get(item.getId());
            BusinessTypeEntity entity = new BusinessTypeEntity();
            entity.setId(Long.parseLong(nodeVO.getId()));
            entity.setParentId(Long.parseLong(nodeVO.getParentId()));
            entity.setName(Objects.equal(item.getParentId(), -1L) ? companyName : item.getName());
            entity.setLevelType(item.getLevelType());
            entity.setCompanyId(targetCompanyId);
            entity.setPathName(item.getPathName());
            entity.setDeletedFlag(item.getDeletedFlag());

            return new SyncCompanyRelationDTO<>(entity, item.getId());
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + BUSINESS_TYPE, JsonUtil.writeValueAsString(collect), Const.ONE_DAY_SECOND);
        }

        return collect;
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<BusinessTypeEntity>> data, Long targetCompanyId) {
        data.forEach(item -> businessTypeMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<BusinessTypeEntity>> targetData, SyncCompanyDTO dto) {

    }

    /**
     * 构造表单新老数据对照
     */
    public static Map<Long, FormConfigTypeTreeNodeVO> buildFormMap(List<BusinessTypeEntity> data) {
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = data.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> tree = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        Map<Long, FormConfigTypeTreeNodeVO> map = Maps.newHashMap();
        updateIds(tree, null, map);
        return map;
    }

    /**
     * 递归修改表单id
     */
    private static void updateIds(List<FormConfigTypeTreeNodeVO> nodes, String parentId, Map<Long, FormConfigTypeTreeNodeVO> map) {
        for (FormConfigTypeTreeNodeVO node : nodes) {
            // 更新当前节点的 ID 和 Parent ID
            Long oldId = Long.parseLong(node.getId());
            String newId = String.valueOf(IdWorker.getId());
            node.setId(newId);
            if (StringUtils.isNotBlank(parentId)) {
                node.setParentId(parentId);
            }

            // 添加到 map 中，并清空子节点引用
            map.put(oldId, new FormConfigTypeTreeNodeVO(newId, node.getParentId(), node.getName(), node.getLevelType(), node.getCompanyId()));

            // 递归更新子节点
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                updateIds(node.getChildren(), newId, map);
            }
        }
    }

    /**
     * 获取企业名称
     * @Param targetCompanyId 参数
     * @return 企业名称
     */
    private String getCompanyNameById(Long targetCompanyId) {
        OrganizationReqDTO dto = new OrganizationReqDTO();
        dto.setIdList(Collections.singletonList(targetCompanyId));
        List<OrganizationRespDTO> list = apiSystemService.queryOrganizationList(dto);
        return CollectionUtils.isNotEmpty(list) ? list.get(0).getCompanyName() : null;
    }
}
