package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerPageDto;
import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerQuery;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收费管理-实收管理-押金实收
 * @Description 押金实收
 * @Date 2024/6/17 11:51
 * @Created by libo
 */
@RestController
@RequestMapping("/securitydepositReceived")
public class SecuritydepositReceivedController {

    @Autowired
    private TbSecuritydepositManagerService securitydepositManagerService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<SecuritydepositManagerPageDto>> queryForPage(@RequestBody SecuritydepositManagerQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        query.setSelectReceived(EntityConstants.YES);
        IPage<SecuritydepositManagerPageDto> chargeManagerPageDtoIPage = securitydepositManagerService.queryForPage(query);
        //todo 房屋信息 客户信息
        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }

}
