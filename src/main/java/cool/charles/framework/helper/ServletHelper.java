package cool.charles.framework.helper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public final class ServletHelper {
    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_THREAD_LOCAL = new ThreadLocal<ServletHelper>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    private ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER_THREAD_LOCAL.set(new ServletHelper(request, response));
    }

    public static void destory() {
        SERVLET_HELPER_THREAD_LOCAL.remove();
    }

    public static void setRequestAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRequestAttribute(String key) {
        return (T) getRequest().getAttribute(key);
    }

    public static void remoteRequestAttribute(String key) {
        getRequest().removeAttribute(key);
    }

    public static void sendRedirect(String location) {
        try {
            getResponse().sendRedirect(getRequest().getContextPath() + location);
        } catch (IOException e) {
            log.error("redirect failure, {}", e);
        }
    }

    public static void setSessionAttribute(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(String key) {
        return (T) getRequest().getSession().getAttribute(key);
    }

    public static void removeSessionAttribute(String key) {
        getRequest().getSession().removeAttribute(key);
    }

    public static void invalidateSession() {
        getRequest().getSession().invalidate();
    }


    /**
     * private functions
     * @return
     */

    private static HttpServletRequest getRequest() {
        return  SERVLET_HELPER_THREAD_LOCAL.get().request;
    }

    private static HttpServletResponse getResponse() {
        return  SERVLET_HELPER_THREAD_LOCAL.get().response;
    }

    private static HttpSession getSession() {
        return getRequest().getSession();
    }

    private static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

}
