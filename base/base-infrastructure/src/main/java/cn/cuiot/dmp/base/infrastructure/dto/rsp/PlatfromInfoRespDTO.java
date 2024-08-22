package cn.cuiot.dmp.base.infrastructure.dto.rsp;


import lombok.Data;

/**
 * 对接外部api dto
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Data
public class PlatfromInfoRespDTO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 平台id
     */
    private String platformId;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 对接系统参数json数据
     */
    private String data;
}
