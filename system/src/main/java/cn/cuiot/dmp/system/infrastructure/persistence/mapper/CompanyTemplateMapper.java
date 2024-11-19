package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.CompanyTemplateEntity;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CompanyTemplateVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 后台-初始化管理-企业模板 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-11-05
 */
public interface CompanyTemplateMapper extends BaseMapper<CompanyTemplateEntity> {

    /**
     * 获取最后一条模板数据
     *
     * @return CompanyTemplateVO
     */
    CompanyTemplateVO queryLastTemplate();
}
