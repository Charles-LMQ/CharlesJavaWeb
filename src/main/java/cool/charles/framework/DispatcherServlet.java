package cool.charles.framework;

import cool.charles.framework.bean.Data;
import cool.charles.framework.bean.Handler;
import cool.charles.framework.bean.Parameter;
import cool.charles.framework.bean.View;
import cool.charles.framework.helper.BeanHelper;
import cool.charles.framework.helper.ConfigHelper;
import cool.charles.framework.helper.ControllerHelper;
import cool.charles.framework.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        HelperLoader.init();

        ServletContext servletContext = servletConfig.getServletContext();

        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if(handler != null) {
            Class<?> controllerClaz = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClaz);

            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = req.getParameterNames();
            while(paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }

            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)) {
                String[] params = StringUtil.splitString(body, "&");
                if(ArrayUtil.isNotEmpty(params)) {
                    for(String param: params) {
                        String[] array = StringUtil.splitString(param, "=");
                        if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
                            String paraName = array[0];
                            String paraValue = array[1];
                            paramMap.put(paraName, paraValue);
                        }
                    }
                }

                Parameter parameter = new Parameter(paramMap);

                Object result;
                Method routeMethod = handler.getRouteMethod();
                if(parameter.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, routeMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, routeMethod, parameter);
                }

                if(result instanceof View) {
                    View view = (View) result;
                    String path = view.getPath();
                    if(StringUtil.isNotEmpty(path)) {
                        if(path.startsWith("/")) {
                            res.sendRedirect(req.getContextPath() + path);
                        } else {
                            Map<String, Object> model = view.getModel();
                            for(Map.Entry<String, Object>entry : model.entrySet()) {
                                req.setAttribute(entry.getKey(), entry.getValue());
                            }

                            req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, res);
                        }
                    } else if (result instanceof Data) {
                        Data data = (Data) result;
                        Object model = data.getModel();
                        if(model != null) {
                            res.setContentType("application/json");
                            res.setCharacterEncoding("UTF-8");
                            PrintWriter writer = res.getWriter();
                            String json = JsonUtil.toJSON(model);
                            writer.write(json);
                            writer.flush();

                        }
                    }
                }
            }
        }

    }
}
