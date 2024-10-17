package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.ContractLeaseStatisticParam;
import cn.cuiot.dmp.lease.dto.contract.TbContractLeaseParam;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.mapper.TbContractIntentionMapper;
import cn.cuiot.dmp.lease.mapper.TbContractLeaseMapper;
import cn.cuiot.dmp.lease.vo.export.ContractLeaseExportVo;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.cuiot.dmp.lease.vo.ContractLeaseStatisticVO;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 租赁合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Service
public class TbContractLeaseService extends BaseMybatisServiceImpl<TbContractLeaseMapper, TbContractLeaseEntity> {
    @Autowired
    TbContractIntentionService contractIntentionService;
    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    BaseContractService baseContractService;
    @Autowired
    private ExcelExportService excelExportService;

    @Override
    public boolean save(TbContractLeaseEntity o) {
        TbContractLeaseEntity entity = (TbContractLeaseEntity) o;
        if (Objects.isNull(entity.getContractNo())) {
            Long code = SnowflakeIdWorkerUtil.nextId();
            entity.setContractNo(String.valueOf(code));
        }
        super.save(entity);
        bindInfoService.createContractBind(entity);
        return true;
    }

    @Override
    public boolean updateById(TbContractLeaseEntity o) {
        TbContractLeaseEntity entity = (TbContractLeaseEntity) o;
        bindInfoService.createContractBind(entity);
        return super.updateById(entity);
    }

    /**
     * 删除合同同时删除绑定信息
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        bindInfoService.removeByContractId(Long.valueOf(String.valueOf(id)));
        return super.removeById(id);
    }

    @Override
    public List<TbContractLeaseEntity> list(TbContractLeaseEntity params) {
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        List<TbContractLeaseEntity> list = super.list(params);
        list.forEach(c -> {
            baseContractService.fillBindHouseInfo(c);
        });
        return list;
    }

    @Override
    public PageResult<TbContractLeaseEntity> page(PageQuery param) {
        TbContractLeaseParam params = (TbContractLeaseParam) param;
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        PageResult<TbContractLeaseEntity> page = super.page(params);
        page.getRecords().forEach(c -> {
            baseContractService.fillBindHouseInfo(c);
        });
        return page;
    }

    public PageResult<TbContractLeaseEntity> pageNoSigned(PageQuery param) {
        TbContractLeaseParam params = (TbContractLeaseParam) param;
        List<Long> leaseIds = contractIntentionService.queryBindContractLeaseId();
        params.setQueryNotInIds(leaseIds);
        PageResult<TbContractLeaseEntity> page = super.page(params);
        page.getRecords().forEach(c -> {
            baseContractService.fillBindHouseInfo(c);
        });
        return page;
    }

    @Override
    public TbContractLeaseEntity getById(Serializable id) {
        AssertUtil.notNull(id, "id不能为空");
        TbContractLeaseEntity leaseEntity = super.getById(id);
        baseContractService.fillBindHouseInfo(leaseEntity);
        return leaseEntity;
    }


    public List<BaseVO> statisticsContract() {
        return baseMapper.statisticsContract();
    }


    public void export(TbContractLeaseParam pageQuery) throws Exception {
        excelExportService.excelExport(ExcelDownloadDto.<TbContractLeaseParam>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                .title("租赁合同列表").fileName("租赁合同导出" + "(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + ")").sheetName("租赁合同列表")
                .build(), ContractLeaseExportVo.class, this::executePageQuery);
    }

    private IPage<ContractLeaseExportVo> executePageQuery(ExcelDownloadDto<TbContractLeaseParam> tbContractLeaseParamExcelDownloadDto) {
        TbContractLeaseParam pageQuery = tbContractLeaseParamExcelDownloadDto.getQuery();
        String houseName = pageQuery.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {

            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            pageQuery.setQueryIds(queryIds);
        }
        PageResult<TbContractLeaseEntity> pageResult = super.page(pageQuery);
        List<ContractLeaseExportVo> exportDataList = new ArrayList<>();
        pageResult.getList().forEach(o -> {
            ContractLeaseExportVo exportVo = new ContractLeaseExportVo();
            BeanUtil.copyProperties(o, exportVo);
            exportDataList.add(exportVo);
        });
        Page<ContractLeaseExportVo> page = new Page<>(pageResult.getCurrentPage(), pageResult.getPageSize(), pageResult.getTotal());
        return page.setRecords(exportDataList);
    }

    public ContractLeaseStatisticVO contractLeaseArchiveStatistic(@Valid ContractLeaseStatisticParam idsReq) {

        // 查询已租
        // (不包含以下状态)
        idsReq.setContractStatus(
                CollectionUtil.newArrayList(ContractEnum.STATUS_DARFT.getCode(),
                ContractEnum.STATUS_COMMITING.getCode(),
                ContractEnum.STATUS_END.getCode(),
                ContractEnum.STATUS_USELESS.getCode()));

        Long leased = baseMapper.queryLeaseStatistic(idsReq);

        // 查询待租
        // (包含以下状态)
        idsReq.setContractStatus(
                CollectionUtil.newArrayList(ContractEnum.STATUS_WAITING.getCode(),
                        ContractEnum.STATUS_EXECUTING.getCode(),
                        ContractEnum.STATUS_CANCELING.getCode()));

        Long leasing = ((TbContractIntentionMapper)contractIntentionService.getBaseMapper()).queryIntentionStatistic(idsReq);

        ContractLeaseStatisticVO statisticResDTO = new ContractLeaseStatisticVO();
        statisticResDTO.setLeased(Optional.ofNullable(leased).orElse(0L));
        statisticResDTO.setLeasing(Optional.ofNullable(leasing).orElse(0L));
        return statisticResDTO;
    }
}
