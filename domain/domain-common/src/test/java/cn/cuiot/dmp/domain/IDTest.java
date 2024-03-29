package cn.cuiot.dmp.domain;

import cn.cuiot.dmp.domain.types.id.UserId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class IDTest {
    @Test
    public void testId() {
        UserId userId = new UserId(123L);
        System.out.println(userId);
        UserId userId2 = new UserId(1234L);
        System.out.println(userId2);
        Assert.assertFalse(userId.equals(userId2));

        String id = null;
        UserId userIdNull = new UserId(id);
        System.out.println(userIdNull.getValue());
        System.out.println(userIdNull.getStrValue());


        List<UserId> userIdList = new ArrayList<>();
        userIdList.add(new UserId(id));
        userIdList.add(new UserId(id));
        userIdList.add(new UserId(id));
        List<Long> collect = userIdList.stream().map(UserId::getValue).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(collect.size());
    }
}
