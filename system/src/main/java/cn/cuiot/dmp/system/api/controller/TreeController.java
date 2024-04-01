package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.service.TreeService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeResDto;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jz
 * @classname TreeController
 * @description 树管理
 * @date 2023/04/07
 */
@RestController
@RequestMapping("/tree")
@Slf4j
public class TreeController extends BaseController {

    @Autowired
    private TreeService treeService;

    /**
     * 组织树查询
     *
     * @param deptTreeReqDto 组织树查询请求dto
     * @return 组织树
     */
    @PostMapping(value = "/getDepartmentTree", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeptTreeResDto> getDepartmentTree(@RequestBody @Valid DeptTreeReqDto deptTreeReqDto) {
        deptTreeReqDto.setOrgId(getOrgId());
        deptTreeReqDto.setUserId(getUserId());
        return treeService.getDeptTree(deptTreeReqDto);
    }

    /**
     * 空间树查询
     *
     * @param spaceTreeReqDto 空间树查询请求dto
     * @return 空间树
     */
    @PostMapping(value = "/getSpaceTree", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SpaceTreeResDto> getSpaceTree(@RequestBody @Valid SpaceTreeReqDto spaceTreeReqDto) {
        spaceTreeReqDto.setOrgId(getOrgId());
        spaceTreeReqDto.setUserId(getUserId());
        return treeService.getSpaceTree(spaceTreeReqDto);
    }

}
