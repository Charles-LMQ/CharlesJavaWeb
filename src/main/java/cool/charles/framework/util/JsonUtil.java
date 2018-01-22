package cool.charles.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String toJSON(T object) {
        String json;
        try{
            json = OBJECT_MAPPER.writeValueAsString(object);
        } catch(Exception e) {
            log.error("convert POJO to JSON failure {}", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T fromJSON(String json, Class<T> type) {
        T pojo;
        try{
            pojo = OBJECT_MAPPER.readValue(json, type);
        }catch (Exception e) {
            log.error("convert JSON to POJO failure {}", e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
