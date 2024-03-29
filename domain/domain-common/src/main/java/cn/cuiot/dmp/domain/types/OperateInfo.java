package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description 操作信息
 * @Author 犬豪
 * @Date 2023/10/19 10:07
 * @Version V1.0
 */
@Data
public class OperateInfo {
    /**
     * 操作类型 1新增 2更新 3删除
     */
    private int operateType;

    /**
     * 操作时间
     */
    private LocalDateTime operateOn;

    /**
     * 操作者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String operateBy;

    /**
     * 操作者类型（1：system、2：用户、3：open api）
     */
    private OperateByTypeEnum operateByType;

    /**
     * 是否是新增操作
     */
    public boolean isCreate() {
        return operateType == 1;
    }

    /**
     * 是否是更新操作
     */
    public boolean isUpdate() {
        return operateType == 2;
    }

    /**
     * 是否是删除操作
     */
    public boolean isDelete() {
        return operateType == 3;
    }
}
