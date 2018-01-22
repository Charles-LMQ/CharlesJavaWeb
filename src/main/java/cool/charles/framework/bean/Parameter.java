package cool.charles.framework.bean;

import cool.charles.framework.util.CastUtil;

import java.util.Map;

public class Parameter {
    private Map<String, Object> parameterMap;

    public Parameter(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public long getLong(String name) {
        return CastUtil.castLong(parameterMap.get(name));
    }

    public Map<String, Object> getParameterMap() {
        return this.parameterMap;
    }
}
