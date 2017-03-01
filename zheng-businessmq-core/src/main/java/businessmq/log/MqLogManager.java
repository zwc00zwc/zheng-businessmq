package businessmq.log;

import com.mongodb.client.MongoCollection;
import common.mongodb.MongodbManager;
import common.utility.DateUtility;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by alan.zheng on 2017/2/13.
 */
public class MqLogManager {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MqLogManager.class);
    public static void log(String listener,String log,Date logTime){
        try {
            String collectionname= DateUtility.getStrFromDate(new Date(),"yyyyMMdd")+"_log";
            MongoCollection collection= MongodbManager.getDatabase("BusinessMqLog").getCollection(collectionname);
            Document document=new Document();
            document.append("logLabel",listener);
            document.append("log",log);
            Calendar calendar   =   new GregorianCalendar();
            calendar.setTime(logTime);
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            document.append("createTime",calendar.getTime());
            collection.insertOne(document);
        } catch (Exception e) {
            logger.error("mongo异常:"+e.toString());
            logger.error(log);
        }
    }
}
