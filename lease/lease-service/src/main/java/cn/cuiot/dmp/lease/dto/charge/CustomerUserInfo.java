package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.enums.CustomerIdentityTypeEnum;
import cn.cuiot.dmp.util.Sm4;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @Description 用户信息
 * @Date 2024/6/19 14:55
 * @Created by libo
 */
@Data
public class CustomerUserInfo {
    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    private String customerUserName;

    /**
     * 客户手机号
     */
    private String customerUserPhone;

    /**
     * 客户身份
     */
    private String customerUserRoleName;

    /**
     * 业主身份
     */
    private Byte identityType;

    /**
     * 房屋ID
     */
    private Long houseId;

    public String getCustomerUserPhone() {
        if(StringUtils.isNotBlank(customerUserPhone)){
            return Sm4.decrypt(customerUserPhone);
        }

        return null;
    }

    public String getCustomerUserRoleName() {
        if(Objects.nonNull(getIdentityType())){
            CustomerIdentityTypeEnum customerIdentityTypeEnum = CustomerIdentityTypeEnum.parseByCode(getIdentityType().toString());
            return customerIdentityTypeEnum.getName();
        }
        return null;
    }
}
