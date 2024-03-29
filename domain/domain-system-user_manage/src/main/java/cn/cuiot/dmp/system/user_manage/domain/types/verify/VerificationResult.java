package cn.cuiot.dmp.system.user_manage.domain.types.verify;

import lombok.Data;

/**
 * @Description 验证结果
 * @Author 犬豪
 * @Date 2023/8/30 14:43
 * @Version V1.0
 */
@Data
public class VerificationResult {

    public VerificationResult(boolean pass) {
        this.pass = pass;
    }

    private boolean pass;

}
