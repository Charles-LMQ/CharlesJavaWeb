package cool.charles.framework;

import cool.charles.framework.helper.*;
import cool.charles.framework.util.ClassUtil;

public final class HelperLoader {
    public static void init() {
        Class<?>[] classes = {
            ClassHelper.class,
            BeanHelper.class,
            AopHelper.class,
            IocHelper.class,
            ControllerHelper.class
        };

        for(Class<?> claz : classes) {
            ClassUtil.loadClass(claz.getName(), true);
        }
    }
}
