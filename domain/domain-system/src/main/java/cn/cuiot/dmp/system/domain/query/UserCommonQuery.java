package cn.cuiot.dmp.system.domain.query;

import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.domain.types.enums.UserTypeEnum;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * @Description 条件查询入参
 * @Author 犬豪
 * @Date 2023/8/29 16:16
 * @Version V1.0
 */
@Data
@Builder
public class UserCommonQuery {
    @Singular("id")
    private List<UserId> idList;
    private Password password;
    private String userId;
    private String username;
    private PhoneNumber phoneNumber;
    private Email email;
    @Singular("userTypeEnum")
    private List<UserTypeEnum> userTypeEnumList;
}
