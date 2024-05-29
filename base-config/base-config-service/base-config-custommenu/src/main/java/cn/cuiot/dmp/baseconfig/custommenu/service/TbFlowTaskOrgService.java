package cn.cuiot.dmp.baseconfig.custommenu.service;

import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskOrgDto;
import cn.cuiot.dmp.common.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskOrgMapper;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskOrg;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TbFlowTaskOrgService extends ServiceImpl<TbFlowTaskOrgMapper, TbFlowTaskOrg> {

    /**
     * 保存流程和组织的中间表
     * @param id
     * @param orgIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFlowOrg(Long id, List<Long> orgIds) {
        List<TbFlowTaskOrg> configOrgs = orgIds.stream().filter(e -> Objects.nonNull(e)).map(e -> {
            TbFlowTaskOrg TbFlowTaskOrg = new TbFlowTaskOrg();
            TbFlowTaskOrg.setFlowTaskId(id);
            TbFlowTaskOrg.setOrgId(e);
            return TbFlowTaskOrg;
        }).collect(Collectors.toList());

        AssertUtil.notEmpty(configOrgs,"所属组织未选中");
        baseMapper.insertList(configOrgs);
    }

    /**
     * 更新流程和组织的中间表
     * @param id
     * @param orgId
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFlowOrg(Long id, List<Long> orgId) {
        //删除原来的中间表数据
        deleteByFlowConfigIds(Lists.newArrayList(id));
        //保存新的中间表数据
        saveFlowOrg(id, orgId);
    }

    /**
     * 根据流程配置删除中间表
     * @param ids
     */
    public void deleteByFlowConfigIds(List<Long> ids) {
        LambdaUpdateWrapper<TbFlowTaskOrg> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(TbFlowTaskOrg::getFlowTaskId, ids);
        baseMapper.delete(wrapper);
    }

    /**
     * 根据流程配置ID查询组织ID
     * @param id
     * @return
     */
    public List<Long> queryOrgIdsByFlowConfigId(Long id) {
        LambdaQueryWrapper<TbFlowTaskOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbFlowTaskOrg::getFlowTaskId, id);
        List<TbFlowTaskOrg> list = Optional.ofNullable(baseMapper.selectList(wrapper)).orElse(Lists.newArrayList());
        return list.stream().map(TbFlowTaskOrg::getOrgId).collect(Collectors.toList());
    }

    /**
     * 填充每一行数据的组织机构ID
     * @param dataIds
     * @return
     */
    public Map<Long, String> queryOrgNameByFlowConfigIds(List<Long> dataIds) {
        List<FlowTaskOrgDto> flowConfigOrgDtos = baseMapper.queryOrgNameByFlowConfigIds(dataIds);
        return flowConfigOrgDtos.stream().collect(Collectors.toMap(FlowTaskOrgDto::getDataId, FlowTaskOrgDto::getOrgIdStr));
    }
}
