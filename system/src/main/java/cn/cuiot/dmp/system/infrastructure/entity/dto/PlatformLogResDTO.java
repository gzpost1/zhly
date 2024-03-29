package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guoying
 * @className DeviceFileUploadVO
 * @description 设备文件上传记录
 * @date 2020-09-03 10:28:12
 */
@Builder
@Data
public class PlatformLogResDTO extends IdmResDTO {

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 返回的数据
     */
    private DataDTO data;

    public PlatformLogResDTO(String code, String message, DataDTO data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class DataDTO {

        private Long counts;

        private List<? extends BaseLogDTO> logDetail;

        private List<?> detail;

    }
}
