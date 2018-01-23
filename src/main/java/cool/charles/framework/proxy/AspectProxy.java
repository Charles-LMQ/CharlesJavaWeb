package cool.charles.framework.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public abstract class AspectProxy implements Proxy {

    public final Object doProxy(ProxyChain proxyChain) throws  Throwable {
        Object result = null;
        Class<?> claz = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();

        try {
            if(intercept(claz, method, params)) {
                before(claz, method, params);
                result = proxyChain.doProxyChain();
                after(claz,method,params,result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            log.error("proxy failure {}", e);
            error(claz, method, params, e);
        } finally {
            end();
        }

        return result;
    }

    public void begin(){}

    public boolean intercept(Class<?> claz, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void before(Class<?> claz, Method method, Object[] params) throws Throwable {

    }

    public void after(Class<?> claz, Method method, Object[] params, Object result) throws Throwable {

    }

    public void error(Class<?> claz, Method method, Object[] params, Throwable throwable) throws Throwable {

    }

    public void end(){}

}
