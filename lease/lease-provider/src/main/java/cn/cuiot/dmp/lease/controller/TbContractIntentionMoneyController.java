package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.lease.dto.contract.TbContractIntentionMoneyParam;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import cn.cuiot.dmp.lease.service.TbContractIntentionMoneyService;
import cn.cuiot.dmp.lease.entity.TbContractIntentionMoneyEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 意向金
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Slf4j
@RestController
@RequestMapping("/contractIntentionMoney")
    public class TbContractIntentionMoneyController extends BaseCurdController<TbContractIntentionMoneyService,TbContractIntentionMoneyEntity, TbContractIntentionMoneyParam> {
}
