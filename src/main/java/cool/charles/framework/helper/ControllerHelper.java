package cool.charles.framework.helper;

import cool.charles.framework.annotation.Route;
import cool.charles.framework.bean.Handler;
import cool.charles.framework.bean.Request;
import cool.charles.framework.util.ArrayUtil;
import cool.charles.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerHelper {
    private static final Map<Request, Handler> ROUTE_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtil.isNotEmpty(controllerClassSet)) {
            for(Class<?> contollerClaz : controllerClassSet) {
                Method[] methods = contollerClaz.getDeclaredMethods();
                if(ArrayUtil.isNotEmpty(methods)) {
                    for(Method method : methods) {
                        if(method.isAnnotationPresent(Route.class)) {
                            Route route = method.getAnnotation(Route.class);
                            String mapping = route.value();
                            if(mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    String requstMethod = array[0];
                                    String requstPath = array[1];
                                    Request request = new Request(requstMethod, requstPath);
                                    Handler handler = new Handler(contollerClaz, method);
                                    ROUTE_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return  ROUTE_MAP.get(request);
    }
}
