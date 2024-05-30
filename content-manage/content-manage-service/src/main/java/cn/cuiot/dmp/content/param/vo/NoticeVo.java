package cn.cuiot.dmp.content.param.vo;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 14:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeVo extends ContentNoticeEntity {

    private List<String> departmentNames;

    private List<String> buildingNames;

}
