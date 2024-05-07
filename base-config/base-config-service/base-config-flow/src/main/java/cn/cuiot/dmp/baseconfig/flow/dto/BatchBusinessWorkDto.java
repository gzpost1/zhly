package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/6 17:07
 */
@Data
public class BatchBusinessWorkDto {

    private List<Long> ids;

    /**
     *  0 停用  1 启用 2 删除
     */
    private Byte businessType;
}
