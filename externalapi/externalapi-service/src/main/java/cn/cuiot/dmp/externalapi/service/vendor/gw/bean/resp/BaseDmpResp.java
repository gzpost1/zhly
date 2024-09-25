package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import cn.cuiot.dmp.externalapi.service.vendor.gw.enums.DmpEntranceGuardResCode;
import cn.cuiot.dmp.externalapi.service.vendor.video.enums.VsuapResCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseDmpResp<T> implements Serializable {
    private String code;
    private String message;
    private String requestId;
    private T data;

    public Boolean isSuccess() {
        return DmpEntranceGuardResCode.SUCCESS_000000.getCode().equals(this.code);
    }
}
