package cn.cuiot.dmp.externalapi.service.query.hik;

import java.io.Serializable;
import lombok.Data;

/**
 * 查询门禁点状态
 * @author: wuyongchong
 * @date: 2024/10/10 18:33
 */
@Data
public class HaikangAcsDoorStateQuery implements Serializable {

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 资源编码
     */
    private String indexCode;

}
