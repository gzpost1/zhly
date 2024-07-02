package cn.cuiot.dmp.baseconfig.custommenu.service;

import cn.cuiot.dmp.base.infrastructure.constants.OperationConstant;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskConfigInsertDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskConfigUpdateDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoPageDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.TbFlowTaskInfoQuery;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskConfigVo;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskInfoVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.baseconfig.custommenu.mapper.TbFlowTaskConfigMapper;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TbFlowTaskConfigService extends ServiceImpl<TbFlowTaskConfigMapper, TbFlowTaskConfig> {
    @Autowired
    private TbFlowTaskInfoService flowTaskInfoService;
    @Autowired
    private TbFlowTaskOrgService flowTaskOrgService;


    /**
     * 分页查询
     * @param query
     * @return
     */
    public IPage<FlowTaskInfoPageDto> queryForPage(TbFlowTaskInfoQuery query) {
        IPage<FlowTaskInfoPageDto> flowTaskInfoPageDtoIPage = baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
        //填充每一行的组织机构ID
        if (Objects.nonNull(flowTaskInfoPageDtoIPage) && CollectionUtils.isNotEmpty(flowTaskInfoPageDtoIPage.getRecords())) {
            List<Long> dataIds = flowTaskInfoPageDtoIPage.getRecords().stream().map(FlowTaskInfoPageDto::getId).collect(Collectors.toList());
            Map<Long, String> orgNameMap = flowTaskOrgService.queryOrgNameByFlowConfigIds(dataIds);
            if (Objects.nonNull(orgNameMap)) {
                flowTaskInfoPageDtoIPage.getRecords().forEach(e -> {
                    if (orgNameMap.containsKey(e.getId())) {
                        List<Long> orgIds = Arrays.stream(StringUtils.split(orgNameMap.get(e.getId()), ","))
                                .map(idstr -> Long.valueOf(idstr)).collect(Collectors.toList());
                        e.setOrgIds(orgIds);
                    }
                });
            }

        }
        return flowTaskInfoPageDtoIPage;
    }

    /**
     * 批量操作
     * @param batcheOperation
     */
    public void batchedOperation(BatcheOperation batcheOperation) {
        if(Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.DELETE)){
            //删除
            this.delete(batcheOperation.getIds());
        }else if(Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.STATUS)){
            //修改状态
            LambdaUpdateWrapper<TbFlowTaskConfig> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(TbFlowTaskConfig::getId, batcheOperation.getIds());
            updateWrapper.set(TbFlowTaskConfig::getStatus, batcheOperation.getOperaToStatus());
            this.update(updateWrapper);
        }else if(Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.MOVE)){
            //移动业务类型
            LambdaUpdateWrapper<TbFlowTaskConfig> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(TbFlowTaskConfig::getId, batcheOperation.getIds());
            updateWrapper.set(TbFlowTaskConfig::getBusinessTypeId, batcheOperation.getOperaToStatus());
            this.update(updateWrapper);
        }
    }

    /**
     * 删除
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        //1 删除任务基本信息
        this.removeByIds(ids);
        //2 删除任务对象
        flowTaskInfoService.deleteByTaskConfigIds(ids);
        //3 删除任务组织中间表
        flowTaskOrgService.deleteByFlowConfigIds(ids);
    }

    /**
     * 更新状态
     * @param updateStatusParam
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(UpdateStatusParam updateStatusParam) {
        TbFlowTaskConfig entity = this.getById(updateStatusParam.getId());
        entity.setStatus(updateStatusParam.getStatus());
        this.updateById(entity);
    }

    /**
     * 创建
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(FlowTaskConfigInsertDto createDto) {
        //创建任务配置
        TbFlowTaskConfig entity = new TbFlowTaskConfig();
        BeanUtils.copyProperties(createDto, entity);
        entity.setId(IdWorker.getId());
        entity.setStatus(EntityConstants.ENABLED);
        entity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        this.save(entity);

        //创建任务对象
        flowTaskInfoService.create(entity.getId(), createDto.getTaskInfoList());

        //创建流程和组织的中间表
        flowTaskOrgService.saveFlowOrg(entity.getId(), createDto.getOrgId());

        return entity.getId();
    }

    /**
     * 更新
     * @param updateDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateData(FlowTaskConfigUpdateDto updateDto) {
        TbFlowTaskConfig config = this.getById(updateDto.getId());
        AssertUtil.notNull(config, "任务配置不存在");
        BeanUtils.copyProperties(updateDto, config);
        this.updateById(config);

        //更新任务对象
        flowTaskInfoService.updateList(config.getId(),updateDto.getTaskInfoList());

        //修改流程和组织的中间表
        flowTaskOrgService.updateFlowOrg(updateDto.getId(), updateDto.getOrgId());
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    public FlowTaskConfigVo queryForDetail(Long id) {
        FlowTaskConfigVo flowTaskConfigVo = new FlowTaskConfigVo();
        TbFlowTaskConfig config = this.getById(id);
        if(config != null){
            BeanUtils.copyProperties(config, flowTaskConfigVo);
            //任务配置的对象信息
            List<FlowTaskInfoVo> flowTaskInfoVos = flowTaskInfoService.queryByTaskConfigId(id);
            if(CollectionUtils.isNotEmpty(flowTaskInfoVos)){
                //根据sort排序
                flowTaskInfoVos = flowTaskInfoVos.stream().sorted((e1, e2) -> e1.getSort().compareTo(e2.getSort())).collect(Collectors.toList());

                List<Long> taskMenuIds = flowTaskInfoVos.stream().map(FlowTaskInfoVo::getFormId).distinct().collect(Collectors.toList());
                flowTaskConfigVo.setTaskMenuIds(taskMenuIds);
            }
            flowTaskConfigVo.setTaskInfoList(flowTaskInfoVos);

            //查询流程和组织的中间表
            List<Long> orgIds = flowTaskOrgService.queryOrgIdsByFlowConfigId(id);
            flowTaskConfigVo.setOrgId(orgIds);
        }
        return flowTaskConfigVo;
    }
}
