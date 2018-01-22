package cool.charles.framework;

import cool.charles.framework.helper.BeanHelper;
import cool.charles.framework.helper.ClassHelper;
import cool.charles.framework.helper.ControllerHelper;
import cool.charles.framework.helper.IocHelper;
import cool.charles.framework.util.ClassUtil;

public final class HelperLoader {
    public static void init() {
        Class<?>[] classes = {
            ClassHelper.class,
            BeanHelper.class,
            IocHelper.class,
            ControllerHelper.class
        };

        for(Class<?> claz : classes) {
            ClassUtil.loadClass(claz.getName(), true);
        }
    }
}
