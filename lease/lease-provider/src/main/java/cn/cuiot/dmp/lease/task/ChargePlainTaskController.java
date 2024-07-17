package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chargeplaintask")
public class ChargePlainTaskController {
    @Autowired
    private ChargePlainTask chargePlainTask;


    @PostMapping("/createDayWork")
    public IdmResDTO createDayWork(@RequestBody IdParam idParam) {

        chargePlainTask.createDayWork(null);

        return IdmResDTO.success();
    }

    @PostMapping("/createMonthWork")
    public IdmResDTO createMonthWork(@RequestBody IdParam idParam) {

        chargePlainTask.createMonthWork(null);

        return IdmResDTO.success();
    }
}
