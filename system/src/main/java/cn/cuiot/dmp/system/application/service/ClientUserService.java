package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.system.application.param.vo.export.ClientUserExportVo;
import cn.cuiot.dmp.system.application.param.vo.export.CodeArchiveExportVo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ClientUserQuery;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ClientUserVo;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.ClientUserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 分页查询
     */
    public IPage<ClientUserVo> queryForPage(ClientUserQuery query) {
        if (StringUtils.isNotBlank(query.getPhone())) {
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

    public void export(ClientUserQuery pageQuery) throws Exception {
        IPage<ClientUserVo> pageResult = clientUserMapper.queryForList(new Page<ClientUserVo>(pageQuery.getPageNo(), pageQuery.getPageSize()), pageQuery);
        if (pageResult.getTotal() > ExcelExportService.MAX_EXPORT_DATA) {
            throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
        }
        IPage<ClientUserExportVo> exportDataList = pageResult.convert(o -> {
            ClientUserExportVo exportVo = new ClientUserExportVo();
            BeanUtils.copyProperties(o, exportVo);
            return exportVo;
        });
        excelExportService.excelExport(ExcelReportDto.<ClientUserQuery, ClientUserExportVo>builder().title("C端用户列表").fileName("C端用户导出").SheetName("C端用户列表")
                .dataList(exportDataList.getRecords()).build(), ClientUserExportVo.class);
    }
}
