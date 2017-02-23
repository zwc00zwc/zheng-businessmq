package common.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MongodbManager {

    private static Properties props = new Properties();
    static {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mongo.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static MongoClient client=null;

    private static MongoDatabase mongoDatabase=null;

    private static MongoClient authClient=null;

    private static MongoDatabase authMongoDatabase=null;

    public static MongoDatabase getDatabase(String database){
        if (mongoDatabase==null){
            if (client==null){
                client=new MongoClient(props.getProperty("mongo_ip"),Integer.parseInt(props.getProperty("mongo_port")));
            }
            mongoDatabase=client.getDatabase(database);
        }
        return mongoDatabase;
    }

    /**
     * 认证
     * @return
     */
    public static MongoDatabase getAuthDatabase(){
        if (authMongoDatabase==null){
            if (authClient==null){
                MongoCredential credential = MongoCredential.createCredential(props.getProperty("db_username"), props.getProperty("db_name"), props.getProperty("db_password").toCharArray());
                authClient=new MongoClient(new ServerAddress(props.getProperty("mongo_ip"),Integer.parseInt(props.getProperty("mongo_port"))), Arrays.asList(credential));
            }
            authMongoDatabase=authClient.getDatabase(props.getProperty("db_name"));
        }
        return authMongoDatabase;
    }

    public static MongoDatabase getMongoClient(String host, int port,String database){
        MongoClient client=new MongoClient(host,port);

        return client.getDatabase(database);
    }

    public static MongoDatabase getMongoClient(String host, int port, String username, String database, String password){
        List<ServerAddress> addresses=new ArrayList<ServerAddress>();
        ServerAddress serverAddress=new ServerAddress(host,port);
        addresses.add(serverAddress);
        List<MongoCredential> credentials=new ArrayList<MongoCredential>();
        MongoCredential mongoCredential= MongoCredential.createScramSha1Credential(username,database,password.toCharArray());
        credentials.add(mongoCredential);
        MongoClient mongoClient=new MongoClient(addresses,credentials);
        return mongoClient.getDatabase(database);
    }

    /**
     * 插入
     * @param collection
     * @param basicDBObject  BasicDBObject实现DBObject接口
     */
    public static void insert(DBCollection collection,BasicDBObject basicDBObject){
        collection.insert(basicDBObject);
    }

    /**
     * 分页查询
     * @param collection 表名
     * @param query 查询条件
     * @param pageNo 页数
     * @param pageSize 页大小
     * @return
     */
    public static List<DBObject> queryPage(DBCollection collection,BasicDBObject query,Integer pageNo,Integer pageSize){
        List<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = collection.find(query).skip((pageNo-1)*pageSize).limit(pageSize);
        while (cursor.hasNext()){
            list.add(cursor.next());
        }
        return list;
    }

    /**
     * 查询
     * @param collection 表名
     * @param query 查询条件
     * @return
     */
    public static List<DBObject> query(DBCollection collection,BasicDBObject query){
        List<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()){
            list.add(cursor.next());
        }
        return list;
    }

    public static void log(){
        try {
            String collectionname="collectionname";
            MongoCollection collection= MongodbManager.getAuthDatabase().getCollection(collectionname);
            Document document=new Document();
            document.append("openId","openId");
            Date date=new Date();//取时间
            Calendar calendar   =   new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            document.append("createTime",calendar.getTime());
            collection.insertOne(document);
        } catch (Exception e) {
        }
    }

//    public static Object queryLog(){
//        String collectionname="collectionname";
//        MongoCollection collection= MongodbManager.getAuthDatabase().getCollection(collectionname);
//        List<OpenDownLoadDto> list=new ArrayList<>();
//        BasicDBObject basicDBObject=new BasicDBObject();
//        if (query.getOpenId()!=null&&query.getOpenId()>0){
//            basicDBObject.put("openId",query.getOpenId());
//        }
//        BasicDBObject timebasesic=new BasicDBObject();
//        Calendar calendar  =  new GregorianCalendar();
//        if (query.getStartTime()!=null){
//            calendar.setTime(query.getStartTime());
//            calendar.add(Calendar.HOUR,8);//时区关系加8小时
//            timebasesic.append("$gte",calendar.getTime());
//        }
//        if (query.getEndTime()!=null){
//            calendar.setTime(query.getEndTime());
//            calendar.add(Calendar.HOUR,8);//时区关系加8小时
//            timebasesic.append("$lte",calendar.getTime());
//        }
//        if (timebasesic.size()>0){
//            basicDBObject.put("createTime",timebasesic);
//        }
//        MongoCursor mongoCursor = collection.find(basicDBObject).sort(new BasicDBObject("createTime", -1)).skip(query.getStartRow()).limit(query.getPageSize()).iterator();
//        while (mongoCursor.hasNext()){
//            Document document=(Document) mongoCursor.next();
//            OpenDownLoadDto openDownLoadDto= new OpenDownLoadDto();
//            openDownLoadDto.setOpenId(Long.parseLong(document.get("openId").toString()));
//            openDownLoadDto.setMsg(document.get("msg").toString());
//            openDownLoadDto.setLoadUrl((String) document.get("loadUrl"));
//            calendar.setTime((Date) document.get("createTime"));
//            calendar.add(Calendar.HOUR,-8);//时区关系加8小时
//            openDownLoadDto.setCreateTime(calendar.getTime());
//            list.add(openDownLoadDto);
//        }
//        return list;
//    }

//    public static int queryLogCount(OpenDownLoadQuery query){
//        String collectionname="openload"+DateUtils.getStrFromDate(query.getCreateTime(),"yyyyMMdd");
//        MongoCollection collection= MongodbManager.getAuthDatabase().getCollection(collectionname);
//        int i=0;
//        BasicDBObject basicDBObject=new BasicDBObject();
//        if (query.getOpenId()!=null&&query.getOpenId()>0){
//            basicDBObject.put("openId",query.getOpenId());
//        }
//        BasicDBObject timebasesic=new BasicDBObject();
//        Calendar calendar   =   new GregorianCalendar();
//        if (query.getStartTime()!=null){
//            calendar.setTime(query.getStartTime());
//            calendar.add(Calendar.HOUR,8);//时区关系加8小时
//            timebasesic.append("$gte",calendar.getTime());
//        }
//        if (query.getEndTime()!=null){
//            calendar.setTime(query.getEndTime());
//            calendar.add(Calendar.HOUR,8);//时区关系加8小时
//            timebasesic.append("$lte",calendar.getTime());
//        }
//        if (timebasesic.size()>0){
//            basicDBObject.put("createTime",timebasesic);
//        }
//
//        MongoCursor mongoCursor = collection.find(basicDBObject).sort(new BasicDBObject("createTime", -1)).iterator();
//        while (mongoCursor.hasNext()){
//            i++;
//            mongoCursor.next();
//        }
//        return i;
//    }
}
