package cn.cuiot.dmp.lease.feign;

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description 系统信息查询调用
 * @Date 2024/5/10 15:41
 * @Created by libo
 */
@Slf4j
@Service
public class SystemToFlowService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;
    @Autowired
    private ArchiveFeignService archiveFeignService;

    /**
     * 查询用户ID根据role
     * @return
     */
    public List<Long> getUserIdByRole(List<Long> roleIdList,List<Long> deptIdList){
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setRoleIdList(roleIdList);
        baseUserReqDto.setDeptIdList(deptIdList);
        List<BaseUserDto> baseUserDtoList = Optional.ofNullable(lookUpUserList(baseUserReqDto)).orElse(Lists.newArrayList());
        return baseUserDtoList.stream().map(BaseUserDto::getId).collect(java.util.stream.Collectors.toList());
    }

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

    public List<FormConfigRspDTO> batchQueryFormConfig(FormConfigReqDTO formConfigReqDTO) {
        try {
            IdmResDTO<List<FormConfigRspDTO>> idmResDTO = systemApiFeignService
                    .batchQueryFormConfig(formConfigReqDTO);
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
            log.info("ApiSystemServiceImpl==batchQueryFormConfig==fail", ex);
            throw new BusinessException(ResultCode.QUERY_FORM_CONFIG_ERROR);
        }
    }

    /**
     * 批量查询表单配置-常用选项设置数据
     */
    public List<CommonOptionSettingRspDTO> batchQueryCommonOptionSetting(CommonOptionSettingReqDTO dto) {
        try {
            IdmResDTO<List<CommonOptionSettingRspDTO>> idmResDTO = systemApiFeignService
                    .batchQueryCommonOptionSetting(dto);
            if (Objects.nonNull(idmResDTO) && Objects.equals(ResultCode.SUCCESS.getCode(), idmResDTO.getCode())) {
                return idmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(idmResDTO)) {
                message = idmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiSystemServiceImpl==batchQueryCommonOptionSetting==fail", ex);
            throw new BusinessException(ResultCode.QUERY_FORM_CONFIG_ERROR);
        }
    }

    /**
     * 查询楼房选项
     */
    public List<BuildingArchive> buildingArchiveQueryForList(@RequestBody @Valid BuildingArchiveReq buildingArchiveReq){
        try {
            IdmResDTO<List<BuildingArchive>> listIdmResDTO = archiveFeignService
                    .buildingArchiveQueryForList(buildingArchiveReq);
            if (Objects.nonNull(listIdmResDTO) && Objects.equals(ResultCode.SUCCESS.getCode(), listIdmResDTO.getCode())) {
                return listIdmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(listIdmResDTO)) {
                message = listIdmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiSystemServiceImpl==buildingArchiveQueryForList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_FORM_CONFIG_ERROR);
        }
    }
}
