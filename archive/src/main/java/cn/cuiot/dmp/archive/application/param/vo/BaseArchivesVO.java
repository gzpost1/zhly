package cn.cuiot.dmp.archive.application.param.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Data
public class BaseArchivesVO<T> implements Serializable {

    private static final long serialVersionUID = -7159394178810792720L;

    /**
     * 档案id
     */
    private Long archiveId;

    /**
     * 档案类型
     */
    private Byte archiveType;

    /**
     * 档案详情
     */
    private T archivesDetail;

}
