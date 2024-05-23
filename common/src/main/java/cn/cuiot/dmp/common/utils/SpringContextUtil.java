package cn.cuiot.dmp.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Component
public class SpringContextUtil implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContextUtil.beanFactory = beanFactory;
    }

    public static <T> T getBean(String beanName) {
        if (null != beanFactory) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }

    public static <T> T getBean(Class<T> tClass) {
        if (null != beanFactory) {
            return beanFactory.getBean(tClass);
        }
        return null;
    }

}
