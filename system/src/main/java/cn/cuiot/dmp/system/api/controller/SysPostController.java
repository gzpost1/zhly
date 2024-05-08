package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.command.SysPostCmd;
import cn.cuiot.dmp.system.application.service.SysPostService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysPostQuery;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysPostEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理-岗位管理
 *
 * @author: wuyongchong
 * @date: 2024/5/6 9:33
 */
@RestController
@RequestMapping("/sys/post")
public class SysPostController {

    @Autowired
    private SysPostService sysPostService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<SysPostEntity>> queryForPage(@RequestBody SysPostQuery query) {
        query.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        IPage<SysPostEntity> pageResult = sysPostService.queryForPage(query);
        return IdmResDTO.success(pageResult);
    }

    /**
     * 列表查询
     */
    @PostMapping("/queryForList")
    public IdmResDTO<List<SysPostEntity>> queryForList(@RequestBody SysPostQuery query) {
        query.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        List<SysPostEntity> list = sysPostService.queryForList(query);
        return IdmResDTO.success(list);
    }

    /**
     * 获取详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<SysPostEntity> queryForDetail(@RequestBody @Valid IdParam param) {
        SysPostEntity data = sysPostService.queryForDetail(param);
        return IdmResDTO.success(data);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid SysPostCmd cmd) {
        AssertUtil
                .isFalse(sysPostService
                                .nameExists(null, cmd.getPostName(),
                                        LoginInfoHolder.getCurrentOrgId()),
                        "岗位名称已存在");
        cmd.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        sysPostService.create(cmd);
        return IdmResDTO.success(null);
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid SysPostCmd cmd) {
        AssertUtil.isTrue(Objects.nonNull(cmd.getId()), "主键ID不能为空");
        AssertUtil
                .isFalse(sysPostService
                                .nameExists(cmd.getId(), cmd.getPostName(),
                                        LoginInfoHolder.getCurrentOrgId()),
                        "岗位名称已存在");
        cmd.setSessionOrgId(LoginInfoHolder.getCurrentOrgId());
        sysPostService.update(cmd);
        return IdmResDTO.success(null);
    }

    /**
     * 启停用
     */
    @RequiresPermissions
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        Long sessionUserId = LoginInfoHolder.getCurrentUserId();
        Long sessionOrgId = LoginInfoHolder.getCurrentOrgId();
        sysPostService.updateStatus(updateStatusParam, sessionUserId, sessionOrgId);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        Long sessionUserId = LoginInfoHolder.getCurrentUserId();
        Long sessionOrgId = LoginInfoHolder.getCurrentOrgId();
        sysPostService.delete(idParam, sessionUserId, sessionOrgId);
        return IdmResDTO.success();
    }

}
