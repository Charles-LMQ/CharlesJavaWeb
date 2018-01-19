package cool.charles.framework.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public final class ReflectionUtil {

    public static Object newInstance(Class<?> claz) {
        Object instance;
        try {
            instance = claz.newInstance();
        } catch (Exception e) {
            log.error("new instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            log.error("set field failure {}", e);
            throw new RuntimeException(e);

        }
    }
}
