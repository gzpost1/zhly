package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertSonDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentTreeVO;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zjb
 * @classname DepartmentController
 * @description 组织管理
 * @date 2022/4/8
 */
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 查询组织树
     *
     * @return
     */
    @RequiresPermissions("system:org:control")
    @GetMapping(value = "/getDepartmentTree", produces = "application/json;charset=UTF-8")
    public List<DepartmentTreeVO> getDepartmentTree(@RequestParam(value = "type", required = false) String type) {
        String orgId = getOrgId();
        String userId = getUserId();
        return departmentService.getDepartmentTree(orgId, userId, type);
    }

    /**
     * 组织管理查询组织树(懒加载)
     *
     * @return
     */
    @RequiresPermissions("system:org:control")
    @GetMapping(value = "/manage/getDepartmentTreeLazy", produces = "application/json;charset=UTF-8")
    public List<GetDepartmentTreeLazyResDto> manageGetDepartmentTreeLazy(@Valid GetDepartmentTreeLazyReqDto getDepartmentTreeLazyReqDto) {
        getDepartmentTreeLazyReqDto.setLoginUserId(getUserId());
        getDepartmentTreeLazyReqDto.setLoginOrgId(getOrgId());
        return departmentService.manageGetDepartmentTreeLazy(getDepartmentTreeLazyReqDto);
    }

    /**
     * 新增组织(根节点)
     *
     * @param dto
     * @return
     */
    @RequiresPermissions("system:org:control")
    @Deprecated
    @PostMapping(value = "/insertDepartment", produces = "application/json;charset=UTF-8")
    public Long insertSite(@RequestBody @Valid InsertDepartmentDto dto) {
        ValidateUtil.validate(dto);
        dto.setPkOrgId(Long.parseLong(getOrgId()));
        dto.setCreateBy(getUserName());
        return departmentService.insertDepartment(dto);
    }

    @RequiresPermissions("system:org:add")
    @LogRecord(operationCode = "insertSonDepartment", operationName = "新增子组织", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/insertSonDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long insertSonDepartment(@RequestBody @Valid InsertSonDepartmentDto dto) {
        dto.setPkOrgId(Long.parseLong(getOrgId()));
        String userName = getUserName();
        dto.setCreateBy(userName);
        dto.setUserId(getUserId());
        return departmentService.insertSonDepartment(dto);
    }

    @RequiresPermissions("system:org:edit")
    @LogRecord(operationCode = "updateDepartment", operationName = "修改子组织", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/updateDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public int updateDepartment(@RequestBody @Valid UpdateDepartmentDto dto) {
        String orgId = getOrgId();
        dto.setPkOrgId(Long.parseLong(orgId));
        dto.setOrgId(orgId);
        dto.setUserId(getUserId());
        return departmentService.updateDepartment(dto);
    }

    /**
     * 删除组织
     *
     * @param id
     */
    @RequiresPermissions("system:org:delete")
    @GetMapping(value = "/deleteDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDepartment(Long id) {
        UpdateDepartmentDto updateDepartmentDto = new UpdateDepartmentDto();
        String orgId = getOrgId();
        updateDepartmentDto.setId(id);
        updateDepartmentDto.setOrgId(orgId);
        updateDepartmentDto.setUserId(getUserId());
        departmentService.deleteDepartment(updateDepartmentDto);
    }

    /**
     * 用户组织树搜索
     *
     * @param dto
     * @return
     */
    @GetMapping(value = "/getUserDepartmentTreeLazyByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetDepartmentTreeLazyByNameResDto> getUserDepartmentTreeLazyByName(@Valid GetDepartmentTreeLazyByNameReqDto dto) {
        dto.setUserId(getUserId());
        dto.setOrgId(getOrgId());
        //前端要求传list
        return Arrays.asList(departmentService.getUserDepartmentTreeLazyByName(dto));
    }

    /**
     * 空间树（全路径到到小区/区域级）
     *
     * @return
     */
    @GetMapping(value = "/getAllSpaceTree", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DepartmentTreeVO> getAllSpaceTree() {

        return departmentService.getAllSpaceTree(getOrgId(), getUserId());
    }

}
