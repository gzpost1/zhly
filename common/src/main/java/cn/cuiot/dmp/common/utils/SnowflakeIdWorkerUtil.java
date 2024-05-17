package cn.cuiot.dmp.common.utils;

/**
 * @author: wuyongchong
 * @date: 2024/4/30 10:25
 */
public class SnowflakeIdWorkerUtil {

    /**
     * 雪花算法生成器
     */
    private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    /**
     * 生成ID
     * @return
     */
    public static Long nextId(){
        return idWorker.nextId();
    }
}
