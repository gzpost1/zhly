package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.base.infrastructure.domain.repository.impl.AbstraceRepositoryImpl;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.base.infrastructure.domain.converter.Converter;
import cn.cuiot.dmp.system.infrastructure.domain.converter.UserConverter;
import cn.cuiot.dmp.system.infrastructure.persistence.iservice.IUserEntityService;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserDepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntityMapper;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.entity.UserDepartmentInfo;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.user_manage.query.UserCommonQuery;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 犬豪
 * @Date 2023/8/28 14:30
 * @Version V1.0
 */
@Repository
public class UserRepositoryImpl extends
        AbstraceRepositoryImpl<UserEntity, User, UserId, UserEntityMapper> implements UserRepository {

    @Autowired
    private UserEntityMapper userEntityMapper;
    @Autowired
    private IUserEntityService iUserEntityService;

    private static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_NAME_USER_ID = "user_id";
    private static final String COLUMN_NAME_EMAIL = "email";
    private static final String COLUMN_NAME_USER_TYPE = "user_type";
    private static final String COLUMN_NAME_USER_NAME = "username";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_PASSWORD = "password";

    @Override
    protected UserEntityMapper getEntityMapper() {
        return userEntityMapper;
    }

    @Override
    protected Converter<User, UserEntity> getConverter() {
        return UserConverter.INSTANCE;
    }



    private void fillUpdatePropertyValue(User user) {
        if (user.getUpdatedOn() == null) {
            user.setUpdatedOn(LocalDateTime.now());
        }
        if (user.getUpdatedBy() == null && user.getUpdatedByType() == null) {
            user.setUpdatedBy(OperateByTypeEnum.SYSTEM.name().toLowerCase());
            user.setUpdatedByType(OperateByTypeEnum.SYSTEM);
        }
    }

    private void fillInsertPropertyValue(User user) {
        //处理id<=0的异常情况
        user.setId(null);
        if (user.getCreatedOn() == null) {
            user.setCreatedOn(LocalDateTime.now());
        }
        if (user.getCreatedBy() == null && user.getCreatedByType() == null) {
            user.setCreatedBy(OperateByTypeEnum.SYSTEM.name().toLowerCase());
            user.setCreatedByType(OperateByTypeEnum.SYSTEM);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSave(@NonNull List<User> aggregate) {
        if (aggregate.isEmpty()) {
            return false;
        }
        //新增删除分组
        Map<Integer, List<User>> collect = aggregate.stream().collect(Collectors.groupingBy(user -> user.getId() == null ? 1 : 0));
        for (Map.Entry<Integer, List<User>> entry : collect.entrySet()) {
            List<User> value = entry.getValue();
            if (entry.getKey() == 0) {
                //更新
                fillUpdatePropertyValue(value);
                List<UserEntity> entityList = UserConverter.INSTANCE.toEntityList(value);
                iUserEntityService.updateBatchById(entityList);
            } else {
                //新增
                fillInsertPropertyValue(value);
                Map<UserEntity, User> entityMap = UserConverter.INSTANCE.toEntityMap(value);
                List<UserEntity> entityList = new ArrayList<>(entityMap.keySet());
                userEntityMapper.batchInsertUser(entityList);
                //填充ID
                fillId(entityMap);
            }
        }
        return true;
    }

    private void fillUpdatePropertyValue(List<User> value) {
        value.forEach(this::fillUpdatePropertyValue);
    }

    private void fillInsertPropertyValue(List<User> value) {
        value.forEach(this::fillInsertPropertyValue);
    }

    private static void fillId(Map<UserEntity, User> entityMap) {
        for (Map.Entry<UserEntity, User> userEntry : entityMap.entrySet()) {
            UserEntity key = userEntry.getKey();
            User value = userEntry.getValue();
            if (value.getId() == null || value.getId().getValue() == null) {
                value.setId(new UserId(key.getId()));
            }
        }
    }

    @Override
    public List<User> commonQuery(@NonNull UserCommonQuery userQuery) {
        QueryWrapper<UserEntity> queryWrapper = buildQueryWrapper(userQuery);
        List<UserEntity> userEntities = userEntityMapper.selectList(queryWrapper);
        return UserConverter.INSTANCE.toDomainList(userEntities);
    }

    @Override
    public User commonQueryOne(@NonNull UserCommonQuery userQuery) {
        List<User> resultList = commonQuery(userQuery);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public User queryByUserNameOrPhoneNumberOrEmail(String userName, PhoneNumber phoneNumber, Email email) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(COLUMN_NAME_USER_TYPE, UserTypeEnum.USER.getValue()).and(childQueryWrapper -> {
            if (StringUtils.isNotBlank(userName)) {
                childQueryWrapper.or().eq(COLUMN_NAME_USER_NAME, userName);
            }
            if (phoneNumber != null) {
                childQueryWrapper.or().eq(COLUMN_NAME_PHONE_NUMBER, phoneNumber.getEncryptedValue());
            }
            if (email != null) {
                childQueryWrapper.or().eq(COLUMN_NAME_EMAIL, email.getEncryptedValue());
            }
        });

        List<UserEntity> userEntities = userEntityMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userEntities)) {
            return null;
        }
        return UserConverter.INSTANCE.toDomainList(userEntities).get(0);
    }

    @Override
    public User findUserWithDeleted(@NonNull UserId userId) {
        UserEntity userEntity = userEntityMapper.findDeletedUser(userId.getValue());
        return UserConverter.INSTANCE.toDomain(userEntity);
    }

    @Override
    public List<User> findUserListWithDeleted(@NonNull List<UserId> userIdList) {
        List<UserEntity> userEntityList = userEntityMapper.findDeletedUserList(userIdList.stream().map(UserId::getValue).distinct().collect(Collectors.toList()));
        return UserConverter.INSTANCE.toDomainList(userEntityList);
    }

    @Override
    public int remove(@NonNull UserCommonQuery userCommonQuery) {
        if (CollectionUtils.isEmpty(userCommonQuery.getIdList())) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "批量删除必须指定ID，防止删错数据");
        }
        QueryWrapper<UserEntity> queryWrapper = buildQueryWrapper(userCommonQuery);
        return userEntityMapper.delete(queryWrapper);
    }

    @Override
    public User queryUserForLogin(String userName, PhoneNumber phoneNumber, @NonNull String password) {
        if (StringUtils.isBlank(userName) && phoneNumber == null) {
            return null;
        }
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(COLUMN_NAME_USER_TYPE, UserTypeEnum.USER.getValue())
                .and(childQueryWrapper -> {
                    if (StringUtils.isNotBlank(userName)) {
                        childQueryWrapper.or().eq(COLUMN_NAME_USER_NAME, userName);
                    }
                    if (phoneNumber != null) {
                        childQueryWrapper.or().eq(COLUMN_NAME_PHONE_NUMBER, phoneNumber.getEncryptedValue());
                    }
                });

        List<UserEntity> userEntities = userEntityMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userEntities)) {
            return null;
        }
        if(!new Password().verifyPassword(userEntities.get(0).getPassword(), password) ) {
            return null;
        }
        return UserConverter.INSTANCE.toDomainList(userEntities).get(0);
    }

    @Override
    public List<User> getPersonUserByPhoneNumber(String phoneNumber) {
        List<UserEntity> userEntities = userEntityMapper.getPersonUserByPhoneNumber(phoneNumber);
        return UserConverter.INSTANCE.toDomainList(userEntities);
    }

    @Override
    public List<UserDepartmentInfo> getUserDepartmentInfo(String phoneNumber) {
        List<UserDepartmentEntity> userEntityList = userEntityMapper.getUserDepartmentInfo(phoneNumber);
        List<UserDepartmentInfo> infoList = new ArrayList<>();
        for (UserDepartmentEntity entity : userEntityList) {
            UserDepartmentInfo info = new UserDepartmentInfo();
            info.setId(entity.getId());
            info.setPath(entity.getPath());
            info.setOrgName(entity.getOrgName());
            infoList.add(info);
        }
        return infoList;
    }
    @Override
    public void removeUserRelate(List<Long> idList) {
        userEntityMapper.delUserLabel(idList);
        userEntityMapper.delUserRole(idList);
    }

    private QueryWrapper<UserEntity> buildQueryWrapper(UserCommonQuery userQuery) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        // ID
        if (CollectionUtils.isNotEmpty(userQuery.getIdList())) {
            queryWrapper.in(COLUMN_NAME_ID, userQuery.getIdList().stream().map(UserId::getValue).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        }

        // 密码
        if (userQuery.getPassword() != null) {
            queryWrapper.eq(COLUMN_NAME_PASSWORD, userQuery.getPassword().getHashEncryptValue());
        }


        // 姓名
        if (userQuery.getUsername() != null) {
            queryWrapper.eq(COLUMN_NAME_USER_NAME, userQuery.getUsername());
        }

        // USER_ID
        if (userQuery.getUserId() != null) {
            queryWrapper.eq(COLUMN_NAME_USER_ID, userQuery.getUserId());
        }

        // 手机号
        if (userQuery.getPhoneNumber() != null) {
            queryWrapper.eq(COLUMN_NAME_PHONE_NUMBER, userQuery.getPhoneNumber().getEncryptedValue());
        }

        // 邮箱
        if (userQuery.getEmail() != null) {
            queryWrapper.eq(COLUMN_NAME_EMAIL, userQuery.getEmail().getEncryptedValue());
        }

        // 用户类型
        if (CollectionUtils.isNotEmpty(userQuery.getUserTypeEnumList())) {
            queryWrapper.and(childQueryWrapper -> {
                // in查询
                List<Integer> userTypeExcludeNull =
                        userQuery.getUserTypeEnumList().stream().filter(userTypeEnum -> userTypeEnum != UserTypeEnum.NULL)
                                .map(UserTypeEnum::getValue).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(userTypeExcludeNull)) {
                    childQueryWrapper.in(COLUMN_NAME_USER_TYPE, userTypeExcludeNull);
                }
                // isNull
                if (userQuery.getUserTypeEnumList().stream()
                        .anyMatch(userTypeEnum -> userTypeEnum == UserTypeEnum.NULL)) {
                    childQueryWrapper.or().isNull(COLUMN_NAME_USER_TYPE);
                }
            });
        }

        return queryWrapper;
    }
}
