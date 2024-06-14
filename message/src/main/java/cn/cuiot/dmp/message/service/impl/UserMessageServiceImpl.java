package cn.cuiot.dmp.message.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.message.constant.StatusConstans;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.dal.mapper.UserMessageMapper;
import cn.cuiot.dmp.message.param.MessagePageQuery;
import cn.cuiot.dmp.message.service.UserMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
        queryWrapper.orderByDesc(UserMessageEntity::getMessageTime);
        return page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
    }

    @Override
    public void readMessage(Long id) {
        UserMessageEntity messageEntity = this.getById(id);
        if (Objects.nonNull(messageEntity) && StatusConstans.UNREAD.equals(messageEntity.getReadStatus())) {
            messageEntity.setReadStatus(StatusConstans.READ);
            this.updateById(messageEntity);
        }
    }

    @Override
    public List<Long> getAcceptDataIdList(MsgExistDataIdReqDto reqDto) {
        return this.baseMapper.getAcceptDataIdList(reqDto);
    }
}
