package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkInfoMapper;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/27 16:50
 */
@Service
@Slf4j
public class AppWorkInfoService extends ServiceImpl<WorkInfoMapper, WorkInfoEntity> {

    /**
     * APP 获取待审批的数据
     * @param query
     * @return
     */
    public IdmResDTO<BaseDto> queryMyNotApprocalCount (PendingProcessQuery query){
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        BaseDto dto = getBaseMapper().queryMyNotApprocalCount(query);
        return IdmResDTO.success(dto);

    }

    /**
     * 获取待处理的列表
     * @param query
     * @return
     */
    public IdmResDTO<List<BaseDto>> queryPendProcessList(PendingProcessQuery query) {
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        List<BaseDto> dtos = getBaseMapper().queryPendProcessList(query);
        return IdmResDTO.success(dtos);
    }
}
