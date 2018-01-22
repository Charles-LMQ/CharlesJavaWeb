package cool.charles.framework.bean;

import java.lang.reflect.Method;

public class Handler {

    private Class<?> controllerClass;

    private Method routeMethod;

    public Handler(Class<?> controllerClass, Method routeMethod) {
        this.controllerClass = controllerClass;
        this.routeMethod = routeMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getRouteMethod() {
        return routeMethod;
    }
}
