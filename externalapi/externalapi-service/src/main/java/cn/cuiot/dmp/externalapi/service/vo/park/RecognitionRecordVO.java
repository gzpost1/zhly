package cn.cuiot.dmp.externalapi.service.vo.park;

import cn.cuiot.dmp.externalapi.service.entity.park.IdentificationRecordEntity;
import lombok.Data;

/**
 * 识别记录实体类
 */
@Data
public class RecognitionRecordVO  extends IdentificationRecordEntity {

    /**
     * 楼盘名称
     */
    private  String communityName;
}