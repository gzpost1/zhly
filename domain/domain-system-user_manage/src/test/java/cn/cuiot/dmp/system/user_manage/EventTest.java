package cn.cuiot.dmp.system.user_manage;

import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.event.ResetPasswordSuccessEvent;
import cn.cuiot.dmp.system.user_manage.domain.event.UserUpdateEvent;
import org.junit.Test;

/**
 * @Author 犬豪
 * @Date 2023/8/31 14:28
 * @Version V1.0
 */
public class EventTest {

    @Test
    public void testEvent() {
        UserUpdateEvent build = ResetPasswordSuccessEvent.builder().user(User.builder().build()).build();
        System.out.println(build);
    }
}
