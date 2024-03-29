package cn.cuiot.dmp.system.user_manage.domain.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * @Author 犬豪
 * @Date 2023/12/14 10:28
 * @Version V1.0
 */
@Data
public class SelfRegistrationAuditPermissions {


    private List<SelfRegistrationAuditPermission> data;

    public void addPermission(SelfRegistrationAuditPermission permission) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(permission);
    }

    public void addPermission(String code, String desc) {
        addPermission(new SelfRegistrationAuditPermission(code, desc));
    }

    @Data
    public static class SelfRegistrationAuditPermission {
        public SelfRegistrationAuditPermission() {
        }

        public SelfRegistrationAuditPermission(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 唯一标识
         */
        private String code;
        /**
         * 描述
         */
        private String desc;
    }

    public List<String> codeList() {
        return data != null ? data.stream().map(SelfRegistrationAuditPermission::getCode).collect(Collectors.toList()) : null;
    }

    public String desc() {
        return data != null ? data.stream().map(SelfRegistrationAuditPermission::getDesc).collect(Collectors.joining("、")) : null;
    }
}
