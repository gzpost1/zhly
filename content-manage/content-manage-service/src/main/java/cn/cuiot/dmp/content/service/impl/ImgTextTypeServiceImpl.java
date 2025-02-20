package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.dal.entity.ImgTextType;
import cn.cuiot.dmp.content.dal.mapper.ImgTextTypeMapper;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import cn.cuiot.dmp.content.service.ImgTextTypeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 10:29
 */
@Service
public class ImgTextTypeServiceImpl extends ServiceImpl<ImgTextTypeMapper, ImgTextType> implements ImgTextTypeService {

    @Autowired
    private ContentImgTextService imgTextService;

    @Override
    public List<ImgTextType> queryForList(String orgId) {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImgTextType::getCompanyId, orgId);
        return list(queryWrapper);
    }

    @Override
    public ImgTextType getByName(String name) {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImgTextType::getName, name);
        queryWrapper.eq(ImgTextType::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public Boolean create(ImgTextType imgTextType) {
        checkCreateOrUpdate(imgTextType);
        imgTextType.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        boolean save = save(imgTextType);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文类型")
                .targetDatas(Lists.newArrayList(new OptTargetData(imgTextType.getName(), imgTextType.getId().toString())))
                .build());
        return save;
    }

    @Override
    public Boolean update(ImgTextType imgTextType) {
        checkCreateOrUpdate(imgTextType);
        boolean b = updateById(imgTextType);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文类型")
                .targetDatas(Lists.newArrayList(new OptTargetData(imgTextType.getName(), imgTextType.getId().toString())))
                .build());
        return b;
    }

    @Override
    public Boolean remove(Long id) {
        ImgTextType imgTextType = this.getById(id);
        if (imgTextType == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
        }
        List<ContentImgTextEntity> contentImgTextEntities = imgTextService.getByTypeId(id);
        if (CollUtil.isNotEmpty(contentImgTextEntities)) {
            throw new BusinessException(ResultCode.IMG_TEXT_TYPE_EXISTS_DATA);
        }
        removeById(id);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文类型")
                .targetDatas(Lists.newArrayList(new OptTargetData(imgTextType.getName(), imgTextType.getId().toString())))
                .build());
        return null;
    }

    @Override
    public Long getAllCount() {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImgTextType::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        return count(queryWrapper);
    }

    @Override
    public List<ImgTextType> queryByIds(List<Long> typeIds) {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ImgTextType::getId, typeIds);
        return list(queryWrapper);
    }

    public void checkCreateOrUpdate(ImgTextType imgTextType) {
        Long allCount = this.getAllCount();
        if (allCount >= 20 && imgTextType.getId() == null) {
            throw new RuntimeException("最多可添加20个类型");
        }
        ImgTextType byName = this.getByName(imgTextType.getName());
        if (byName == null) {
            return;
        }
        if (imgTextType.getId() == null) {
            throw new BusinessException(ResultCode.IMG_TEXT_TYPE_EXISTS);
        }
        if (!byName.getId().equals(imgTextType.getId())) {
            throw new BusinessException(ResultCode.IMG_TEXT_TYPE_EXISTS);
        }

    }
}
