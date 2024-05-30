package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.AppWorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.QueryTaskUserInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.TaskUserInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkInfoMapper;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @create 2024/5/27 16:50
 */
@Service
@Slf4j
public class AppWorkInfoService extends ServiceImpl<WorkInfoMapper, WorkInfoEntity> {


    @Autowired
    private SystemApiFeignService systemApiFeignService;
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

    /**
     * 获取转交时，任务原有的
     * @param dto
     * @return
     */

    public IdmResDTO<List<TaskUserInfoDto>> queryTaskUserInfo(QueryTaskUserInfoDto dto) {

        List<TaskUserInfoDto> users = getBaseMapper().queryTaskUserInfo(dto);
        if(CollectionUtil.isEmpty(users)){
            return IdmResDTO.success();
        }
        List<Long> userIds = users.stream().filter(e -> Objects.nonNull(e.getUserId())).
                map(TaskUserInfoDto::getUserId).collect(Collectors.toList());
        Map<Long, String> userMap = getUserMap(userIds);
        users.stream().forEach(item->{
            item.setUserName(userMap.get(item.getUserId()));
        });

        return IdmResDTO.success(users);
    }

    public Map<Long,String> getUserMap(List<Long> userIds){
        BaseUserReqDto userReqDto = new BaseUserReqDto();
        userReqDto.setUserIdList(userIds);
        IdmResDTO<List<BaseUserDto>> listIdmResDTO = systemApiFeignService.lookUpUserList(userReqDto);
        List<BaseUserDto> data = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("用户信息不存在"));
        return data.stream().collect(Collectors.toMap(BaseUserDto::getId,BaseUserDto::getName ));
    }

    /**
     * app端获取工单信息
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<AppWorkInfoDto>> queryAppMySubmitWorkInfo(QueryMyApprovalDto dto) {
        dto.setAssignee(LoginInfoHolder.getCurrentUserId());
        Page<AppWorkInfoDto>  page = baseMapper.queryAppMySubmitWorkInfo(new Page<AppWorkInfoDto>(dto.getCurrentPage(),dto.getPageSize()),dto);

        List<AppWorkInfoDto> records = page.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return IdmResDTO.success();
        }
        List<Long> userIds = records.stream().map(AppWorkInfoDto::getCreateUser).collect(Collectors.toList());
        Map<Long, String> userMap = getUserMap(userIds);
        records.stream().forEach(item->{
            item.setCreateUserName(userMap.get(item.getCreateUser()));
        });

        return IdmResDTO.success(page);
    }
}
