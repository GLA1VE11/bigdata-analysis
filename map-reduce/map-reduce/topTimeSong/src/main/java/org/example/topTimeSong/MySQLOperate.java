package org.example.topTimeSong;

import org.example.topTimeSong.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLOperate {

    public void insert(String song,int play){
        PreparedStatement preparedStatement=null;
        Connection connection = MySQLConnection.getConnection();
        String sql="insert into topTimeSong values(?,?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, song);
            preparedStatement.setInt(2, play);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert Error...");
            e.printStackTrace();
        }
    }    //清空表的数据
    public void clear(){
        PreparedStatement preparedStatement=null;
        Connection connection = MySQLConnection.getConnection();
        String sql="truncate table topTimeSong";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Clear Error");
            e.printStackTrace();
        }
    }
}
