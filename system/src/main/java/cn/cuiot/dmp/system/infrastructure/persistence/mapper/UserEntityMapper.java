package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.IotBaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yth
 * @since 2023-08-28
 */
public interface UserEntityMapper extends IotBaseMapper<UserEntity> {

    int batchInsertUser(@Param("list") List<UserEntity> userEntityList);

    UserEntity findDeletedUser(Long value);

    List<UserEntity> getPersonUserByPhoneNumber(String phoneNum);

    List<UserDepartmentEntity> getUserDepartmentInfo(String phoneNum);

    List<UserEntity> findDeletedUserList(@Param("idList") List<Long> idList);

    int delUserLabel(@Param("idList") List<Long> idList);

    int delUserRole(@Param("idList") List<Long> idList);
}
