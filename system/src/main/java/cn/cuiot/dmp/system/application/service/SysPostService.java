package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.system.application.param.command.SysPostCmd;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysPostQuery;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysPostEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysPostMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntityMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 岗位管理服务
 *
 * @author: wuyongchong
 * @date: 2024/5/6 9:32
 */
@Slf4j
@Service
public class SysPostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    /**
     * 分页查询
     */
    public IPage<SysPostEntity> queryForPage(SysPostQuery query) {
        LambdaQueryWrapper<SysPostEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysPostEntity::getOrgId, query.getSessionOrgId());
        if (StringUtils.isNotBlank(query.getPostName())) {
            lambdaQueryWrapper.like(SysPostEntity::getPostName, query.getPostName());
        }
        if (Objects.nonNull(query.getStatus())) {
            lambdaQueryWrapper.like(SysPostEntity::getStatus, query.getStatus());
        }
        lambdaQueryWrapper.orderByDesc(SysPostEntity::getCreateTime);
        IPage<SysPostEntity> pageResult = sysPostMapper
                .selectPage(new Page<SysPostEntity>(query.getPageNo(), query.getPageSize()),
                        lambdaQueryWrapper);
        return pageResult;
    }

    /**
     * 列表查询
     */
    public List<SysPostEntity> queryForList(SysPostQuery query) {
        LambdaQueryWrapper<SysPostEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysPostEntity::getOrgId, query.getSessionOrgId());
        if (StringUtils.isNotBlank(query.getPostName())) {
            lambdaQueryWrapper.like(SysPostEntity::getPostName, query.getPostName());
        }
        if (Objects.nonNull(query.getStatus())) {
            lambdaQueryWrapper.like(SysPostEntity::getStatus, query.getStatus());
        }
        lambdaQueryWrapper.orderByDesc(SysPostEntity::getCreateTime);
        List<SysPostEntity> selectList = sysPostMapper
                .selectList(lambdaQueryWrapper);
        return selectList;
    }

    public boolean nameExists(Long id, String name, Long orgId) {
        LambdaQueryWrapper<SysPostEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysPostEntity::getOrgId, orgId);
        lambdaQueryWrapper.eq(SysPostEntity::getPostName, name);
        if (Objects.nonNull(id)) {
            lambdaQueryWrapper.ne(SysPostEntity::getId, id);
        }
        Long selectCount = sysPostMapper.selectCount(lambdaQueryWrapper);
        if (selectCount >= 1) {
            return true;
        }
        return false;
    }

    /**
     * 创建
     */
    public void create(SysPostCmd cmd) {
        SysPostEntity entity = BeanMapper.map(cmd, SysPostEntity.class);
        entity.setOrgId(cmd.getSessionOrgId());
        sysPostMapper.insert(entity);
    }

    /**
     * 修改
     */
    public void udpate(SysPostCmd cmd) {
        SysPostEntity dbEntity = sysPostMapper.selectById(cmd.getId());
        if (!dbEntity.getOrgId().equals(cmd.getSessionOrgId())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        SysPostEntity entity = BeanMapper.map(cmd, SysPostEntity.class);
        entity.setOrgId(cmd.getSessionOrgId());
        sysPostMapper.updateById(entity);
    }

    /**
     * 启停用
     */
    public void updateStatus(UpdateStatusParam param, Long sessionUserId,
            Long sessionOrgId) {
        SysPostEntity dbEntity = sysPostMapper.selectById(param.getId());
        if (!dbEntity.getOrgId().equals(sessionOrgId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        if (EntityConstants.DISABLED.equals(param.getStatus())) {
            if (bindUsers(param.getId())) {
                throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "该岗位已绑定用户，不可停用");
            }
        }
        dbEntity.setStatus(param.getStatus());
        sysPostMapper.updateById(dbEntity);
    }

    /**
     * 删除
     */
    public void delete(IdParam idParam, Long sessionUserId, Long sessionOrgId) {
        SysPostEntity dbEntity = sysPostMapper.selectById(idParam.getId());
        if (!dbEntity.getOrgId().equals(sessionOrgId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        if (bindUsers(idParam.getId())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR, "该岗位已绑定用户，不可删除");
        }
        sysPostMapper.deleteByIdWithAllField(dbEntity);
    }

    /**
     * 是否绑定用户
     */
    public boolean bindUsers(Long postId) {
        Long selectCount = userEntityMapper
                .selectCount(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getPostId, postId));
        if (selectCount >= 1) {
            return true;
        }
        return false;
    }

}
