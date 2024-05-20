package cn.cuiot.dmp.system.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeArchivesPageQuery extends PageQuery {

    private static final long serialVersionUID = -6962341570088784584L;

    /**
     * 二维码id
     */
    private Long id;

    /**
     * 关联档案类型
     */
    private Byte archiveType;

    /**
     * 编码类型
     */
    private Byte codeType;

    /**
     * 停启用状态
     */
    private Byte status;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
