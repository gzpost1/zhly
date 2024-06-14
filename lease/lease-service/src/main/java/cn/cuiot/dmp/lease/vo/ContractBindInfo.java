package cn.cuiot.dmp.lease.vo;

import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.lease.entity.TbContractIntentionMoneyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

;

/**
 * 合同操作日志
 *
 * @author MJ~
 * @since 2024-06-13
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractBindInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<HousesArchivesVo> houseList;
    private List<TbContractIntentionMoneyEntity> moneyList;

    private List<Long> houseIds;
    private List<Long> moneyIds;

    public List<Long> getHouseIds() {
        if (CollectionUtils.isNotEmpty(houseList)) {
            houseIds = houseList.stream().map(HousesArchivesVo::getId).collect(Collectors.toList());
        }
        return houseIds;
    }

    public List<Long> getMoneyIds() {
        if (CollectionUtils.isNotEmpty(moneyList)) {
            moneyIds = moneyList.stream().map(TbContractIntentionMoneyEntity::getId).collect(Collectors.toList());
        }
        return moneyIds;
    }


}
