package cn.cuiot.dmp.archive.infrastructure.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/6/13 17:37
 */
@Data
public class CustomerHouseExportVo implements Serializable {

    /**
     * 房屋ID
     */
    private Long houseId;


    /**
     * 身份
     */
    private String identityTypeName;


    /**
     * 迁入日期
     */
    private String moveInDateStr;


    /**
     * 迁出日期
     */
    private String moveOutDateStr;

}
