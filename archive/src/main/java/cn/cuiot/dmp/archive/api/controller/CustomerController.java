package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.CustomerDto;
import cn.cuiot.dmp.archive.application.param.query.CustomerQuery;
import cn.cuiot.dmp.archive.application.service.CustomerService;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【PC】客户管理
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<CustomerVo>> queryForPage(@RequestBody CustomerQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<CustomerVo> pageData = customerService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 获取详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<CustomerVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        CustomerVo data = customerService.queryForDetail(idParam.getId(), currentOrgId);
        return IdmResDTO.success(data);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createCustomer", operationName = "创建客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid CustomerDto dto) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        customerService.createCustomer(dto);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCustomer", operationName = "修改客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid CustomerDto dto) {
        if (Objects.isNull(dto.getId())) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "ID不能为空");
        }
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        customerService.updateCustomer(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteCustomer", operationName = "删除客户", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        customerService.deleteCustomer(deleteParam.getId(), currentOrgId);
        return IdmResDTO.success();
    }

    /**
     * 修改状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCustomerStatus", operationName = "修改客户状态", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        customerService.updateStatus(updateStatusParam.getId(), updateStatusParam.getStatus(),
                currentOrgId);
        return IdmResDTO.success();
    }

}
