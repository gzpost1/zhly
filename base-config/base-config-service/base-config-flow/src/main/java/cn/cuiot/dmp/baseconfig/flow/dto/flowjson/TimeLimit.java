package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

/**
 * @Description 超时设置
 * @Date 2024/4/25 9:58
 * @Created by libo
 */
@Data
public class TimeLimit {
    /**
     * 超时时间
     */
    private TimeOut timeout;
    /**
     * 超时处理
     */
    private Handler handler;

    @Data
    public static class TimeOut {
        /**
         * 时间单位 D天 H小时 M分钟
         */
        private String unit;
        /**
         * 时间值
         */
        private String value;
    }

    @Data
    public static class Handler {
        /**
         * 处理类型
         */
        private String type;
        /**
         * 通知设置
         */
        private Notify notify;

        @Data
        public static class Notify {
            /**
             * 是否通知一次
             */
            private Boolean once;
            /**
             * 通知时间间隔
             */
            private Integer hour;
        }
    }
}
