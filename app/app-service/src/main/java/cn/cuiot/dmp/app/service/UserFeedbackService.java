package cn.cuiot.dmp.app.service;

import cn.cuiot.dmp.app.dto.UserFeedbackQuery;
import cn.cuiot.dmp.app.entity.UserFeedbackEntity;
import cn.cuiot.dmp.app.mapper.UserFeedbackMapper;
import cn.cuiot.dmp.app.vo.export.UserFeedbackExportVo;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 用户反馈 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-14
 */
@Service
public class UserFeedbackService extends ServiceImpl<UserFeedbackMapper, UserFeedbackEntity> {

    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 提交反馈意见
     */
    public void createUserFeedback(UserFeedbackEntity entity) {
        userFeedbackMapper.insert(entity);
    }

    /**
     * 分页查询
     */
    public IPage<UserFeedbackEntity> queryForPage(UserFeedbackQuery query) {
        IPage<UserFeedbackEntity> page = userFeedbackMapper
                .queryForPage(new Page<UserFeedbackEntity>(query.getPageNo(), query.getPageSize()),
                        query);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(item -> {
                if (StringUtils.isNotBlank(item.getPhone())) {
                    item.setPhone(Sm4.decrypt(item.getPhone()));
                }
            });
        }
        return page;
    }

    /**
     * 获得详情
     */
    public UserFeedbackEntity queryForDetail(Long id) {
        UserFeedbackEntity entity = userFeedbackMapper.selectById(id);
        if (Objects.nonNull(entity)) {
            if (StringUtils.isNotBlank(entity.getPhone())) {
                entity.setPhone(Sm4.decrypt(entity.getPhone()));
            }
        }
        return entity;
    }

    /**
     * 回复
     */
    public void replyUserFeedback(Long id, Long replyUserId, String replyUserName, Date replyTime,
                                  String replyContent) {
        UserFeedbackEntity updateEntity = new UserFeedbackEntity();
        updateEntity.setId(id);
        updateEntity.setReplyUserId(replyUserId);
        updateEntity.setReplyUserName(replyUserName);
        updateEntity.setReplyTime(replyTime);
        updateEntity.setReplyContent(replyContent);
        updateEntity.setStatus(EntityConstants.YES);
        userFeedbackMapper.updateById(updateEntity);
    }

    public void export(UserFeedbackQuery pageQuery) {
        ExcelDownloadDto<UserFeedbackQuery> excelDownloadDto = ExcelDownloadDto.<UserFeedbackQuery>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery)
                .title("图文列表").fileName("图文导出"+ "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("图文列表").build();
        excelExportService.excelExport(excelDownloadDto, UserFeedbackExportVo.class, this::executePageQuery);
    }

    private IPage<UserFeedbackExportVo> executePageQuery(ExcelDownloadDto<UserFeedbackQuery> userFeedbackQueryExcelDownloadDto) {
        IPage<UserFeedbackEntity> page = this.queryForPage(userFeedbackQueryExcelDownloadDto.getQuery());
        return page.convert(o -> {
            UserFeedbackExportVo exportVo = new UserFeedbackExportVo();
            BeanUtil.copyProperties(o, exportVo);
            return exportVo;
        });
    }
}
