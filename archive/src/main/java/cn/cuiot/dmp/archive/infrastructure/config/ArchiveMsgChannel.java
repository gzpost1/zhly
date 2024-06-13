package cn.cuiot.dmp.archive.infrastructure.config;//	模板

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 档案中心消息配置
 *
 * @author wuyongchong
 * @Description
 * @data 2024/5/27 10:11
 */
public interface ArchiveMsgChannel {

    String ARCHIVE_INPUT = "archiveInput";

    @Input(ARCHIVE_INPUT)
    SubscribableChannel archiveInput();

}