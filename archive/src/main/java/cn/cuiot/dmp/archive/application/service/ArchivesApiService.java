package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.ArchivesApiQueryDTO;
import cn.cuiot.dmp.archive.application.param.vo.BaseArchivesVO;

/**
 * @author caorui
 * @date 2024/5/23
 */
public interface ArchivesApiService {

    /**
     * 二维码档案-根据档案id和类型获取详情
     */
    BaseArchivesVO<Object> queryForDetail(ArchivesApiQueryDTO queryDTO);

}
