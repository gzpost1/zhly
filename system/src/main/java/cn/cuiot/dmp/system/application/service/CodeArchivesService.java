package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CodeArchivesVO;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface CodeArchivesService {

    /**
     * 根据条件获取二维码档案分页
     */
    PageResult<CodeArchivesVO> queryForPage(CodeArchivesPageQuery pageQuery);

    /**
     * 根据id获取二维码档案详情
     */
    CodeArchivesVO queryForDetail(Long id);

    /**
     * 保存
     */
    int saveCodeArchives(CodeArchivesCreateDTO createDTO);

    /**
     * 更新
     */
    int updateCodeArchives(CodeArchivesUpdateDTO updateDTO);

    /**
     * 更新状态
     */
    int updateCodeArchivesStatus(UpdateStatusParam updateStatusParam);

    /**
     * 删除
     */
    int deleteCodeArchives(Long id);

    /**
     * 手动关联
     */
    int associateCodeArchives(CodeArchivesUpdateDTO updateDTO);

    void export(CodeArchivesPageQuery pageQuery) throws Exception;
}
