package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.lease.dto.contract.TbContractLeaseParam;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.TbContractLeaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 租赁合同
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Slf4j
@RestController
@RequestMapping("/contractLease")
    public class TbContractLeaseController extends BaseCurdController<TbContractLeaseService,TbContractLeaseEntity, TbContractLeaseParam> {

}
