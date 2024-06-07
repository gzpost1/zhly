package cn.cuiot.dmp.archive.api.controller.app;

import cn.cuiot.dmp.archive.application.param.dto.HouseKeeperDto;
import cn.cuiot.dmp.archive.application.param.query.HouseKeeperQuery;
import cn.cuiot.dmp.archive.application.service.HouseKeeperService;
import cn.cuiot.dmp.archive.infrastructure.entity.HouseKeeperEntity;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【APP】管家管理
 * @author wuyongchong
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/app/house-keeper")
public class AppHouseKeeperController {

    @Autowired
    private HouseKeeperService houseKeeperService;

    /**
     * 获得小区楼管
     */
    @PostMapping("/queryForList")
    public IdmResDTO<List<HouseKeeperEntity>> queryForList() {
        Long communityId = LoginInfoHolder.getCommunityId();
        if(Objects.isNull(communityId)){
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "小区楼盘ID参数为空");
        }
        HouseKeeperQuery query = new HouseKeeperQuery();
        query.setCommunityId(communityId);
        List<HouseKeeperEntity> dataList = houseKeeperService.queryForList(query);
        return IdmResDTO.success(dataList);
    }

}
