package common.utility;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XR on 2016/8/31.
 */
public class StringUtility {
    public static List<Long> StringToListLong(String string){
        List<Long> list=new ArrayList<Long>();
        if (!StringUtils.isEmpty(string)){
            String[] longs=string.split(",");
            for (String l:longs) {
                if (!StringUtils.isEmpty(l)){
                    list.add(Long.parseLong(l));
                }
            }
        }
        return list;
    }
}
