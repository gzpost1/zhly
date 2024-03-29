package cn.cuiot.dmp.domain.types;


/**
 * @Description 当前登录信息持有者
 * @Author 犬豪
 * @Date 2023/9/25 09:47
 * @Version V1.0
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


    public static <T> T executeWithLoginInfo(Function function, LoginInfo loginInfo) {
        setLocalLoginInfo(loginInfo);
        try {
            return (T) function.execute();
        } finally {
            clearLoginInfo();
        }
    }

    public interface Function {
        Object execute();
    }


    public static String getCurrentLoginUserPath() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getUserPath() : null;
    }

    public static String getCurrentLoginOrgPath() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getOrgPath() : null;
    }

    public static Long getCurrentLoginOrgId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getOrgId() : null;
    }

    public static Long getCurrentUserId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getUserId() : null;
    }

    public static String getCurrentUserIdStr() {
        if (LOCAL_OPERATOR_IDENTITY.get() != null && LOCAL_OPERATOR_IDENTITY.get().getUserId() != null) {
            return LOCAL_OPERATOR_IDENTITY.get().getUserId().toString();
        }
        return null;
    }

    public static Long getCurrentOrgId() {
        return LOCAL_OPERATOR_IDENTITY.get() != null ? LOCAL_OPERATOR_IDENTITY.get().getOrgId() : null;
    }

    public static String getCurrentOrgIdStr() {
        if (LOCAL_OPERATOR_IDENTITY.get() != null && LOCAL_OPERATOR_IDENTITY.get().getOrgId() != null) {
            return LOCAL_OPERATOR_IDENTITY.get().getOrgId().toString();
        }
        return null;
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
