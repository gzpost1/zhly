package cn.cuiot.dmp.common.log.intf;

/**
 * @author guoying
 * @className AbstractResourceParam
 * @description 资源公共的属性
 * @date 2020-10-22 11:31:43
 */
public abstract class AbstractResourceParam implements ResourceParam {

    private String[] operationTarget;

    private String operationTargetInfo;

    public void setOperationTarget(String[] operationTarget) {
        this.operationTarget = operationTarget;
    }

    @Override
    public String[] getOperationTarget() {
        return operationTarget;
    }

    @Override
    public String getOperationTargetInfo() {
        return operationTargetInfo;
    }

    public void setOperationTargetInfo(String operationTargetInfo) {
        this.operationTargetInfo = operationTargetInfo;
    }
}
