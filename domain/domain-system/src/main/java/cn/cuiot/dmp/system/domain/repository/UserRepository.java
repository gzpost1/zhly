package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.repository.Repository;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.entity.UserDepartmentInfo;
import cn.cuiot.dmp.system.domain.query.UserCommonQuery;
import java.util.List;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:05
 * @Version V1.0
 */
public interface UserRepository extends Repository<User, UserId> {

    /**
     * 批量ID删除
     *
     * @param aggregate
     * @return
     */
    @Override
    int removeList(@NonNull List<UserId> aggregate);

    /**
     * 批量修改插入 null字段默认忽略 有ID修改，无ID新增
     */
    boolean batchSave(@NonNull List<User> aggregate);

    /**
     * 通用条件查询
     */
    List<User> commonQuery(@NonNull UserCommonQuery userQuery);

    /**
     * 通用条件查询，返回一条记录
     */
    User commonQueryOne(@NonNull UserCommonQuery userQuery);

    /**
     * 根据用户名、或者手机号、或者邮箱查询"实体用户"
     */
    User queryByUserNameOrPhoneNumberOrEmail(String userName, PhoneNumber phoneNumber, Email email,Integer userType);

    /**
     * 查询已删除用户（正常用户也可查询）
     */
    User findUserWithDeleted(@NonNull UserId userId);

    /**
     * 查询已删除用户（正常用户也可查询）
     */
    List<User> findUserListWithDeleted(@NonNull List<UserId> userIdList);

    /**
     * 条件删除
     */
    int remove(@NonNull UserCommonQuery userCommonQuery);

    /**
     * 查询用户（登录使用）
     */
    User queryUserForLogin(String userName, PhoneNumber phoneNumber);

    /**
     * 查询用户（门禁人员）
     */
    List<User> getPersonUserByPhoneNumber(String phoneNumber);

    /**
     * 查询用户信息（门禁人员）
     */
    List<UserDepartmentInfo> getUserDepartmentInfo(String phoneNumber);


    void removeUserRelate(List<Long> idList);
}
