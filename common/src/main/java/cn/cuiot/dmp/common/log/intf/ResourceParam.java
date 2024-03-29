package cn.cuiot.dmp.common.log.intf;

/**
 * @author guoying
 * @className ResourceParam
 * @description 资源参数
 * @date 2020-10-22 11:30:07
 */
public interface ResourceParam {
    /***
     * 获取操作对象
     * @return
     */
    String[] getOperationTarget();

    /***
     * 获取操作对象信息
     * @return
     */
    String getOperationTargetInfo();
}
