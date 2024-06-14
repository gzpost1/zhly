package cn.cuiot.dmp.content.constant;//	模板


/**
 * @author hantingyao
 * @Description 内容中心模块常量定义
 * @data 2024/5/29 17:59
 */
public class ContentConstants {

    /**
     * 数据类型常量定义
     */
    public static class DataType {
        /**
         * 通知
         */
        public static final Byte NOTICE = 1;

        /**
         * 图文
         */
        public static final Byte IMG_TEXT = 2;

        public static String getDataService(Byte dataType) {
            switch (dataType) {
                case 1:
                    return "noticeService";
                case 2:
                    return "imgTextService";
                default:
                    return null;
            }
        }
    }

    /**
     * 发布状态常量定义
     */
    public static class PublishStatus {
        /**
         * 未发布
         */
        public static final Byte UNPUBLISHED = 1;

        /**
         * 已发布
         */
        public static final Byte PUBLISHED = 2;

        /**
         * 已过期
         */
        public static final Byte EXPIRED = 3;

        /**
         * 停止发布
         */
        public static final Byte STOP_PUBLISH = 4;
    }

    /**
     * 审核状态常量定义
     */
    public static class AuditStatus {
        /**
         * 未审核
         */
        public static final Byte AUDIT_ING = 1;

        /**
         * 审核通过
         */
        public static final Byte AUDIT_PASSED = 2;

        /**
         * 审核不通过
         */
        public static final Byte NOT_PASSED = 3;
    }

    public static class ShowStatus {
        /**
         * 不展示
         */
        public static final Byte NOT_SHOW = 0;

        /**
         * 展示
         */
        public static final Byte SHOW = 1;
    }

    public static class PublishSource {
        /**
         * 管理端
         */
        public static final Byte MANAGE = 1;

        /**
         * 客户端
         */
        public static final Byte APP = 2;
    }

    public static class MsgInform {
        /**
         * 系统内通知
         */
        public static final Byte SYSTEM = 1;

        /**
         * 短信通知
         */
        public static final Byte SMS = 2;
    }
}
