package cn.cuiot.dmp.content.param.vo;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentAudit;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import lombok.Data;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 15:32
 */
@Data
public class ImgTextVo extends ContentImgTextEntity {

    private List<String> departmentNames;

    private List<String> buildingNames;

    private String creatUserName;

    private ContentAudit contentAudit;
}
