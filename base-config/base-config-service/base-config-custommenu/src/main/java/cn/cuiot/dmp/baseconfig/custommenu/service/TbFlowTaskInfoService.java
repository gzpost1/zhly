package cn.cuiot.dmp.baseconfig.custommenu.service;

import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoInsertDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoUpdateDto;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskInfo;
import cn.cuiot.dmp.baseconfig.custommenu.mapper.TbFlowTaskInfoMapper;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskInfoVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TbFlowTaskInfoService extends ServiceImpl<TbFlowTaskInfoMapper, TbFlowTaskInfo> {


    /**
     * 根据任务配置删除任务对象
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTaskConfigIds(List<Long> ids) {
        LambdaUpdateWrapper<TbFlowTaskInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(TbFlowTaskInfo::getTaskConfigId, ids);
        this.remove(updateWrapper);
    }

    /**
     * 创建任务对象
     *
     * @param id
     * @param taskInfoList
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(Long id, List<FlowTaskInfoInsertDto> taskInfoList) {
        for (int i = 0; i < taskInfoList.size(); i++) {
            taskInfoList.get(i).setSort(i);
        }

        List<TbFlowTaskInfo> insertList = taskInfoList.stream().map(taskInfo -> {
            TbFlowTaskInfo tbFlowTaskInfo = new TbFlowTaskInfo();
            BeanUtils.copyProperties(taskInfo, tbFlowTaskInfo);
            tbFlowTaskInfo.setTaskConfigId(id);
            tbFlowTaskInfo.setId(IdWorker.getId());
            return tbFlowTaskInfo;
        }).collect(Collectors.toList());

        baseMapper.insertList(insertList);
    }

    /**
     * 更新任务对象
     *
     * @param id
     * @param taskInfoList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateList(Long id, List<FlowTaskInfoUpdateDto> taskInfoList) {
        for (int i = 0; i < taskInfoList.size(); i++) {
            taskInfoList.get(i).setSort(i);
        }

        //首先删除数据
        List<FlowTaskInfoUpdateDto> existIds = taskInfoList.stream().filter(e -> Objects.nonNull(e.getId())).collect(Collectors.toList());
        if (!existIds.isEmpty()) {
            List<Long> ids = existIds.stream().map(FlowTaskInfoUpdateDto::getId).collect(Collectors.toList());
            LambdaUpdateWrapper<TbFlowTaskInfo> updateWrapper = new LambdaUpdateWrapper<>();
//            updateWrapper.in(TbFlowTaskInfo::getId, ids);
            updateWrapper.eq(TbFlowTaskInfo::getTaskConfigId,id);
            this.remove(updateWrapper);
        }
        //插入数据
        List<TbFlowTaskInfo> insertList = taskInfoList.stream().map(taskInfo -> {
            TbFlowTaskInfo tbFlowTaskInfo = new TbFlowTaskInfo();
            BeanUtils.copyProperties(taskInfo, tbFlowTaskInfo);
            tbFlowTaskInfo.setTaskConfigId(id);
            if(Objects.isNull(tbFlowTaskInfo.getId())){
                tbFlowTaskInfo.setId(IdWorker.getId());
            }
            return tbFlowTaskInfo;
        }).collect(Collectors.toList());

        this.saveOrUpdateBatch(insertList);
    }

    /**
     * 根据任务配置id查询任务对象
     * @param id
     * @return
     */
    public List<FlowTaskInfoVo> queryByTaskConfigId(Long id) {
        return baseMapper.queryByTaskConfigId(id);
    }
}
