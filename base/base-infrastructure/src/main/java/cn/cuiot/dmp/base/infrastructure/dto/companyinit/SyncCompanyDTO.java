package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 企业初始化请求Dto
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Data
public class SyncCompanyDTO implements Serializable {
    /**
     * 源企业id
     */
    @NotNull(message = "源企业id不能为空")
    private Long sourceCompanyId;
    /**
     * 目标企业id
     */
    @NotNull(message = "目标企业id不能为空")
    private Long targetCompanyId;
}
