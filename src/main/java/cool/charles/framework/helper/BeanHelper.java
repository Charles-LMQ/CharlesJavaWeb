package cool.charles.framework.helper;

import cool.charles.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {

    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass:beanClassSet) {
            Object object = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, object);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> claz) {
        if(!BEAN_MAP.containsKey(claz)) {
            throw new RuntimeException("can not get bean by class:" + claz);
        }
        return (T) BEAN_MAP.get(claz);
    }



}
