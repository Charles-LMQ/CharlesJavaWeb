package cool.charles.framework;

import cool.charles.framework.bean.Data;
import cool.charles.framework.bean.Handler;
import cool.charles.framework.bean.Parameter;
import cool.charles.framework.bean.View;
import cool.charles.framework.helper.*;
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

        UploadHelper.init(servletContext);

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        ServletHelper.init(req, res);

        try {

            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();

            if (requestPath.equals("/favicon.ico")) {
                return;
            }

            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if(handler != null) {
                Class<?> controllerClaz = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClaz);

                Parameter parameter;

                if(UploadHelper.isMultipart(req)) {
                    parameter = UploadHelper.createParameter(req);
                } else {
                    parameter = RequestHelper.createParameter(req);
                }

                Object result;
                Method routeMethod = handler.getRouteMethod();

                if(parameter.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, routeMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, routeMethod, parameter);
                }

                if(result instanceof View) {
                    handlerViewResult((View) result, req, res);
                } else if (result instanceof Data) {
                    handlerDataResult((Data) result, res);
                }

            }

        } finally {
            ServletHelper.destory();
        }

    }


    private void handlerViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    private void handlerDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJSON(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
