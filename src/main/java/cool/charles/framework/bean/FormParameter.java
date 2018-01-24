package cool.charles.framework.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@AllArgsConstructor
@Getter
public class FormParameter {

    private String fieldName;
    private Object fieldValue;

}
