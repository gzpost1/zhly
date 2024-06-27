package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.lease.dto.contract.TbContractLeaseBackParam;
import cn.cuiot.dmp.lease.entity.TbContractLeaseBackEntity;
import cn.cuiot.dmp.lease.service.TbContractLeaseBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 退租信息
 *
 * @author Mujun
 * @since 2024-06-17
 */
@Slf4j
@RestController
@RequestMapping("/contractLeaseBack")
    public class TbContractBackController extends BaseCurdController<TbContractLeaseBackService, TbContractLeaseBackEntity, TbContractLeaseBackParam> {
}
