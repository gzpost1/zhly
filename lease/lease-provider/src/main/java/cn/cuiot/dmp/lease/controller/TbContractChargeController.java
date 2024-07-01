package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.lease.dto.contract.TbContractChargeParam;
import cn.cuiot.dmp.lease.entity.TbContractChargeEntity;
import cn.cuiot.dmp.lease.service.TbContractChargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 费用条款
 *
 * @author Mujun
 * @since 2024-06-24
 */
@Slf4j
@RestController
@RequestMapping("/contractCharge")
    public class TbContractChargeController extends BaseCurdController<TbContractChargeService,TbContractChargeEntity, TbContractChargeParam> {
}
