package cool.charles.framework.util;


public final class CastUtil {

    /**
     *
     * @param object
     * @return String
     */
    public static String castString(Object object) {
        return  CastUtil.castString(object,"");
    }

    public static String castString(Object object, String value) {
        return object !=null ? String.valueOf(object):value;
    }

    /**
     *
     * @param object
     * @return Double
     */
    public static double castDouble(Object object) {
        return  CastUtil.castDouble(object,0);
    }

    public static double castDouble(Object object, double value) {
        if(object != null) {
            String origin = castString(object);
            if(StringUtil.isNotEmpty(origin)) {
                try{
                    value = Double.parseDouble(origin);
                }catch (NumberFormatException e){

                }
            }
        }
        return value;
    }

    /**
     *
     * @param object
     * @return long
     */
    public static long castLong(Object object) {
        return  CastUtil.castLong(object,0l);
    }

    public static long castLong(Object object, long value) {
        if(object != null) {
            String origin = castString(object);
            if(StringUtil.isNotEmpty(origin)) {
                try{
                    value = Long.parseLong(origin);
                }catch (NumberFormatException e){

                }
            }
        }
        return value;
    }

    /**
     *
     * @param object
     * @return long
     */
    public static int castInt(Object object) {
        return  CastUtil.castInt(object,0);
    }

    public static int castInt(Object object, int value) {
        if(object != null) {
            String origin = castString(object);
            if(StringUtil.isNotEmpty(origin)) {
                try{
                    value = Integer.parseInt(origin);
                }catch (NumberFormatException e){

                }
            }
        }
        return value;
    }

    /**
     *
     * @param object
     * @return boolean
     */
    public static boolean castBoolean(Object object) {
        return  CastUtil.castBoolean(object,false);
    }

    public static boolean castBoolean(Object object, boolean value) {
        if(object != null) {
            value = Boolean.parseBoolean(castString(object));
        }
        return value;
    }


}
