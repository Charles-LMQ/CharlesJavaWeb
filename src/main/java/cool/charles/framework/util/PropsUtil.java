package cool.charles.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public final class PropsUtil {

    public static Properties loadProps(String fileName) {
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw  new FileNotFoundException(fileName + "config file is not found.");
            }
            properties = new Properties();
            properties.load(inputStream);
        }
        catch (IOException e) {
            log.error("config file load failure {}", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }catch (IOException e) {
                    log.error("finish stream has problem {}", e);
                }
            }
        }
        return properties;
    }

    /**
     * get type String from value
     */
    public static String getString(Properties properties, String key) {
        return getString(properties,key,"");
    }

    public static String getString(Properties properties, String key, String value) {
        if(properties.containsKey(key)) {
            value = properties.getProperty(key);
        }

        return value;
    }

    /**
     * get type Integer from value
     */
    public static int getInt(Properties properties, String key) {
        return getInt(properties,key,0);
    }

    public static int getInt(Properties properties, String key, int value) {
        if(properties.containsKey(key)) {
            value = CastUtil.castInt(properties.getProperty(key));
        }

        return value;
    }

    /**
     * get type Boolean from value
     */
    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties,key,false);
    }

    public static boolean getBoolean(Properties properties, String key, Boolean value) {
        if(properties.containsKey(key)) {
            value = CastUtil.castBoolean(properties.getProperty(key));
        }

        return value;
    }




}
