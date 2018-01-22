package cool.charles.framework.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            log.error("invoke method failure {}", e);
            throw new RuntimeException(e);
        }
        return result;
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
