package cn.cuiot.dmp.baseconfig.flow.feign;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description 流程配置请求系统服务
 * @Date 2024/5/10 15:41
 * @Created by libo
 */
@Slf4j
@Service
public class SystemToFlowService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 查询用户ID根据role
     * @return
     */
    public List<Long> getUserIdByRole(List<Long> roleIdList){
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setRoleIdList(roleIdList);
        List<BaseUserDto> baseUserDtoList = Optional.ofNullable(lookUpUserList(baseUserReqDto)).orElse(Lists.newArrayList());
        return baseUserDtoList.stream().map(BaseUserDto::getId).collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询用户ID根据部门ID
     */
    public List<Long> getUserIdByDept(List<Long> deptIdList){
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setDeptIdList(deptIdList);
        List<BaseUserDto> baseUserDtoList = Optional.ofNullable(lookUpUserList(baseUserReqDto)).orElse(Lists.newArrayList());
        return baseUserDtoList.stream().map(BaseUserDto::getId).collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询用户
     */
    public List<BaseUserDto> lookUpUserList(BaseUserReqDto query) {
        try {
            IdmResDTO<List<BaseUserDto>> idmResDTO = systemApiFeignService
                    .lookUpUserList(query);
            if (Objects.nonNull(idmResDTO) && ResultCode.SUCCESS.getCode()
                    .equals(idmResDTO.getCode())) {
                return idmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(idmResDTO)) {
                message = idmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiSystemServiceImpl==lookUpUserList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }
}
