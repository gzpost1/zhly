package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询门禁点状态 resp
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikDoorStatesResp {

    /**
     * 有权限的门禁点状态集合
     */
    private List<AuthDoor> authDoorList;

    /**
     * 没有权限的门禁点集合
     */
    private List<String> noAuthDoorIndexCodeList;

    /**
     * 有权限的门禁点状态对象
     */
    @Data
    public static class AuthDoor {

        /**
         * 门禁点 indexCode
         */
        private String doorIndexCode;

        /**
         * 门状态，0 初始状态，1 开门状态，2 关门状态，3 离线状态
         */
        private Integer doorState;
    }
}
