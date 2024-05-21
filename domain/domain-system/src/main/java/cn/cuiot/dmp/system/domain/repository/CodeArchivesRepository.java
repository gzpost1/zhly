package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchives;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface CodeArchivesRepository {

    /**
     * 根据条件获取二维码档案分页
     */
    PageResult<CodeArchives> queryForPage(CodeArchivesPageQuery pageQuery);

    /**
     * 根据id获取二维码档案详情
     */
    CodeArchives queryForDetail(Long id);

    /**
     * 保存
     */
    int saveCodeArchives(CodeArchives codeArchives);

    /**
     * 更新
     */
    int updateCodeArchives(CodeArchives codeArchives);

    /**
     * 更新状态
     */
    int updateCodeArchivesStatus(CodeArchives codeArchives);

    /**
     * 删除
     */
    int deleteCodeArchives(Long id);

    /**
     * 手动关联
     */
    int associateCodeArchives(CodeArchives codeArchives);

}
