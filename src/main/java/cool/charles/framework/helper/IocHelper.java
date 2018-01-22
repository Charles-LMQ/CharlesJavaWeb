package cool.charles.framework.helper;

import cool.charles.framework.annotation.Inject;
import cool.charles.framework.util.ArrayUtil;
import cool.charles.framework.util.CollectionUtil;
import cool.charles.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class IocHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)) {
            for(Map.Entry<Class<?>, Object> beanEntry: beanMap.entrySet()) {
                Class<?> beanCalss = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                Field[] beanFields = beanCalss.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)) {
                    for(Field beanField : beanFields) {
                        if(beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if(beanFieldInstance != null) {
                                ReflectionUtil.setField(beanInstance, beanField , beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
