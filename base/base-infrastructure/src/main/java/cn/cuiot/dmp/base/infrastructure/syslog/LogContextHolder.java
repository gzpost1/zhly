package cn.cuiot.dmp.base.infrastructure.syslog;

/**
 * 日志操作对象内容上下文
 * @author: wuyongchong
 * @date: 2024/6/17 14:07
 */
public class LogContextHolder {

    /**
     * 线程变量
     */
    private final static ThreadLocal<OptTargetInfo> OPT_TARGET_CONTEXT = new ThreadLocal<>();

    /**
     * 设置
     * @param optTargetInfo
     */
    public static void setOptTargetInfo(OptTargetInfo optTargetInfo){
        OPT_TARGET_CONTEXT.set(optTargetInfo);
    }

    /**
     * 获取
     * @return
     */
    public static OptTargetInfo getOptTargetInfo(){
        return OPT_TARGET_CONTEXT.get();
    }

    /**
     * 移除
     * @return
     */
    public static void remove(){
        OPT_TARGET_CONTEXT.remove();
    }

}
