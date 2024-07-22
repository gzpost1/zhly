package cn.cuiot.dmp.content.task;//	模板

import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.service.NoticeService;
import cn.hutool.core.collection.CollUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/12 10:54
 */
@Component
@Slf4j
public class NoticePublishTask {

    @Autowired
    private NoticeService noticeService;

    /**
     * 发布通知
     *
     * @param param
     * @return
     */
    @XxlJob("publishNotice")
    @Transactional
    public ReturnT<String> publishNotice(String param) {
        log.info("publishNotice start");
        List<ContentNoticeEntity> noticeEntityList = noticeService.queryPublishNotice();
        if (CollUtil.isEmpty(noticeEntityList)) {
            log.info("publishNotice end, no notice to publish");
            return ReturnT.SUCCESS;
        }
        noticeEntityList.forEach(noticeEntity -> {
            noticeEntity.setNoticed(ContentConstants.Noticed.NOTICED);
            noticeService.updateById(noticeEntity);
            noticeService.sendNoticeMessage(noticeEntity);
        });
        return ReturnT.SUCCESS;
    }

}
