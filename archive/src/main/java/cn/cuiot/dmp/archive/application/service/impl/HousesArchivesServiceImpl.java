package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.HousesArchivesMapper;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DoubleValidator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 房屋档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service
public class HousesArchivesServiceImpl extends ServiceImpl<HousesArchivesMapper, HousesArchivesEntity> implements HousesArchivesService {

    /**
     * 参数校验
     */
    @Override
    public void checkParams(HousesArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房屋名称不可为空");
        }
        if (StringUtils.isBlank(entity.getRoomNum())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房号不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属楼盘不可为空");
        }
        if (StringUtils.isBlank(entity.getCode())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房屋编码不可为空");
        }

        // 规则判断
        if (entity.getName().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房屋名称长度不可超过30");
        }
        if (entity.getRoomNum().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房号长度不可超过30");
        }
        if (entity.getCode().length() > 15){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房屋编码长度不可超过30");
        }
        if (StringUtils.isNotBlank(entity.getFloorAlias()) && entity.getFloorAlias().length() > 4){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"楼层别名长度不可超过4");
        }
        if (StringUtils.isNotBlank(entity.getFloorName()) &&entity.getFloorName().length() > 7){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"楼层别名长度不可超过7");
        }
        if (StringUtils.isNotBlank(entity.getCapacity()) &&entity.getCapacity().length() > 9){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"容量长度不可超过9");
        }
        if (StringUtils.isNotBlank(entity.getQibie()) && entity.getQibie().length() > 11){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"期别长度不可超过11");
        }
        if (Objects.nonNull(entity.getFloorCoefficient()) && (entity.getFloorCoefficient() < 0.0 || entity.getFloorCoefficient() > 9999.99)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"楼层系数区间0-9999.99");
        }
        if (Objects.nonNull(entity.getBuildingArea()) && !DoubleValidator.validateDouble(entity.getBuildingArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"建筑面积支持4位小数，最长可输入15位");
        }
        if (Objects.nonNull(entity.getUsableArea()) && !DoubleValidator.validateDouble(entity.getUsableArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"使用面积支持4位小数，最长可输入15位");
        }
        if (Objects.nonNull(entity.getChargeArea()) && !DoubleValidator.validateDouble(entity.getChargeArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"收费面积支持4位小数，最长可输入15位");
        }
        if (Objects.nonNull(entity.getSharedArea()) && !DoubleValidator.validateDouble(entity.getSharedArea(), 15, 4)){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"公摊面积支持4位小数，最长可输入15位");
        }
        if (StringUtils.isNotBlank(entity.getOwnershipUnit()) && entity.getOwnershipUnit().length() > 50){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"产权单位长度不可超过50");
        }
    }

    @Override
    public void checkParamsImport(HousesArchiveImportDto entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房屋名称不可为空");
        }
        if (StringUtils.isBlank(entity.getRoomNum())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房号不可为空");
        }
        if (StringUtils.isBlank(entity.getLoupanName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属楼盘不可为空");
        }
        if (StringUtils.isBlank(entity.getCode())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"房屋编码不可为空");
        }

        // 规则判断
        if (entity.getName().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房屋名称长度不可超过30");
        }
        if (entity.getRoomNum().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房号长度不可超过30");
        }
        if (entity.getCode().length() > 15){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"房屋编码长度不可超过30");
        }
    }

    @Override
    public List<HousesArchiveExportVo> buildExportData(IdsParam param) {
        // 查询列表信息
        List<HousesArchivesEntity> list = this.listByIds(param.getIds());
        List<HousesArchiveExportVo> res = new ArrayList<>(list.size());

        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 查询楼盘信息-用于楼盘id转换为楼盘名称-汇总成Map
        Map<Long, String> loupanIdNameMap = new HashMap<>();
        // 查询配置信息-用于配置id转换为配置名称-汇总成Map
        Map<Long, String> configIdNameMap = new HashMap<>();

        // 构造导出列表
        list.forEach(entity -> {
            HousesArchiveExportVo vo = new HousesArchiveExportVo();
            vo.setCode(entity.getCode());
            vo.setLoupanName(loupanIdNameMap.getOrDefault(entity.getLoupanId(), ""));
            vo.setRoomNum(entity.getRoomNum());
            vo.setBuildingArea(getFiledForExport(entity.getBuildingArea()));
            vo.setUsableArea(getFiledForExport(entity.getUsableArea()));
            vo.setChargeArea(getFiledForExport(entity.getChargeArea()));
            vo.setStatusName(configIdNameMap.getOrDefault(entity.getStatus(), ""));
            vo.setHouseTypeName(configIdNameMap.getOrDefault(entity.getHouseType(), ""));
            vo.setCapacity(entity.getCapacity());
            vo.setQibie(entity.getQibie());
            vo.setFloorName(entity.getFloorName());
            vo.setFloorAlias(entity.getFloorAlias());
            vo.setPropertyTypeName(configIdNameMap.getOrDefault(entity.getPropertyType(), ""));
            res.add(vo);
        });

        return res;
    }

    @Override
    public void importDataSave(List<HousesArchiveImportDto> dataList) {
        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 先查询所属楼盘，如果查不到，就报错，查到生成map-nameIdMap
        Map<String, Long> nameIdMap = new HashMap<>();
        // 查询指定配置的数据，如果有配置，查询生成map-nameConfigIdMap
        Map<String, Long> nameConfigIdMap = new HashMap<>();

        // 构造插入列表进行保存
        List<HousesArchivesEntity> list = new ArrayList<>();
        dataList.forEach(data -> {
            HousesArchivesEntity entity = new HousesArchivesEntity();
            entity.setLoupanId(nameIdMap.getOrDefault(data.getLoupanName(), 1L));
            entity.setRoomNum(data.getRoomNum());
            entity.setName(data.getName());
            entity.setCode(data.getCode());
            // TODO: 2024/5/16 这里还需要基于不同的一级类目去查询配置
            list.add(entity);
        });

        this.saveBatch(list);
    }

    private String getFiledForExport(Object value){
        if (Objects.isNull(value)){
            return "";
        }
        return value.toString();
    }
}
