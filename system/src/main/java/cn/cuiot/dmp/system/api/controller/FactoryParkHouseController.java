package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.system.application.enums.OrgLabelEnum;
import cn.cuiot.dmp.system.application.service.BaseHouseService;
import cn.cuiot.dmp.system.application.service.CommercialBuildingHouseService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.HouseDeleteDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgLabelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseInfoResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PropertyHouseListReqVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import javax.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物业侧-厂园区房屋Controller
 *
 * @author huw51
 * @date 2023-01-11 10:07:42
 */
@RestController
@RequestMapping("factoryPark/house")
public class FactoryParkHouseController extends BaseController {

    @Autowired
    private CommercialBuildingHouseService commercialBuildingHouseService;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private BaseHouseService baseHouseService;

    /**
     * 列表分页条件查询
     *
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:space:home")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<PropertyHouseListReqVO> listAll(@RequestBody PropertyHouseListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return commercialBuildingHouseService.selectAll(dto);
    }

    @RequiresPermissions("factoryPark:home:add")
    @LogRecord(operationCode = "add", operationName = "添加房屋", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public int add(@RequestBody @Valid PropertyHouseAddReqDto dto) {
        if (Strings.isNotEmpty(dto.getUsedArea()) && !dto.getUsedArea().matches(RegexConst.NEW_HOUSE_AREA)) {
            throw new BusinessException(ResultCode.HOUSE_USE_AREA_SUPPORT_NUM);
        }
        if (Strings.isNotEmpty(dto.getPublicArea()) && !dto.getPublicArea().matches(RegexConst.NEW_HOUSE_AREA)) {
            throw new BusinessException(ResultCode.HOUSE_PUBLIC_AREA_SUPPORT_NUM);
        }
        dto.setPkOrgId(Long.parseLong(getOrgId()));
        if (dto.getRegionId() == null) {
            throw new BusinessException(ResultCode.REGION_NOT_NULL);
        }
        dto.setLoginUserId(getUserId());
        OrgLabelDto orgLabelDto = organizationDao.getOrgLabelById(getOrgId());
        if (!OrgLabelEnum.FACTORY_PARK.getId().equals(orgLabelDto.getLabelId())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        return commercialBuildingHouseService.add(dto);
    }

    /**
     * 修改
     */
    @RequiresPermissions("factoryPark:home:edit")
    @LogRecord(operationCode = "update", operationName = "修改房屋", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public int update(@RequestBody @Valid PropertyHouseUpdateReqDto dto) {
        if (Strings.isNotEmpty(dto.getUsedArea()) && !dto.getUsedArea().matches(RegexConst.NEW_HOUSE_AREA)) {
            throw new BusinessException(ResultCode.HOUSE_USE_AREA_SUPPORT_NUM);
        }
        if (Strings.isNotEmpty(dto.getPublicArea()) && !dto.getPublicArea().matches(RegexConst.NEW_HOUSE_AREA)) {
            throw new BusinessException(ResultCode.HOUSE_PUBLIC_AREA_SUPPORT_NUM);
        }
        dto.setLoginUserId(getUserId());
        dto.setOrgId(getOrgId());
        OrgLabelDto orgLabelDto = organizationDao.getOrgLabelById(getOrgId());
        if (!OrgLabelEnum.FACTORY_PARK.getId().equals(orgLabelDto.getLabelId())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        return commercialBuildingHouseService.update(dto);
    }

    /**
     * 信息
     */
    @RequiresPermissions("factoryPark:home:detail")
    @PostMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public PropertyHouseInfoResDto info(@RequestParam("id") String id) {
        return commercialBuildingHouseService.selectById(Long.parseLong(id), getOrgId(), getUserId());
    }

    /**
     * 删除房屋
     *
     * @param dto
     * @return
     */
    @PostMapping("/delete")
    @RequiresPermissions("factoryPark:home:delete")
    @LogRecord(operationCode = "delete", operationName = "删除房屋", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    public int delete(@RequestBody HouseDeleteDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return baseHouseService.batchDelete(dto);
    }

}
