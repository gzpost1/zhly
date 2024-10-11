package cn.cuiot.dmp.lease.controller.app;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositRefund;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 押金管理
 *
 * @Description 押金管理
 * @Date 2024/6/14 10:26
 * @Created by libo
 */
@RestController
@RequestMapping("/appsecuritydepositManager")
public class AppSecuritydepositManagerController {

    @Autowired
    private TbSecuritydepositManagerService securitydepositManagerService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;


    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<SecuritydepositManagerPageDto>> queryForPage(@RequestBody SecuritydepositManagerQuery query) {
        IPage<SecuritydepositManagerPageDto> chargeManagerPageDtoIPage = securitydepositManagerService.queryForPage(query);

        if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {
            chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(), SecuritydepositManagerPageDto.class);
        }

        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }

    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<SecuritydepositManagerDto> queryForDetail(@RequestBody @Valid IdParam idParam) {
        SecuritydepositManagerDto securitydepositManagerDto = securitydepositManagerService.queryForDetail(idParam.getId());
        if(Objects.nonNull(securitydepositManagerDto)){
            chargeInfoFillService.fillinfo(Lists.newArrayList(securitydepositManagerDto),SecuritydepositManagerDto.class);
            chargeInfoFillService.fillinfo(Lists.newArrayList(securitydepositManagerDto.getSecuritydepositRefundList()), TbSecuritydepositRefund.class);

        }
        return IdmResDTO.success().body(securitydepositManagerDto);
    }
}
