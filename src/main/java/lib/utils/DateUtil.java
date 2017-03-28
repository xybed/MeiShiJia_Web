package lib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/28.
 */
public class DateUtil {

    /**
     * 获取系统时间，根据format的标准来格式化时间
     * @param format 需要让时间形成格式的format字符串
     * @return 时间字符串
     */
    public static String getTime(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }
}
