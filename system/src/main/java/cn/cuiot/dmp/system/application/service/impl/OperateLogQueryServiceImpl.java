package cn.cuiot.dmp.system.application.service.impl;


import cn.cuiot.dmp.common.constant.Constant;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.OperateLogQueryService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.OperationLogEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bean.OperationLogBean;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OperationLogDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PlatformLogResDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OperationLogDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


/**
 * @Author 26432
 * @Date 2021/12/8   17:32
 */
@Slf4j
@Service
public class OperateLogQueryServiceImpl extends BaseController implements OperateLogQueryService {

        /**
         * root
         */
        private static final String ROOT_USER_ID = "1";

        @Autowired
        private OperationLogDao operationLogDao;

        @Autowired
        private UserService userService;

        @Autowired
        private DepartmentService departmentService;

        @Override
        public PlatformLogResDTO listLogs(OperationLogBean operationLogBean) {
                String orgId = getOrgId();
                String userId = getUserId();
                Long deptId = Optional.ofNullable(userService.getDeptId(userId, orgId)).map(Long::valueOf).orElse(null);
                if (deptId == null) {
                        throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
                }
                DepartmentEntity departmentEntity = departmentService.getDeptById(String.valueOf(deptId));
                Page<OperationLogEntity> page =
                        PageHelper.startPage(operationLogBean.getPageNum(), operationLogBean.getPageSize());
                try {
                        if (!ROOT_USER_ID.equals(userId)) {
                                operationLogBean.setOrgId(orgId);
                                if (departmentEntity != null) {
                                        operationLogBean.setPath(departmentEntity.getPath());
                                }
                        }
                        operationLogDao.listLogs(operationLogBean);
                } catch (Exception e) {
                        throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, e);
                }

                PageInfo<OperationLogEntity> pageInfo = page.toPageInfo();
                List<OperationLogEntity> logDetails = pageInfo.getList();
                List<OperationLogDto> operationLogDtoList = logDetails.stream().map(optLogEntity -> {
                        OperationLogDto operationLogDTO = new OperationLogDto();
                        BeanUtils.copyProperties(optLogEntity, operationLogDTO);
                        return operationLogDTO;
                }).collect(Collectors.toList());
                PlatformLogResDTO.DataDTO dataDTO =
                        PlatformLogResDTO.DataDTO.builder().counts(page.getTotal()).logDetail(operationLogDtoList).build();
                return PlatformLogResDTO.builder().code(Constant.SUCCESS_CODE).message(HttpStatus.OK.name())
                        .data(dataDTO).build();
        }
}
