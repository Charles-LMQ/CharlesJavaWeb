package cool.charles.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

@Slf4j
public final class FileUtil {

    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    public static File createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                FileUtils.forceMkdir(parentDir);
            }
        } catch (Exception e) {
            log.error("Create file failure, {}", e);
            throw new RuntimeException(e);
        }

        return file;
    }

}
