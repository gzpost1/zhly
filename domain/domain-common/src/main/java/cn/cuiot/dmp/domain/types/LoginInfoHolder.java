package cn.cuiot.dmp.domain.types;


/**
 * 当前登录信息持有者
 */
public class LoginInfoHolder {

    private static final ThreadLocal<LoginInfo> LOCAL_OPERATOR_IDENTITY = new ThreadLocal<>();

    public static void setLocalLoginInfo(LoginInfo loginInfo) {
        LOCAL_OPERATOR_IDENTITY.set(loginInfo);
    }

    public static void clearLoginInfo() {
        LOCAL_OPERATOR_IDENTITY.remove();
    }

    public static LoginInfo getCurrentLoginInfo() {
        return LOCAL_OPERATOR_IDENTITY.get();
    }

    /**
     * 用户id
     */
    public static Long getCurrentUserId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getUserId() : null;
    }

    /**
     * 用户名
     */
    public static String getCurrentUsername() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getUsername() : null;
    }

    /**
     * 用户的手机号
     */
    public static String getCurrentPhoneNumber() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getPhoneNumber() : null;
    }

    /**
     * 姓名
     */
    public static String getCurrentName() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getName() : null;
    }

    /**
     * 账户ID
     */
    public static Long getCurrentOrgId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getOrgId() : null;
    }

    /**
     * 账户类型
     */
    public static Integer getCurrentOrgTypeId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getOrgTypeId() : null;
    }

    /**
     * 部门ID
     */
    public static Long getCurrentDeptId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getDeptId() : null;
    }

    /**
     * 岗位ID
     */
    public static Long getCurrentPostId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getPostId() : null;
    }

    /**
     * 用户类型
     */
    public static Integer getCurrentUserType() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getUserType() : null;
    }

    /**
     * 获得小区ID
     */
    public static Long getCommunityId(){
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getCommunityId() : null;
    }

    public static OperateInfo getCurrentLoginOperateInfo() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getOperateInfo() : null;
    }

    public static void markCreateOperation() {
        if (LOCAL_OPERATOR_IDENTITY.get() != null) {
            LOCAL_OPERATOR_IDENTITY.get().markCreateOperation();
        }
    }

    public static void markUpdateOperation() {
        if (LOCAL_OPERATOR_IDENTITY.get() != null) {
            LOCAL_OPERATOR_IDENTITY.get().markUpdateOperation();
        }
    }

    public static void markDeleteOperation() {
        if (LOCAL_OPERATOR_IDENTITY.get() != null) {
            LOCAL_OPERATOR_IDENTITY.get().markDeleteOperation();
        }
    }

}
