package cool.charles.framework.bean;

import cool.charles.framework.util.CastUtil;
import cool.charles.framework.util.CollectionUtil;
import cool.charles.framework.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class Parameter {

    private List<FormParameter> formParameterList;
    private List<FileParameter> fileParameterList;

    private List<Integer> integerList = Arrays.asList(1,2,3,4,5);

    public void test() {

        for(int num : integerList) {
            int i = 0;
            System.out.println("num:" + num);
            List<Integer> integerList = new ArrayList<Integer>();
            integerList.add(10);
            System.out.println("inner num:" + integerList.get(i));

            i++;
        }
    }


    public Parameter(List<FormParameter> formParameterList) {

        this.formParameterList = formParameterList;
    }

    public Parameter(List<FormParameter> formParameterList, List<FileParameter> fileParameterList) {

        this.formParameterList = formParameterList;
        this.fileParameterList = fileParameterList;
    }

    public Map<String, Object> getFieldMap() {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        if(CollectionUtil.isNotEmpty(formParameterList)) {
            for (FormParameter formParameter : formParameterList) {
                String fieldName = formParameter.getFieldName();
                Object fieldValue = formParameter.getFieldValue();
                if(fieldMap.containsKey(fieldName)) {
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName, fieldValue);
            }
        }

        return fieldMap;
    }

    public Map<String, List<FileParameter>> getFileMap() {
        Map<String, List<FileParameter>> fileMap = new HashMap<String, List<FileParameter>>();
        if(CollectionUtil.isNotEmpty(fileParameterList)) {
            for (FileParameter fileParameter : fileParameterList) {
                String fieldName = fileParameter.getFieldName();
                List<FileParameter> fileParameterList;
                if(fileMap.containsKey(fieldName)) {
                    fileParameterList = fileMap.get(fieldName);
                } else {
                    fileParameterList = new ArrayList<FileParameter>();
                }
                fileParameterList.add(fileParameter);
                fileMap.put(fieldName, fileParameterList);
            }
        }
        return fileMap;
    }

    public List<FileParameter> getFileList(String fieldName) {
        return getFileMap().get(fieldName);
    }

    public FileParameter getFile(String fieldName) {
        List<FileParameter> fileParameterList = getFileList(fieldName);
        if(CollectionUtil.isNotEmpty(fileParameterList) && fileParameterList.size() == 1) {
            return fileParameterList.get(0);
        }
        return null;
    }

    public boolean isEmpty() {
        return CollectionUtil.isEmpty(formParameterList) && CollectionUtil.isEmpty(fileParameterList);
    }

    public String getString(String name) {
        return CastUtil.castString(getFieldMap().get(name));
    }


    public long getLong(String name) {
        return CastUtil.castLong(getFieldMap().get(name));
    }

    public double getDouble(String name) {
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    public int getInt(String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }

    public boolean getBoolean(String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }

    public static void main(String[] args) {
            Parameter parameter = new Parameter(null, null);
            parameter.test();

    }


}
