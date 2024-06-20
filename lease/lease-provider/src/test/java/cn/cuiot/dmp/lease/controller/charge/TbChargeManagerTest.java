package cn.cuiot.dmp.lease.controller.charge;


import cn.cuiot.dmp.lease.LeaseApplication;
import cn.cuiot.dmp.lease.dto.charge.ChargeManagerInsertVo;
import cn.cuiot.dmp.lease.enums.ChargeReceivedTypeEnum;
import cn.cuiot.dmp.lease.enums.ChargeTypeEnum;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 收费管理-收银台-缴费管理
 *
 * @author libo
 * @date 2024/6/12
 */
@Slf4j
@SpringBootTest(classes = LeaseApplication.class)
public class TbChargeManagerTest {

    @Autowired
    private TbChargeManagerService tbChargeManagerService;

    @Test
    public void testCreate() {
        ChargeManagerInsertVo createDto = new ChargeManagerInsertVo();
        createDto.setHouseId(1L);
        createDto.setChargeType(ChargeReceivedTypeEnum.NATURAL_MONTH_CYCLE.getCode());
        createDto.setOwnershipPeriodBegin(DateUtil.parse("2024-06-12 12:00:00"));
        createDto.setOwnershipPeriodEnd(DateUtil.parse("2024-02-12 00:12:00"));
        createDto.setDueDateNum(31);

        tbChargeManagerService.saveData(createDto, ChargeTypeEnum.MANUAL_CREATE.getCode(), null);

    }

}

