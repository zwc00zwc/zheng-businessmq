package businessmq.db.dal;

import businessmq.db.BaseDB;
import businessmq.db.DbConfig;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.db.model.businessmq.query.BusinessMqNodeQuery;
import common.utility.DateUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alan.zheng on 2017/2/15.
 */
public class BusinessMqNodeDal {
    public List<BusinessMqNode> queryNodeList(DbConfig dbConfig){
        List<BusinessMqNode> nodeList=new ArrayList<>();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        connection= BaseDB.getConnection(dbConfig);
        try {
            preparedStatement=connection.prepareStatement("SELECT `id`,`node`,`driver`,`url`,`username`,`password` FROM tb_businessmq_node");
            resultSet=preparedStatement.executeQuery();
            nodeList= resultToBusinessMqNode(resultSet);
        } catch (SQLException e) {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return nodeList;
    }

    public BusinessMqNode queryById(Long nodeId){
        BusinessMqNode node=new BusinessMqNode();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        connection= BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("SELECT `id`,`node`,`driver`,`url`,`username`,`password` FROM tb_businessmq_node");
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                node=new BusinessMqNode();
                node.setId(resultSet.getLong("id"));
                node.setNode(resultSet.getInt("node"));
                node.setDriver(resultSet.getString("driver"));
                node.setUrl(resultSet.getString("url"));
                node.setUsername(resultSet.getString("username"));
                node.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return node;
    }

    public List<BusinessMqNode> queryPageList(BusinessMqNodeQuery query){
        List<BusinessMqNode> nodeList=new ArrayList<>();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        connection= BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("SELECT `id`,`node`,`driver`,`url`,`username`,`password` FROM tb_businessmq_node limit ?,?");
            int i=1;
            int m=2;
            preparedStatement.setInt(i,query.getStartRow());
            preparedStatement.setInt(m,query.getPageSize());
            resultSet=preparedStatement.executeQuery();
            nodeList= resultToBusinessMqNode(resultSet);
        } catch (SQLException e) {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return nodeList;
    }

    public int queryCountPage(BusinessMqNodeQuery query){
        Integer count=null;
        List<BusinessMqNode> nodeList=new ArrayList<>();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        connection= BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("SELECT COUNT(*) FROM tb_businessmq_node");
            resultSet=preparedStatement.executeQuery();
            count=resultSet.getInt(1);
        } catch (SQLException e) {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return count;
    }

    public boolean insertNode(BusinessMqNode node){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        connection=BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("INSERT INTO tb_businessmq_node(node,driver,url,username,password) VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1,node.getNode());
            preparedStatement.setString(2,node.getDriver());
            preparedStatement.setString(3,node.getUrl());
            preparedStatement.setString(4,node.getUsername());
            preparedStatement.setString(5,node.getPassword());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDB.dispose(connection,preparedStatement,null);
        }
        return false;
    }

    public boolean deleteNode(Long nodeId){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        connection=BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("DELETE FROM tb_businessmq_node WHERE id=?");
            preparedStatement.setLong(1,nodeId);
            if (preparedStatement.executeUpdate()>0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDB.dispose(connection,preparedStatement,null);
        }
        return false;
    }

    private List<BusinessMqNode> resultToBusinessMqNode(ResultSet resultSet){
        List<BusinessMqNode> list=new ArrayList<BusinessMqNode>();
        BusinessMqNode node=null;
        try {
            while (resultSet.next()){
                node=new BusinessMqNode();
                node.setId(resultSet.getLong("id"));
                node.setNode(resultSet.getInt("node"));
                node.setDriver(resultSet.getString("driver"));
                node.setUrl(resultSet.getString("url"));
                node.setUsername(resultSet.getString("username"));
                node.setPassword(resultSet.getString("password"));
                list.add(node);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
