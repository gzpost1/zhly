package cn.cuiot.dmp.archive.api.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.cuiot.dmp.archive.application.param.dto.CustomerDto;
import cn.cuiot.dmp.archive.application.param.query.CustomerQuery;
import cn.cuiot.dmp.archive.application.service.CustomerService;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 【PC】客户管理
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Slf4j
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

    /**
     * 导入客户
     */
    @PostMapping(value = "importCustomers")
    public IdmResDTO importCustomers(@RequestParam("file") MultipartFile file) throws Exception {
        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<List<Object>> readList = reader.read(2);
        if(CollectionUtils.isEmpty(readList)){
            throw new BusinessException(ResultCode.INNER_ERROR, "解析内容失败");
        }
        int size = readList.size();
        List<String> headers = readList.get(0).stream().map(ite -> ite.toString())
                .collect(Collectors.toList());
        List<List<Object>> dataList = Lists.newArrayList();
        for(int i=1;i<size;i++){
            dataList.add(readList.get(i));
        }
        if(CollectionUtils.isEmpty(dataList)){
            throw new BusinessException(ResultCode.INNER_ERROR, "解析内容为空");
        }
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        List<CustomerDto> dtoList = customerService.analysisImportData(currentOrgId,headers,dataList);
        customerService.saveImportCustomers(dtoList);
        return IdmResDTO.success();
    }



}
