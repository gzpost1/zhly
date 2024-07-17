package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertSonDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentTreeVO;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 组织管理
 *
 * @author wuyongchong
 * @date 2024/5/6
 */
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 查询组织树
     */
    @GetMapping(value = "/getDepartmentTree", produces = "application/json;charset=UTF-8")
    public List<DepartmentTreeVO> getDepartmentTree(
            @RequestParam(value = "type", required = false) String type) {
        String orgId = getOrgId();
        String userId = getUserId();
        return departmentService.getDepartmentTree(orgId, userId, type);
    }

    /**
     * 组织管理查询组织树(懒加载)
     */
    @GetMapping(value = "/manage/getDepartmentTreeLazy", produces = "application/json;charset=UTF-8")
    public List<GetDepartmentTreeLazyResDto> manageGetDepartmentTreeLazy(
            @Valid GetDepartmentTreeLazyReqDto getDepartmentTreeLazyReqDto) {
        getDepartmentTreeLazyReqDto.setLoginUserId(getUserId());
        getDepartmentTreeLazyReqDto.setLoginOrgId(getOrgId());
        return departmentService.manageGetDepartmentTreeLazy(getDepartmentTreeLazyReqDto);
    }

    /**
     * 新增组织
     */
    @RequiresPermissions
    @LogRecord(operationCode = "insertSonDepartment", operationName = "添加组织", serviceType = "department",serviceTypeName = "组织管理")
    @PostMapping(value = "/insertSonDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long insertSonDepartment(@RequestBody @Valid InsertSonDepartmentDto dto) {
        dto.setPkOrgId(Long.parseLong(getOrgId()));
        dto.setCreateBy(getUserId());
        dto.setUserId(getUserId());
        Long deptId = departmentService.insertSonDepartment(dto);

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("组织")
                .targetDatas(Lists.newArrayList(new OptTargetData(dto.getDepartmentName(),deptId.toString())))
                .build());

        return deptId;
    }

    /**
     * 修改组织
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateDepartment", operationName = "修改组织", serviceType = "department",serviceTypeName = "组织管理")
    @PostMapping(value = "/updateDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public int updateDepartment(@RequestBody @Valid UpdateDepartmentDto dto) {
        String orgId = getOrgId();
        dto.setPkOrgId(Long.parseLong(orgId));
        dto.setOrgId(orgId);
        dto.setUserId(getUserId());

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("组织")
                .targetDatas(Lists.newArrayList(new OptTargetData(dto.getDepartmentName(),dto.getId().toString())))
                .build());

        return departmentService.updateDepartment(dto);
    }

    /**
     * 删除组织
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteDepartment", operationName = "删除组织", serviceType = "department",serviceTypeName = "组织管理")
    @GetMapping(value = "/deleteDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDepartment(Long id) {
        UpdateDepartmentDto updateDepartmentDto = new UpdateDepartmentDto();
        String orgId = getOrgId();
        updateDepartmentDto.setId(id);
        updateDepartmentDto.setOrgId(orgId);
        updateDepartmentDto.setUserId(getUserId());

        DepartmentEntity departmentEntity = departmentService.getDeptById(id.toString());
        if(Objects.isNull(departmentEntity)){
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST,"组织不存在");
        }
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("组织")
                .targetDatas(Lists.newArrayList(new OptTargetData(departmentEntity.getDepartmentName(),departmentEntity.getId().toString())))
                .build());

        departmentService.deleteDepartment(updateDepartmentDto);
    }

    /**
     * 用户组织树搜索
     */
    @GetMapping(value = "/getUserDepartmentTreeLazyByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetDepartmentTreeLazyByNameResDto> getUserDepartmentTreeLazyByName(
            @Valid GetDepartmentTreeLazyByNameReqDto dto) {
        dto.setUserId(getUserId());
        dto.setOrgId(getOrgId());
        //前端要求传list
        return Arrays.asList(departmentService.getUserDepartmentTreeLazyByName(dto));
    }

    /**
     * 空间树（全路径到到小区/区域级）
     */
    @GetMapping(value = "/getAllSpaceTree", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DepartmentTreeVO> getAllSpaceTree() {

        return departmentService.getAllSpaceTree(getOrgId(), getUserId());
    }

    /**
     * 根据类型获取部门或用户
     */
    @GetMapping(value = "/getDepartmentOrUserByType", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DepartmentUserVO> getDepartmentOrUserByType(
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "type") String type) {
        String orgId = getOrgId();
        return departmentService.getDepartmentOrUserByType(Long.valueOf(orgId), deptId, type);
    }

}
