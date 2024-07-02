package cn.cuiot.dmp.lease.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatusVo;
import cn.cuiot.dmp.lease.service.TbContractBindInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 内部服务接口
 *
 * @author Mujun
 * @Description
 * @data 2024/06/19 14:26
 */
@InternalApi
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private TbContractBindInfoService bindInfoService;

    /**
     * 根据房屋id获取意向合同和租赁合同信息
     */
    @PostMapping("/queryConctactStatusByHouseIds")
    public ContractStatusVo queryConctactStatusByHouseIds(@RequestBody @Valid IdsReq idsReq) {
        return bindInfoService.queryConctactStatusByHouseIds(idsReq.getIds());
    }

}
