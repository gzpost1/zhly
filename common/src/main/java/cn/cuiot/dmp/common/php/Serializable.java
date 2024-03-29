package cn.cuiot.dmp.common.php;

/**
 * @author zhangxp207
 */
public interface Serializable {
    /**
     * serialize
     * @author <zhangxp207@chinaunicom.cn>
     * @since 2022/12/2 17:20
     * @return byte[]
     */
    byte[] serialize();
    /**
     * unserialize
     * @author <zhangxp207@chinaunicom.cn>
     * @since 2022/12/2 17:20
     * @param ss
     */
    void unserialize(byte[] ss);
}
