package cool.charles.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class Request {

    private String requestMethod;

    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public String getRequestPath() {
        return this.requestPath;
    }

    @Override
    public boolean equals(Object o) {

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {

        return HashCodeBuilder.reflectionHashCode(this);
    }
}
