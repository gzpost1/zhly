package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.BusinessType;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface BusinessTypeRepository {

    /**
     * 根据id获取业务类型详情
     */
    BusinessType queryForDetail(Long id);

    /**
     * 根据企业id获取业务类型树
     */
    List<BusinessType> queryByCompany(Long companyId);

    /**
     * 保存
     */
    int saveBusinessType(BusinessType businessType);

    /**
     * 更新
     */
    int updateBusinessType(BusinessType businessType);

    /**
     * 删除预校验
     */
    void checkDeleteStatus(Long id);

    /**
     * 删除
     */
    int deleteBusinessType(List<String> idList);

}
