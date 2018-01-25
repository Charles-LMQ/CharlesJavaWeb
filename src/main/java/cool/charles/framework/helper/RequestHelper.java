package cool.charles.framework.helper;

import cool.charles.framework.bean.FormParameter;
import cool.charles.framework.bean.Parameter;
import cool.charles.framework.util.ArrayUtil;
import cool.charles.framework.util.CodecUtil;
import cool.charles.framework.util.StreamUtil;
import cool.charles.framework.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class RequestHelper {

    public static Parameter createParameter(HttpServletRequest request) throws IOException {
        List<FormParameter> formParameterList = new ArrayList<FormParameter>();
        formParameterList.addAll(parseParameterNames(request));
        formParameterList.addAll(parseInputStream(request));
        return new Parameter(formParameterList);
    }

    private static List<FormParameter> parseParameterNames(HttpServletRequest request) {
        List<FormParameter> formParameterList = new ArrayList<FormParameter>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)) {
                Object fieldValue;
                if (fieldValues.length == 1) {
                    fieldValue = fieldValues[0];
                } else {
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < fieldValues.length; i++) {
                        sb.append(fieldValues[i]);
                        if (i != fieldValues.length - 1) {
                            sb.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue = sb.toString();
                }
                formParameterList.add(new FormParameter(fieldName, fieldValue));
            }
        }
        return formParameterList;
    }


    private static List<FormParameter> parseInputStream(HttpServletRequest request) throws IOException {
        List<FormParameter> formParameterList = new ArrayList<FormParameter>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if(StringUtil.isNotEmpty(body)) {
            String[] kvs = StringUtil.splitString(body, "&");
            if(ArrayUtil.isNotEmpty(kvs)) {
                for (String kv : kvs) {
                    String[] array = StringUtil.splitString(kv, "=");
                    if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParameterList.add(new FormParameter(fieldName, fieldValue));
                    }
                }
            }
        }
        return formParameterList;
    }
}
