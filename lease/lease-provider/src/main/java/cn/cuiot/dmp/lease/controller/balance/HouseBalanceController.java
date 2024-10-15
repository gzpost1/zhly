package cn.cuiot.dmp.lease.controller.balance;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.lease.dto.charge.HouseInfoDto;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.lease.service.charge.TbChargeStandardService;
import cn.cuiot.dmp.lease.util.ExcelUtils;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChargeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.enums.BalanceChangeTypeEnum;
import cn.cuiot.dmp.pay.service.service.service.BalanceRuleAtHandler;
import cn.cuiot.dmp.pay.service.service.vo.BalanceChangeRecordSysVo;
import cn.cuiot.dmp.pay.service.service.vo.BalanceChargeRecordVO;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
import cn.cuiot.dmp.pay.service.service.vo.RechargeBalanceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.utils.MathTool.checkCollectionEmpty;
import static cn.cuiot.dmp.pay.service.service.enums.Constants.ARTIFICIAL;

/**
 * 系统管理-预缴管理
 *
 * @author wuyongchong
 * @since 2023-11-16
 */
@Slf4j
@RestController
@RequestMapping("/balance")
public class HouseBalanceController extends BaseController {

    @Autowired
    private BalanceRuleAtHandler ruleAtHandler;
    @Autowired
    private ChargeHouseAndUserService chargeHouseAndUserService;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private TbChargeStandardService tbChargeStandardService;
    /**
     * 充值记录列表
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryBalanceChangeRecordForPage")
    public IdmResDTO<IPage<BalanceChangeRecordSysVo>> queryBalanceChangeRecordForPage(@RequestBody BalanceChangeRecordQuery query) {
        IPage<BalanceChangeRecordSysVo> pageVo = queryBalanceChangeRecord(query);
        return IdmResDTO.success(pageVo);
    }

    /**
     * 充值记录导出
     */
    @RequiresPermissions
    @PostMapping(value = "/balanceChangeRecord/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody @Valid BalanceChangeRecordQuery query) throws IOException {
        query.setPageSize(10000L);
        IPage<BalanceChangeRecordSysVo> pageVo = queryBalanceChangeRecord(query);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("空间档案", pageVo.getRecords(), BalanceChangeRecordSysVo.class);

        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtils.downLoadExcel(
                "充值记录-" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
                response,
                workbook);
    }

    private IPage<BalanceChangeRecordSysVo> queryBalanceChangeRecord(BalanceChangeRecordQuery query){
        IPage<BalanceChangeRecord> pageResult = ruleAtHandler.queryBalanceChangeRecordForPage(query);
        IPage<BalanceChangeRecordSysVo> pageVo = getPageVo(pageResult, BalanceChangeRecordSysVo.class);
        List<BalanceChangeRecordSysVo> records = pageVo.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return pageVo;
        }
        //转译用户名称及房屋名称
        List<Long> houseIds = records.stream().map(vo->vo.getHouseId()).distinct().collect(Collectors.toList());
        List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(houseIds));
        Map<Long, String> houseMap = houseInfoDtos.stream().collect(Collectors.toMap(HouseInfoDto::getHouseId, HouseInfoDto::getHouseName));
        //填充操作人名称
        List<Long> userIds = records.stream().map(BalanceChangeRecordSysVo::getCreateUser).distinct().collect(Collectors.toList());
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setUserIdList(userIds);
        List<BaseUserDto> baseUserDtoList = systemToFlowService.lookUpUserList(baseUserReqDto);
        Map<Long, BaseUserDto> userMap = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, vo->vo));
        for(BalanceChangeRecordSysVo vo:records){
            vo.setHouseName(houseMap.get(vo.getHouseId()));
            BaseUserDto user = userMap.get(vo.getCreateUser());
            vo.setCreateName(Objects.isNull(user)?null:user.getName()+"("+user.getPhoneNumber()+")");
        }

        return pageVo;
    }


    public static <T, R> IPage<R> getPageVo(IPage<T> iPage, Class<R> clz) {
        List<T> sourceList = checkCollectionEmpty(iPage.getRecords()) ? Collections.emptyList() : iPage.getRecords();
        List<R> targetList = BeanMapper.mapList(sourceList, clz);
        IPage<R> newPage = new Page<>();
        iPage.setRecords(null);
        BeanMapper.copy(iPage,newPage);
        newPage.setRecords(targetList);
        return newPage;
    }

    /**
     * 扣缴记录列表
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryChargeForPage")
    public IdmResDTO<IPage<BalanceChargeRecordVO>> queryChargeForPage(@RequestBody @Valid BalanceChargeRecordQuery query) {
        IPage<BalanceChargeRecordVO> page = queryCharge(query);
        return IdmResDTO.success(page);
    }


    /**
     * 扣缴录导出
     */
    @RequiresPermissions
    @PostMapping(value = "/Charge/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody @Valid BalanceChargeRecordQuery query) throws IOException {
        query.setPageSize(10000L);
        IPage<BalanceChargeRecordVO> pageVo = queryCharge(query);
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("空间档案", pageVo.getRecords(), BalanceChargeRecordVO.class);

        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtils.downLoadExcel(
                "扣缴记录-" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
                response,
                workbook);
    }


    public IPage<BalanceChargeRecordVO> queryCharge(BalanceChargeRecordQuery query) {
        IPage<BalanceChargeRecordVO> page = ruleAtHandler.queryChargeForPage(query);
        List<BalanceChargeRecordVO> records = page.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return page;
        }
        //转译房屋名称
        List<Long> houseIds = records.stream().map(vo->vo.getHouseId()).distinct().collect(Collectors.toList());
        List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(houseIds));
        Map<Long, String> houseMap = houseInfoDtos.stream().collect(Collectors.toMap(HouseInfoDto::getHouseId, HouseInfoDto::getHouseName));
        //填充收费标准
        List<Long> chargeStandardIds = records.stream().map(vo->vo.getChargeStandard()).collect(Collectors.toList());
        List<TbChargeStandard> tbChargeStandards = Optional.ofNullable(tbChargeStandardService.listByIds(chargeStandardIds)).orElse(new ArrayList<>());
        Map<Long, TbChargeStandard> chargeStandardMap = tbChargeStandards.stream().collect(Collectors.toMap(TbChargeStandard::getId, Function.identity()));
        //收费项目名称
        List<Long> carteIds = records.stream().map(vo->vo.getChargeItemId()).collect(Collectors.toList());
        CommonOptionSettingReqDTO commonOptionSettingReqDTO = new CommonOptionSettingReqDTO();
        commonOptionSettingReqDTO.setIdList(carteIds);
        List<CommonOptionSettingRspDTO> commonOptionSettingRspDTOS = Optional.ofNullable(systemToFlowService.batchQueryCommonOptionSetting(commonOptionSettingReqDTO)).orElse(new ArrayList<>());
        Map<Long, CommonOptionSettingRspDTO> changeItemMap = commonOptionSettingRspDTOS.stream().collect(Collectors.toMap(CommonOptionSettingRspDTO::getId, Function.identity()));

        for(BalanceChargeRecordVO vo:records){
            vo.setHouseName(houseMap.get(vo.getHouseId()));
            vo.setChargeStandardName(Optional.ofNullable(chargeStandardMap.get(vo.getChargeStandard())).map(v->v.getChargeStandard()).orElse(null));
            vo.setChargeItemName(Optional.ofNullable(changeItemMap.get(vo.getChargeItemId())).map(v->v.getName()).orElse(null));
        }

        return page;
    }

    /**
     * 查询房屋当前余额
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryHouseBalance")
    public IdmResDTO<BalanceEntity> queryHouseBalance(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(ruleAtHandler.queryHouseBalance(param.getId()));
    }


    /**
     * 余额变动(包括充值，扣减等)-业务端调用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "changeBalance", operationName = "系统管理端-余额变动", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/changeBalance")
    public IdmResDTO changeBalance(@RequestBody @Valid RechargeBalanceVo vo) {
        BalanceEventAggregate balanceEventAggregate = new BalanceEventAggregate();
        balanceEventAggregate.setChangeUser(ARTIFICIAL);
        balanceEventAggregate.setBalance(vo.getBalance());
        balanceEventAggregate.setHouseId(vo.getHouseId());
        balanceEventAggregate.setReason(vo.getReason());
        balanceEventAggregate.setOrderName("后台充值");
        balanceEventAggregate.setChangeType(BalanceChangeTypeEnum.BALANCE_RECHARGE.getType());
        ruleAtHandler.handler(balanceEventAggregate);
        return IdmResDTO.success();
    }


}
