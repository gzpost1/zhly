package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description 当前操作信息s
 * @Author 犬豪
 * @Date 2023/9/25 09:06
 * @Version V1.0
 */
@Data
public class LoginInfo {
    public LoginInfo() {
    }

    public LoginInfo(String userName, Long userId, Long orgId) {
        this.userName = userName;
        this.userId = userId;
        this.orgId = orgId;
    }

    /**
     * 传入lazyPathFinder会开启path的懒加载功能，实现依赖于各个微服务具体实现
     *
     * @param lazyPathFinder
     * @param userName
     * @param userId
     * @param orgId
     */
    public LoginInfo(LazyPathFinder lazyPathFinder, String userName, Long userId, Long orgId) {
        this.lazyPathFinder = lazyPathFinder;
        this.userName = userName;
        this.userId = userId;
        this.orgId = orgId;
    }

    /**
     * 懒加载path查找器
     */
    private LazyPathFinder lazyPathFinder;

    /**
     * 是否已经尝试加载过了，标记接口
     */
    private boolean findedUserPathMark;
    /**
     * 是否已经尝试加载过了，标记接口
     */
    private boolean findedOrgPathMark;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账户ID
     */
    private Long orgId;

    /**
     * 用户所在组织路径
     */
    private String userPath;

    /**
     * 账户所在组织路径
     */
    private String orgPath;

    /**
     * 操作信息
     */
    private OperateInfo operateInfo;

    public String getUserPath() {
        if (userPath == null && !findedUserPathMark && getUserId() != null && lazyPathFinder != null) {
            userPath = lazyPathFinder.lookUpUserPath(getUserId());
            findedUserPathMark = true;
        }
        return userPath;
    }

    public String getOrgPath() {
        if (orgPath == null && !findedOrgPathMark && getOrgId() != null && lazyPathFinder != null) {
            orgPath = lazyPathFinder.lookUpOrgPath(getUserId());
            findedOrgPathMark = true;
        }
        return orgPath;
    }

    /**
     * 懒加载的用户所在path查找器
     */
    public interface LazyPathFinder {
        String lookUpUserPath(Long userId);

        String lookUpOrgPath(Long orgId);
    }

    public OperateInfo buildOperateInfoIfNecessary() {
        if (operateInfo == null) {
            operateInfo = new OperateInfo();
        }
        return operateInfo;
    }

    /**
     * 标记删除操作
     */
    public void markDeleteOperation() {
        markDeleteOperation(getUserId() == null ? null : getUserId().toString(), LocalDateTime.now(), OperateByTypeEnum.USER);
    }

    /**
     * 标记删除操作
     */
    public void markDeleteOperation(String deleteBy, LocalDateTime deleteOn, OperateByTypeEnum deleteByType) {
        buildOperateInfoIfNecessary();
        operateInfo.setOperateType(3);
        operateInfo.setOperateOn(deleteOn);
        operateInfo.setOperateBy(deleteBy);
        operateInfo.setOperateByType(deleteByType);
    }

    /**
     * 标记新增操作
     */
    public void markCreateOperation() {
        markCreateOperation(getUserId() == null ? null : getUserId().toString(), LocalDateTime.now(), OperateByTypeEnum.USER);
    }

    /**
     * 标记新增操作
     */
    public void markCreateOperation(String createBy, LocalDateTime createdOn, OperateByTypeEnum createdByType) {
        buildOperateInfoIfNecessary();
        operateInfo.setOperateType(1);
        operateInfo.setOperateOn(createdOn);
        operateInfo.setOperateBy(createBy);
        operateInfo.setOperateByType(createdByType);
    }

    /**
     * 标记更新操作
     */
    public void markUpdateOperation() {
        markUpdateOperation(getUserId() == null ? null : getUserId().toString(), LocalDateTime.now(), OperateByTypeEnum.USER);
    }

    /**
     * 标记更新操作
     */
    public void markUpdateOperation(String updateBy, LocalDateTime updateOn, OperateByTypeEnum updateByType) {
        buildOperateInfoIfNecessary();
        operateInfo.setOperateType(2);
        operateInfo.setOperateOn(updateOn);
        operateInfo.setOperateBy(updateBy);
        operateInfo.setOperateByType(updateByType);
    }


}
