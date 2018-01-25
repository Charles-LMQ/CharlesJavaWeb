package cool.charles.framework.helper;

import cool.charles.framework.bean.FileParameter;
import cool.charles.framework.bean.FormParameter;
import cool.charles.framework.bean.Parameter;
import cool.charles.framework.util.CollectionUtil;
import cool.charles.framework.util.FileUtil;
import cool.charles.framework.util.StreamUtil;
import cool.charles.framework.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class UploadHelper {

    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));

        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit != 0) {
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
        }
    }

    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }


    public static Parameter createParameter(HttpServletRequest request) throws IOException {
        List<FormParameter> formParameterList = new ArrayList<FormParameter>();
        List<FileParameter> fileParameterList = new ArrayList<FileParameter>();

        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);

            if (CollectionUtil.isNotEmpty(fileItemListMap)) {
                for (Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()) {
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if (CollectionUtil.isNotEmpty(fileItemList)) {
                        for (FileItem fileItem : fileItemList) {
                            if (fileItem.isFormField()) {
                                String fieldValue = fileItem.getString("UTF-8");
                                formParameterList.add(new FormParameter(fieldName, fieldValue));
                            } else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                                if (StringUtil.isNotEmpty(fileName)) {
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileParameterList.add(new FileParameter(fieldName, fileName, fileSize, contentType, inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            log.error("Create parameter failure, {}", e);
            throw new RuntimeException(e);
        }

        return new Parameter(formParameterList, fileParameterList);
    }


    public static void uploadFile(String basePath, FileParameter fileParam) {
        try {
            if(fileParam != null) {
                String filePath = basePath + fileParam.getFieldName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (Exception e) {
            log.error("Upload file failure, {}", e);
            throw new RuntimeException(e);
        }
    }

    public static void uploadFiles(String basePath, List<FileParameter> fileParameterList) {
        try {
            if(CollectionUtil.isNotEmpty(fileParameterList)) {
                for(FileParameter fileParameter : fileParameterList) {
                    uploadFile(basePath, fileParameter);
                }
            }
        } catch (Exception e) {
            log.error("Upload file failure, {}", e);
            throw new RuntimeException(e);
        }
    }

}
