package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDeviceQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDeviceService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端-海康门禁设备
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:13
 */
@Slf4j
@RestController
@RequestMapping("/hik/acs-device")
public class HaikangAcsDeviceController {


    @Autowired
    private HaikangAcsDeviceService haikangAcsDeviceService;


    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HaikangAcsDeviceVo>> queryForPage(
            @RequestBody HaikangAcsDeviceQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<HaikangAcsDeviceVo> pageData = haikangAcsDeviceService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 下拉列表查询
     */
    @RequiresPermissions
    @PostMapping("/listForSelect")
    public IdmResDTO<List<HaikangAcsDeviceVo>> listForSelect(
            @RequestBody HaikangAcsDeviceQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HaikangAcsDeviceVo> list = haikangAcsDeviceService.listForSelect(query);
        return IdmResDTO.success(list);
    }


}
