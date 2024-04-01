package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 通用断言工具
 *
 * @author: wuyongchong
 * @date: 2024/4/1 20:17
 */
public final class AssertUtil {

    public static ResultCode DEFAULT_ERROR_CODE = ResultCode.PARAM_NOT_COMPLIANT;

    public static void isEq(Object a, Object b, RuntimeException re) {
        if (!String.valueOf(a).equals(String.valueOf(b))) {
            throw re;
        }
    }

    public static void state(boolean expression, RuntimeException re) {
        if (!expression) {
            throw re;
        }
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }


    public static void isTrue(boolean expression, RuntimeException re) {
        if (!expression) {
            throw re;
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void isFalse(boolean expression, RuntimeException re) {
        if (expression) {
            throw re;
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void isBlank(String text, RuntimeException re) {
        if (StringUtils.isNotBlank(text)) {
            throw re;
        }
    }

    public static void isBlank(String text, String message) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void notBlank(String text, RuntimeException re) {
        if (StringUtils.isBlank(text)) {
            throw re;
        }
    }

    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void isNull(Object object, RuntimeException re) {
        if (object != null) {
            throw re;
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void notNull(Object object, RuntimeException re) {
        if (object == null) {
            throw re;
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void hasLength(String text, RuntimeException re) {
        if (StringUtils.isBlank(text)) {
            throw re;
        }
    }

    public static void hasLength(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void hasText(String text, RuntimeException re) {
        if (StringUtils.isBlank(text)) {
            throw re;
        }
    }

    public static void hasText(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring,
            RuntimeException re) {
        if (StringUtils.isNotBlank(textToSearch) && StringUtils.isNotBlank(substring)
                && textToSearch
                .contains(substring)) {
            throw re;
        }
    }

    public static void doesNotContain(String textToSearch, String substring,
            String message) {
        if (StringUtils.isNotBlank(textToSearch) && StringUtils.isNotBlank(substring)
                && textToSearch
                .contains(substring)) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void notEmpty(Object[] array, RuntimeException re) {
        if (null == array || array.length == 0) {
            throw re;
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (null == array || array.length == 0) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void notEmpty(Collection<?> collection, RuntimeException re) {
        if (CollectionUtils.isEmpty(collection)) {
            throw re;
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void notEmpty(Map<?, ?> map, RuntimeException re) {
        if (null == map || map.size() == 0) {
            throw re;
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (null == map || map.size() == 0) {
            throw new BusinessException(DEFAULT_ERROR_CODE, message);
        }
    }

    public static void noNullElements(Object[] array, RuntimeException re) {
        if (array != null) {
            Object[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                if (element == null) {
                    throw re;
                }
            }
        }
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            Object[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                if (element == null) {
                    throw new BusinessException(DEFAULT_ERROR_CODE, message);
                }
            }
        }
    }

}