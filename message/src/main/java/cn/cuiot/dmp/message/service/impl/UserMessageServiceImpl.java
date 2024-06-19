package cn.cuiot.dmp.message.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.message.constant.StatusConstans;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.dal.mapper.UserMessageMapper;
import cn.cuiot.dmp.message.param.MessagePageQuery;
import cn.cuiot.dmp.message.service.UserMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 11:40
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessageEntity> implements UserMessageService {

    @Override
    public IPage<UserMessageEntity> getMessage(MessagePageQuery pageQuery) {
        LambdaQueryWrapper<UserMessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserMessageEntity::getAccepter, LoginInfoHolder.getCurrentUserId());
        queryWrapper.eq(Objects.nonNull(pageQuery.getReadStatus()), UserMessageEntity::getReadStatus, pageQuery.getReadStatus());
        queryWrapper.eq(Objects.nonNull(pageQuery.getDataType()), UserMessageEntity::getDataType, pageQuery.getDataType());
        queryWrapper.ge(Objects.nonNull(pageQuery.getMessageGtTime()), UserMessageEntity::getMessageTime, pageQuery.getMessageGtTime());
        queryWrapper.le(Objects.nonNull(pageQuery.getMessageLeTime()), UserMessageEntity::getMessageTime, pageQuery.getMessageLeTime());
        queryWrapper.orderByDesc(UserMessageEntity::getMessageTime);
        return page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
    }

    @Override
    public void readMessage(List<Long> ids) {
        LambdaUpdateWrapper<UserMessageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(UserMessageEntity::getId, ids);
        updateWrapper.set(UserMessageEntity::getReadStatus, StatusConstans.READ);
        updateWrapper.set(UserMessageEntity::getReadTime, new Date());
        update(updateWrapper);
    }

    @Override
    public List<Long> getAcceptDataIdList(MsgExistDataIdReqDto reqDto) {
        return this.baseMapper.getAcceptDataIdList(reqDto);
    }

    @Override
    public Long getUnreadMessageCount() {
        LambdaQueryWrapper<UserMessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserMessageEntity::getAccepter, LoginInfoHolder.getCurrentUserId());
        queryWrapper.eq(UserMessageEntity::getReadStatus, StatusConstans.UNREAD);
        return count(queryWrapper);
    }
}
