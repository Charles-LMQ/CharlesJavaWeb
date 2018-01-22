package cool.charles.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.net.URLEncoder;

@Slf4j
public final class CodecUtil {


    public static String encodeURL(String source) {
        String target;
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch(Exception e) {
            log.error("encode url failure {}", e);
            throw new RuntimeException(e);
        }

        return target;
    }

    public static String decodeURL(String source) {
        String target;
        try {
            target = URLDecoder.decode(source,"UTF-8");
        } catch (Exception e) {
            log.error("decode url failure", e);
            throw new RuntimeException(e);
        }

        return target;
    }


}
