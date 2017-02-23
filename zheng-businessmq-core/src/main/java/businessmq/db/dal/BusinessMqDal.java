package businessmq.db.dal;

import businessmq.db.BaseDB;
import businessmq.db.DbConfig;
import common.utility.DateUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/14.
 */
public class BusinessMqDal {
    /**
     * insert并返回主键
     * @param config
     * @param msg
     * @return
     */
    public Long insertMq(DbConfig config, String msg){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        connection=BaseDB.getConnection(config);
        try {
            preparedStatement=connection.prepareStatement("INSERT INTO tb_message(message,status,createtime) VALUES (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,msg);
            preparedStatement.setInt(2,0);
            preparedStatement.setString(3, DateUtility.getStrFromDate(new Date(),""));
            preparedStatement.execute();
            resultSet=preparedStatement.getGeneratedKeys();
            if (resultSet!=null&&resultSet.next()){
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return null;
    }

    public boolean updateMqStatus(DbConfig config, Long id, Integer status){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        connection=BaseDB.getConnection(config);
        try {
            preparedStatement=connection.prepareStatement("UPDATE tb_message SET status=?,updatetime=? WHERE id=?");
            int a=1;
            int b=2;
            int c=3;
            preparedStatement.setInt(a,status);
            preparedStatement.setString(b,DateUtility.getStrFromDate(new Date(),""));
            preparedStatement.setLong(c,id);
            if (preparedStatement.executeUpdate()>0){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDB.dispose(connection,preparedStatement,null);
        }
        return false;
    }
}
