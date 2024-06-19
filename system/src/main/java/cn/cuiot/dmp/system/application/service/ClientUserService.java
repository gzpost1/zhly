package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ClientUserQuery;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ClientUserVo;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.ClientUserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * C端用户服务
 *
 * @author: wuyongchong
 * @date: 2024/6/14 9:28
 */
@Service
public class ClientUserService {

    @Autowired
    private ClientUserMapper clientUserMapper;

    /**
     * 分页查询
     */
    public IPage<ClientUserVo> queryForPage(ClientUserQuery query) {
        if(StringUtils.isNotBlank(query.getPhone())){
            query.setPhone(Sm4.encryption(query.getPhone()));
        }
        IPage<ClientUserVo> page = clientUserMapper
                .queryForList(new Page<ClientUserVo>(query.getPageNo(), query.getPageSize()),
                        query);
        for (ClientUserVo userVo : page.getRecords()) {
            if (StringUtils.isNotBlank(userVo.getPhoneNumber())) {
                userVo.setPhoneNumber(Sm4.decrypt(userVo.getPhoneNumber()));
            }
        }
        return page;
    }

}
