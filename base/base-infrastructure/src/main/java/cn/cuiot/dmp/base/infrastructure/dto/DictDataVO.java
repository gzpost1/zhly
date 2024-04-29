package cn.cuiot.dmp.base.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.Data;

/**
 * 字典数据
 * @author: wuyongchong
 * @date: 2024/4/29 9:29
 */
@Data
public class DictDataVO implements Serializable {

    @JsonIgnore
    private Long dataId;

    private String dataName;
    private String dataValue;

    private Integer sort;
    private Byte status;

    private String remark;

    public DictDataVO() {

    }

    public DictDataVO(Long dataId, String dataName, String dataValue, Integer sort,
            Byte status) {
        this.dataId = dataId;
        this.dataName = dataName;
        this.dataValue = dataValue;
        this.sort = sort;
        this.status = status;
    }

    public DictDataVO(Long dataId, String dataName, String dataValue, Integer sort,
            Byte status, String remark) {
        this.dataId = dataId;
        this.dataName = dataName;
        this.dataValue = dataValue;
        this.sort = sort;
        this.status = status;
        this.remark = remark;
    }
}