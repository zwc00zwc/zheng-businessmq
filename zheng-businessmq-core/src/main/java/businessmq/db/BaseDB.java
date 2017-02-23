package businessmq.db;

import common.utility.PropertiesUtility;

import java.sql.*;

/**
 * Created by alan.zheng on 2017/2/9.
 */
public class BaseDB {
    public static Connection getConnection(DbConfig dbConfig){
        String driver =dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Connection conn = null;
        try {
            Class.forName(driver);
            conn =  DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getConnection(){
        PropertiesUtility propertiesUtility=new PropertiesUtility("job.properties");
        String driver =propertiesUtility.getProperty("jdbc.driver");
        String url = propertiesUtility.getProperty("jdbc.url");
        String username = propertiesUtility.getProperty("jdbc.username");
        String password = propertiesUtility.getProperty("jdbc.password");
        Connection conn = null;
        try {
            Class.forName(driver);
            conn =  DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void query(PreparedStatement preparedStatement,String[] args) throws SQLException {
        if (args!=null&&args.length>0){
            for (int i=1;i<=args.length;i++){
                preparedStatement.setString(i,args[i-1]);
            }
        }
    }

    public static void dispose(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement!=null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
