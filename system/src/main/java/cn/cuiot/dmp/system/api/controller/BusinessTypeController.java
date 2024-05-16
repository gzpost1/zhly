package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;
import cn.cuiot.dmp.system.application.service.BusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 企业账号-系统配置-初始化配置-业务类型
 *
 * @author caorui
 * @date 2024/4/26
 */
@RestController
@RequestMapping("/businessType")
public class BusinessTypeController {

    @Autowired
    private BusinessTypeService businessTypeService;

    /**
     * 根据id获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public BusinessTypeVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return businessTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 编辑时查询类型列表（排除当前节点）
     */
    @RequiresPermissions
    @PostMapping("/queryExcludeChild")
    public List<BusinessTypeTreeNodeVO> queryExcludeChild(@RequestBody @Valid BusinessTypeQueryDTO queryDTO) {
        return businessTypeService.queryExcludeChild(queryDTO);
    }

    /**
     * 根据条件查询企业的业务类型详情
     */
    @RequiresPermissions
    @PostMapping("/queryByCompany")
    public List<BusinessTypeTreeNodeVO> queryByCompany(@RequestBody @Valid BusinessTypeQueryDTO queryDTO) {
        return businessTypeService.queryByCompany(queryDTO);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public int create(@RequestBody @Valid BusinessTypeCreateDTO businessTypeCreateDTO) {
        return businessTypeService.saveBusinessType(businessTypeCreateDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateBusinessType", operationName = "更新业务类型", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int update(@RequestBody @Valid BusinessTypeUpdateDTO businessTypeUpdateDTO) {
        return businessTypeService.updateBusinessType(businessTypeUpdateDTO);
    }

    /**
     * 删除预校验
     */
    @RequiresPermissions
    @PostMapping("/checkBeforeDelete")
    public void checkBeforeDelete(@RequestBody @Valid IdParam idParam) {
        businessTypeService.checkDeleteStatus(idParam.getId());
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteBusinessType", operationName = "删除业务类型", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int delete(@RequestBody @Valid BusinessTypeQueryDTO queryDTO) {
        return businessTypeService.deleteBusinessType(queryDTO);
    }

}
