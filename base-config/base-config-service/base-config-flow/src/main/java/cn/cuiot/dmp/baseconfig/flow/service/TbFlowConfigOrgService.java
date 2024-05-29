package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.baseconfig.flow.dto.FlowConfigOrgDto;
import cn.cuiot.dmp.common.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfigOrg;
import cn.cuiot.dmp.baseconfig.flow.mapper.TbFlowConfigOrgMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbFlowConfigOrgService extends ServiceImpl<TbFlowConfigOrgMapper, TbFlowConfigOrg> {

    /**
     * 保存流程和组织的中间表
     * @param id
     * @param orgIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFlowOrg(Long id, List<Long> orgIds) {
        List<TbFlowConfigOrg> configOrgs = orgIds.stream().filter(e -> Objects.nonNull(e)).map(e -> {
            TbFlowConfigOrg tbFlowConfigOrg = new TbFlowConfigOrg();
            tbFlowConfigOrg.setFlowConfigId(id);
            tbFlowConfigOrg.setOrgId(e);
            return tbFlowConfigOrg;
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
        LambdaUpdateWrapper<TbFlowConfigOrg> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(TbFlowConfigOrg::getFlowConfigId, ids);
        baseMapper.delete(wrapper);
    }

    /**
     * 根据流程配置ID查询组织ID
     * @param id
     * @return
     */
    public List<Long> queryOrgIdsByFlowConfigId(Long id) {
        LambdaQueryWrapper<TbFlowConfigOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbFlowConfigOrg::getFlowConfigId, id);
        List<TbFlowConfigOrg> list = Optional.ofNullable(baseMapper.selectList(wrapper)).orElse(Lists.newArrayList());
        return list.stream().map(TbFlowConfigOrg::getOrgId).collect(Collectors.toList());
    }

    /**
     * 填充每一行数据的组织机构ID
     * @param dataIds
     * @return
     */
    public Map<Long, String> queryOrgNameByFlowConfigIds(List<Long> dataIds) {
        List<FlowConfigOrgDto> flowConfigOrgDtos = baseMapper.queryOrgNameByFlowConfigIds(dataIds);
        return flowConfigOrgDtos.stream().collect(Collectors.toMap(FlowConfigOrgDto::getDataId, FlowConfigOrgDto::getOrgIdStr));
    }
}
