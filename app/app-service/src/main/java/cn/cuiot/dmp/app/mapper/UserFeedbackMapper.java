package cn.cuiot.dmp.app.mapper;

import cn.cuiot.dmp.app.dto.UserFeedbackQuery;
import cn.cuiot.dmp.app.entity.UserFeedbackEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户反馈 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-14
 */
public interface UserFeedbackMapper extends BaseMapper<UserFeedbackEntity> {

    IPage<UserFeedbackEntity> queryForPage(Page<UserFeedbackEntity> page,@Param("param") UserFeedbackQuery query);

}
