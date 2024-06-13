package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.ChargeManagerPageDto;
import cn.cuiot.dmp.lease.dto.charge.PaidInManageMentQuery;
import cn.cuiot.dmp.lease.dto.charge.TbChargeManagerQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.service.charge.TbChargeReceivedService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实收管理
 * @Description 实收管理
 * @Date 2024/6/13 15:16
 * @Created by libo
 */
@RestController
@RequestMapping("/paidInManageMent")
public class PaidInManageMentController {
    @Autowired
    private TbChargeReceivedService tbChargeReceivedService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbChargeReceived>> queryForPaidinPage(@RequestBody PaidInManageMentQuery query) {
        AssertUtil.notNull(query.getHouseId(), "房屋id不能为空");

        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<TbChargeReceived> chargeManagerPageDtoIPage = tbChargeReceivedService.queryForPaidinPage(query);
        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }
}
