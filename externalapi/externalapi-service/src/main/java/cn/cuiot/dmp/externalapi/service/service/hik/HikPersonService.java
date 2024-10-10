package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.hik.HikPersonEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HikPersonMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HikPersonFaceDataDto;
import cn.cuiot.dmp.externalapi.service.query.hik.HikPersonInfoCreateDto;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikFaceSingleUpdateReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikPersonAddReq;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 海康-人员信息 业务层
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Service
public class HikPersonService extends ServiceImpl<HikPersonMapper, HikPersonEntity> {

    @Autowired
    private HikApiFeignService hikApiFeignService;
    @Autowired
    private HikCommonHandle hikCommonHandle;

    /**
     * 创建人员基础信息 Step 1
     *
     * @return Long 主键id
     * @Param dto 参数
     */
    public Long createPersonInfo(HikPersonInfoCreateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // id
        long id = IdWorker.getId();

        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        HikPersonAddReq req = new HikPersonAddReq();
        BeanUtils.copyProperties(dto, req);
        req.setPersonId(id + "");
        // 调用外部接口添加人员信息
        hikApiFeignService.personAdd(req, bo);

        HikPersonEntity entity = new HikPersonEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setCompany(companyId);
        save(entity);

        return id;
    }

    /**
     * 编辑照片信息 Step 2
     *
     * @return 人员信息id
     * @Param dto 参数
     */
    public Long editFaceData(HikPersonFaceDataDto dto) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        HikPersonEntity entity = getOne(new LambdaQueryWrapper<HikPersonEntity>()
                .eq(HikPersonEntity::getId, dto.getId())
                .eq(HikPersonEntity::getCompany, companyId)
                .last(" LIMIT 1 "));
        if (Objects.isNull(entity)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        // 调用外部接口编辑照片
        HikFaceSingleUpdateReq req = new HikFaceSingleUpdateReq();
        req.setFaceId(entity.getId() + "");
        req.setFaceData(dto.getFaceData());
        hikApiFeignService.faceSingleUpdate(req, bo);

        entity.setFaceData(dto.getFaceData());
        updateById(entity);

        return entity.getId();
    }

    public void editAuthorize() {

    }
}
