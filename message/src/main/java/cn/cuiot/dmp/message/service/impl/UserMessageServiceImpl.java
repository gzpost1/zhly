package cn.cuiot.dmp.message.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.message.constant.StatusConstans;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.dal.mapper.UserMessageMapper;
import cn.cuiot.dmp.message.param.MessagePageQuery;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public IPage<UserMessageEntity> getMessage(MessagePageQuery pageQuery) {
        if (StatusConstans.READ.equals(pageQuery.getReadStatus())) {
            Query query = new Query();
            query.addCriteria(Criteria.where("accepter").is(LoginInfoHolder.getCurrentUserId()));
            if (Objects.nonNull(pageQuery.getDataType())) {
                query.addCriteria(new Criteria().andOperator(Criteria.where("dataType").is(pageQuery.getDataType())));
            }
            if (Objects.nonNull(pageQuery.getMessageGtTime())) {
                query.addCriteria(Criteria.where("messageTime").gte(pageQuery.getMessageGtTime()));
            }
            if (Objects.nonNull(pageQuery.getMessageLeTime())) {
                query.addCriteria(Criteria.where("messageTime").lte(pageQuery.getMessageLeTime()));
            }
            //计算总数
            long total = mongoTemplate.count(query, UserMessageEntity.class);

            // 排序
            Sort sort = Sort.by(Sort.Direction.DESC, "messageTime");
            query.with(sort);
            // 分页 (当前页-1)*每页大小，每页大小
            long pageNum = pageQuery.getPageNo();
            int pageSize = pageQuery.getPageSize().intValue();
            query.skip((pageNum - 1) * pageSize).limit(pageSize);

            List<UserMessageEntity> dataList = mongoTemplate.find(query, UserMessageEntity.class);

            Page<UserMessageEntity> page = new Page<>(pageNum, pageSize);
            page.setTotal(total);
            page.setRecords(dataList);
            return page;
        } else {
            LambdaQueryWrapper<UserMessageEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserMessageEntity::getAccepter, LoginInfoHolder.getCurrentUserId());
            queryWrapper.eq(Objects.nonNull(pageQuery.getReadStatus()), UserMessageEntity::getReadStatus, pageQuery.getReadStatus());
            queryWrapper.eq(Objects.nonNull(pageQuery.getDataType()), UserMessageEntity::getDataType, pageQuery.getDataType());
            queryWrapper.ge(Objects.nonNull(pageQuery.getMessageGtTime()), UserMessageEntity::getMessageTime, pageQuery.getMessageGtTime());
            queryWrapper.le(Objects.nonNull(pageQuery.getMessageLeTime()), UserMessageEntity::getMessageTime, pageQuery.getMessageLeTime());
            queryWrapper.orderByDesc(UserMessageEntity::getMessageTime);
            return page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        }
    }

    @Override
    @Transactional
    public void readMessage(List<Long> ids) {
        LambdaQueryWrapper<UserMessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserMessageEntity::getId, ids);
        List<UserMessageEntity> userMessageEntities = this.baseMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(userMessageEntities)) {
            userMessageEntities.forEach(userMessageEntity -> {
                userMessageEntity.setReadStatus(StatusConstans.READ);
                userMessageEntity.setReadTime(new Date());
                mongoTemplate.save(userMessageEntity);
            });
        }
        this.baseMapper.deleteBatchIds(ids);
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