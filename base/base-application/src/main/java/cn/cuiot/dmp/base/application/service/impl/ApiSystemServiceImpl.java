package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomConfigDetailRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ApiSystemService实现
 *
 * @author: wuyongchong
 * @date: 2024/4/25 15:53
 */
@Slf4j
@Service
public class ApiSystemServiceImpl implements ApiSystemService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 查询角色
     */
    @Override
    public List<BaseRoleDto> lookUpRoleList(BaseRoleReqDto query) {
        try {
            IdmResDTO<List<BaseRoleDto>> idmResDTO = systemApiFeignService
                    .lookUpRoleList(query);
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
            log.info("ApiSystemServiceImpl==lookUpRoleList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

    /**
     * 查询部门
     */
    @Override
    public List<DepartmentDto> lookUpDepartmentList(DepartmentReqDto query) {
        try {
            IdmResDTO<List<DepartmentDto>> idmResDTO = systemApiFeignService
                    .lookUpDepartmentList(query);
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
            log.info("ApiSystemServiceImpl==lookUpDepartmentList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

    /**
     * 查询用户
     */
    @Override
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

    /**
     * 获取用户信息
     */
    @Override
    public BaseUserDto lookUpUserInfo(BaseUserReqDto query) {
        try {
            IdmResDTO<BaseUserDto> idmResDTO = systemApiFeignService
                    .lookUpUserInfo(query);
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
            log.info("ApiSystemServiceImpl==lookUpUserInfo==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

    @Override
    public DepartmentDto lookUpDepartmentInfo(Long deptId, Long userId, Long orgId) {
        try {
            IdmResDTO<DepartmentDto> idmResDTO = systemApiFeignService
                    .lookUpDepartmentInfo(deptId, userId, orgId);
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
            log.info("ApiSystemServiceImpl==lookUpDepartmentInfo==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

    /**
     * 查询子部门
     */
    @Override
    public List<DepartmentDto> lookUpDepartmentChildList(DepartmentReqDto query) {
        try {
            IdmResDTO<List<DepartmentDto>> idmResDTO = systemApiFeignService
                    .lookUpDepartmentChildList(query);
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
            log.info("ApiSystemServiceImpl==lookUpDepartmentChildList==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

    @Override
    public List<DepartmentTreeRspDTO> lookUpDepartmentTree(Long orgId, Long userId) {
        try {
            IdmResDTO<List<DepartmentTreeRspDTO>> idmResDTO = systemApiFeignService
                    .lookUpDepartmentTree(orgId, userId);
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
            log.info("ApiSystemServiceImpl==lookUpDepartmentTree==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }


    @Override
    public List<BusinessTypeRspDTO> batchGetBusinessType(BusinessTypeReqDTO businessTypeReqDTO) {
        try {
            IdmResDTO<List<BusinessTypeRspDTO>> idmResDTO = systemApiFeignService
                    .batchGetBusinessType(businessTypeReqDTO);
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
            log.info("ApiSystemServiceImpl==batchGetBusinessType==fail", ex);
            throw new BusinessException(ResultCode.QUERY_BUSINESS_TYPE_ERROR);
        }
    }

    @Override
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

    @Override
    public List<CustomConfigDetailRspDTO> batchQueryCustomConfigDetails(CustomConfigDetailReqDTO customConfigDetailReqDTO) {
        try {
            IdmResDTO<List<CustomConfigDetailRspDTO>> idmResDTO = systemApiFeignService
                    .batchQueryCustomConfigDetails(customConfigDetailReqDTO);
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
            log.info("ApiSystemServiceImpl==batchQueryCustomConfigDetails==fail", ex);
            throw new BusinessException(ResultCode.QUERY_CUSTOM_CONFIG_DETAIL_ERROR);
        }
    }

}
