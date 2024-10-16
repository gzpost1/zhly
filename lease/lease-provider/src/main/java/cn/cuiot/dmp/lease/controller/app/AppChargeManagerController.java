package cn.cuiot.dmp.lease.controller.app;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.lease.dto.charge.AppChargeManagerDto;
import cn.cuiot.dmp.lease.dto.charge.AppChargemanagerQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeHangupQueryDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeManagerDetailDto;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 房屋账单
 *
 * @author libo
 * @date 2024/6/12
 */
@RestController
@RequestMapping("/appChargeManager")
public class AppChargeManagerController {

    @Autowired
    private TbChargeManagerService tbChargeManagerService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<AppChargeManagerDto>> appChargeManager(@RequestBody AppChargemanagerQuery query) {

        IPage<AppChargeManagerDto> chargeManagerPageDtoIPage = tbChargeManagerService.appChargeManager(query);

        if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {
            chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(), AppChargeManagerDto.class);
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
    public IdmResDTO<ChargeManagerDetailDto> queryForDetail(@RequestBody @Valid IdParam idParam) {
        ChargeManagerDetailDto chargeManagerDetailDto = tbChargeManagerService.queryForDetail(idParam.getId());
        //填充客户信息
        if (Objects.nonNull(chargeManagerDetailDto)) {
            chargeInfoFillService.fillinfo(Lists.newArrayList(chargeManagerDetailDto), ChargeManagerDetailDto.class);
        }
        return IdmResDTO.success().body(chargeManagerDetailDto);
    }


    /**
     * 获取收款明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForReceivedPage")
    public IdmResDTO<IPage<TbChargeReceived>> queryForReceivedPage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        IPage<TbChargeReceived> tbChargeHangupIPage = tbChargeManagerService.queryForReceivedPage(queryDto);
        if (Objects.nonNull(tbChargeHangupIPage) && CollectionUtils.isNotEmpty(tbChargeHangupIPage.getRecords())) {
            chargeInfoFillService.fillinfo(tbChargeHangupIPage.getRecords(), TbChargeReceived.class);
        }
        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

}

