package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujianyu
 * @description 房屋查询条件
 * @since 2024-05-15 10:37
 */
@Data
public class HousesArchivesQuery extends PageQuery {

    /**
     * 所属楼盘id
     */
    @NotNull(message = "请选择所属楼盘")
    private Long loupanId;

    /**
     * 房屋编码
     */
    private String code;

    /**
     * 产权单位（支持输入汉字、英文、符号、数字，长度支持50字符）
     */
    private String ownershipUnit;

}
