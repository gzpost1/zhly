package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.LabelManageTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.LabelManageTypeRspDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.LabelManageTypeDTO;
import cn.cuiot.dmp.system.application.service.LabelManageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置-初始化配置-标签管理
 *
 * @author caorui
 * @date 2024/7/1
 */
@RestController
@RequestMapping("/labelManageType")
public class LabelManageTypeController {

    @Autowired
    private LabelManageTypeService labelManageTypeService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public LabelManageTypeDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return labelManageTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 根据条件查询标签管理列表
     */
    @PostMapping("/queryForList")
    public List<LabelManageTypeRspDTO> queryForList(@RequestBody @Valid LabelManageTypeReqDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        queryDTO.setCompanyId(companyId);
        return labelManageTypeService.queryForList(queryDTO);
    }

    /**
     * 根据企业id查询标签管理列表
     */
    @PostMapping("/queryByCompany")
    public List<LabelManageTypeDTO> queryByCompany() {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        return labelManageTypeService.queryByCompany(companyId);
    }

}
