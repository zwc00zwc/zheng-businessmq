package common.utility;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by XR on 2016/8/30.
 */
public class BeanUtility {
    public static <T> DBObject beanToDbObject(T bean){
        if (bean==null){
            return null;
        }
        DBObject dbObject=new BasicDBObject();
        Field[] fields= bean.getClass().getFields();
        for (Field field:fields) {
            boolean accessible= field.isAccessible();
            if (!accessible){
                field.setAccessible(true);
            }
            try {
                dbObject.put(field.getName(),field.get(bean));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(accessible);
        }
        return dbObject;
    }

    public static <T> T dbObjectToBean(DBObject dbobject,T bean){
        if (bean == null) {
            return null;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            Object object = dbobject.get(varName);
            if (object != null) {
                try {
                    BeanUtils.setProperty(bean,varName,object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
