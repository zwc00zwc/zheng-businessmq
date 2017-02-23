package common.utility;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by Administrator on 2017/1/18.
 */
public class DateUtility {
    public static String getStrFromDate(Date date,String format){
        DateFormat dateFormat=null;
        if (StringUtils.isEmpty(format)){
            dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else {
            dateFormat=new SimpleDateFormat(format);
        }
        return dateFormat.format(date);
    }

    public static Date getDateFromStr(String datestr,String format){
        DateFormat dateFormat=null;
        if (StringUtils.isNotEmpty(format)){
            dateFormat = new SimpleDateFormat(format);
        }else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        if (StringUtils.isNotEmpty(datestr)){
            try {
                return dateFormat.parse(datestr);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}
