package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.TbContractIntentionParam;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.mapper.TbContractIntentionMapper;
import cn.cuiot.dmp.lease.vo.export.ContractIntentionExportVo;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 意向合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractIntentionService extends BaseMybatisServiceImpl<TbContractIntentionMapper, TbContractIntentionEntity> {

    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    SystemApiFeignService systemApiFeignService;
    @Autowired
    BaseContractService baseContractService;
    @Autowired
    private ExcelExportService excelExportService;

    @Override
    public List<TbContractIntentionEntity> list(TbContractIntentionEntity params) {
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        List<TbContractIntentionEntity> list = super.list(params);
        list.forEach(c -> {
            baseContractService.fillBindHouseInfo(c);
        });
        return list;
    }

    @Override
    public PageResult<TbContractIntentionEntity> page(PageQuery param) {
        TbContractIntentionParam params = (TbContractIntentionParam) param;
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        PageResult<TbContractIntentionEntity> page = super.page(param);
        page.getRecords().forEach(c -> {
            baseContractService.fillBindHouseInfo(c);
        });
        return page;
    }

    @Override
    public TbContractIntentionEntity getById(Serializable id) {
        AssertUtil.notNull(id, "id不能为空");
        TbContractIntentionEntity intentionEntity = super.getById(id);
        baseContractService.fillBindHouseInfo(intentionEntity);
        return intentionEntity;
    }

    public TbContractIntentionEntity getByContractNo(String contractNo) {
        LambdaQueryWrapper<TbContractIntentionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractIntentionEntity::getContractNo, contractNo);
        queryWrapper.last("limit 1");
        TbContractIntentionEntity intentionEntity = baseMapper.selectOne(queryWrapper);
        baseContractService.fillBindHouseInfo(intentionEntity);
        return intentionEntity;
    }


    @Override
    public boolean save(TbContractIntentionEntity o) {
        TbContractIntentionEntity entity = (TbContractIntentionEntity) o;
        if (Objects.isNull(entity.getContractNo())) {
            Long code = SnowflakeIdWorkerUtil.nextId();
            entity.setContractNo(String.valueOf(code));
        }
        super.save(entity);
        bindInfoService.createContractBind(entity);
        return true;
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
    public boolean updateById(TbContractIntentionEntity entity) {
        TbContractIntentionEntity o = (TbContractIntentionEntity) entity;
        bindInfoService.createContractBind(o);
        return super.updateById(o);
    }

    public List<BaseVO> statisticsContract() {
        return baseMapper.statisticsContract(baseContractService.getLoginDeptIds());
    }

    /**
     * 查询已经有关联的租赁合同
     *
     * @return
     */
    public List<Long> queryBindContractLeaseId() {
        LambdaQueryWrapper<TbContractIntentionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNotNull(TbContractIntentionEntity::getContractLeaseId);
        List<TbContractIntentionEntity> intentionEntities = baseMapper.selectList(queryWrapper);
        List<Long> leaseIds = intentionEntities.stream().map(TbContractIntentionEntity::getContractLeaseId).distinct().collect(Collectors.toList());
        return leaseIds;
    }

    public void export(TbContractIntentionParam param) throws Exception {
        excelExportService.excelExport(ExcelDownloadDto.<TbContractIntentionParam>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(param)
                .title("意向合同列表").fileName("意向合同导出" + "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("意向合同列表")
                .build(), ContractIntentionExportVo.class, this::executePageQuery);
    }

    private IPage<ContractIntentionExportVo> executePageQuery(ExcelDownloadDto<TbContractIntentionParam> dto) {
        TbContractIntentionParam query = dto.getQuery();
        String houseName = query.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            query.setQueryIds(queryIds);
        }
        PageResult<TbContractIntentionEntity> pageResult = super.page(query);
        List<ContractIntentionExportVo> exportDataList = new ArrayList<>();
        pageResult.getList().forEach(o -> {
            ContractIntentionExportVo exportVo = new ContractIntentionExportVo();
            BeanUtil.copyProperties(o, exportVo);
            exportDataList.add(exportVo);
        });
        Page<ContractIntentionExportVo> page = new Page<>(pageResult.getCurrentPage(), pageResult.getPageSize(), pageResult.getTotal());
        return page.setRecords(exportDataList);
    }
}
