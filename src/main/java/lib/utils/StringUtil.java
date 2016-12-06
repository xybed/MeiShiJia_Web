package lib.utils;

/**
 * Created by Administrator on 2016/11/24.
 */
public class StringUtil {
    /**
     * 判断字符串是否为空
     * @param s 字符串
     * @return true代表空，false代表不为空
     */
    public static boolean isEmpty(String s){
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        return s.trim().length() == 0;
    }

    /**
     * 去掉字符串中的4位中文编码，utf8最大是3位中文编码
     * @param str
     * @return
     */
    public static String filterUtf8mb4(String str){
        return str.replaceAll("[^\\u0020-\\u9FA5]", "");
    }
}
