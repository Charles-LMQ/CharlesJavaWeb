package cool.charles.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public final class StringUtil {

    public static final String SEPARATOR = File.separator;

    public static boolean isEmpty(String string) {
        if(string != null) {
            string = string.trim();
        }
        return StringUtils.isEmpty(string);
    }

    public static boolean isNotEmpty(String string) {

        return !isEmpty(string);
    }

    public static String[] splitString(String origin, String regex) {
        return origin.split(regex);
    }

}
