package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorControlDto;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDoorService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端-海康门禁点
 *
 * @author: wuyongchong
 * @date: 2024/10/10 15:56
 */
@Slf4j
@RestController
@RequestMapping("/hik/acs-door")
public class HaikangAcsDoorController {

    @Autowired
    private HaikangAcsDoorService haikangAcsDoorService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HaikangAcsDoorVo>> queryForPage(
            @RequestBody HaikangAcsDoorQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<HaikangAcsDoorVo> pageData = haikangAcsDoorService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 下拉列表查询
     */
    @RequiresPermissions
    @PostMapping("/listForSelect")
    public IdmResDTO<List<HaikangAcsDoorVo>> listForSelect(
            @RequestBody HaikangAcsDoorQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HaikangAcsDoorVo> list = haikangAcsDoorService.listForSelect(query);
        return IdmResDTO.success(list);
    }

    /**
     * 门禁点反控
     */
    @RequiresPermissions
    @PostMapping("/dooControlDoor")
    public IdmResDTO dooControlDoor(@RequestBody @Valid HaikangAcsDoorControlDto dto) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        dto.setCompanyId(currentOrgId);
        haikangAcsDoorService.dooControlDoor(dto);
        return IdmResDTO.success();
    }

}
