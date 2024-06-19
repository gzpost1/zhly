package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description 当前操作信息s
 * @Author 犬豪
 * @Date 2023/9/25 09:06
 * @Version V1.0
 */
@Data
public class LoginInfo implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户的手机号
     */
    private String phoneNumber;

    /**
     * 姓名
     */
    private String name;

    /**
     * 企业ID
     */
    private Long orgId;

    /**
     * 企业类型（5:平台 11:企业）
     */
    private Integer orgTypeId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 岗位ID
     */
    private Long postId;


    /**
     * 用户类型(1:员工 2：业主）
     */
    private Integer userType;

    /**
     * 小区ID
     */
    private Long communityId;

    /**
     * 操作信息
     */
    private OperateInfo operateInfo;


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
        markDeleteOperation(getUserId() == null ? null : getUserId().toString(),
                LocalDateTime.now(), OperateByTypeEnum.USER);
    }

    /**
     * 标记删除操作
     */
    public void markDeleteOperation(String deleteBy, LocalDateTime deleteOn,
            OperateByTypeEnum deleteByType) {
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
        markCreateOperation(getUserId() == null ? null : getUserId().toString(),
                LocalDateTime.now(), OperateByTypeEnum.USER);
    }

    /**
     * 标记新增操作
     */
    public void markCreateOperation(String createBy, LocalDateTime createdOn,
            OperateByTypeEnum createdByType) {
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
        markUpdateOperation(getUserId() == null ? null : getUserId().toString(),
                LocalDateTime.now(), OperateByTypeEnum.USER);
    }

    /**
     * 标记更新操作
     */
    public void markUpdateOperation(String updateBy, LocalDateTime updateOn,
            OperateByTypeEnum updateByType) {
        buildOperateInfoIfNecessary();
        operateInfo.setOperateType(2);
        operateInfo.setOperateOn(updateOn);
        operateInfo.setOperateBy(updateBy);
        operateInfo.setOperateByType(updateByType);
    }


}
