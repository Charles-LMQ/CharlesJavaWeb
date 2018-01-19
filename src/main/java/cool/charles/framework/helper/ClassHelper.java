package cool.charles.framework.helper;

import cool.charles.framework.annotation.Controller;
import cool.charles.framework.annotation.Service;
import cool.charles.framework.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> claz : classSet) {
            if(claz.isAnnotationPresent(Service.class)) {
                classSet.add(claz);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> claz : classSet) {
            if(claz.isAnnotationPresent(Controller.class)) {
                classSet.add(claz);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        beanSet.addAll(getServiceClassSet());
        beanSet.addAll(getControllerClassSet());
        return beanSet;
    }
}
