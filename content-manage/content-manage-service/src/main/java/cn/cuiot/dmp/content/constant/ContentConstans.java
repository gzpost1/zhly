package cn.cuiot.dmp.content.constant;//	模板

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 17:59
 */
public class ContentConstans {

    public static class DataType {
        /**
         * 通知
         */
        public static final Byte NOTICE = 1;

        /**
         * 图文
         */
        public static final Byte IMG_TEXT = 2;
    }

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
         * 已国企
         */
        public static final Byte EXPIRED = 3;
    }
}
