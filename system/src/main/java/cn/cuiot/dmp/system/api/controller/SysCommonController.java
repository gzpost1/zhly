package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseRoleReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.RoleService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.TargetDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.TargetQuery;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理-公共扩展
 *
 * @author: wuyongchong
 * @date: 2024/5/15 10:08
 */
@RestController
@RequestMapping("/sys/common")
public class SysCommonController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 根据类型获得目标数据列表
     */
    @PostMapping("/queryTargetList")
    public IdmResDTO<List<TargetDto>> queryTargetList(@RequestBody @Valid TargetQuery query) {
        List<TargetDto> list = Lists.newArrayList();
        if (NumberConst.ZERO.equals(query.getType())) {
            //查询部门
            DepartmentReqDto reqDto = new DepartmentReqDto();
            reqDto.setDeptIdList(query.getCcIds());
            List<DepartmentDto> dtoList = departmentService.lookUpDepartmentList(reqDto);
            list = Optional.ofNullable(dtoList).orElse(Lists.newArrayList()).stream()
                    .map(item -> new TargetDto(item.getId(), item.getName())).collect(
                            Collectors.toList());
        } else if (NumberConst.ONE.equals(query.getType())) {
            //查询人员
            BaseUserReqDto reqDto = new BaseUserReqDto();
            reqDto.setUserIdList(query.getCcIds());
            List<BaseUserDto> dtoList = userService.lookUpUserList(reqDto);
            list = Optional.ofNullable(dtoList).orElse(Lists.newArrayList()).stream()
                    .map(item -> new TargetDto(item.getId(), item.getName())).collect(
                            Collectors.toList());
        } else if (NumberConst.TWO.equals(query.getType())) {
            //查询角色
            BaseRoleReqDto reqDto = new BaseRoleReqDto();
            reqDto.setRoleIdList(query.getCcIds());
            List<BaseRoleDto> dtoList = roleService.lookUpRoleList(reqDto);
            list = Optional.ofNullable(dtoList).orElse(Lists.newArrayList()).stream()
                    .map(item -> new TargetDto(item.getId(), item.getRoleName())).collect(
                            Collectors.toList());
        }
        return IdmResDTO.success(list);
    }

}
