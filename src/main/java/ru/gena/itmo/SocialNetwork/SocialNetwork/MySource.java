package ru.gena.itmo.SocialNetwork.SocialNetwork;

import ru.gena.itmo.SocialNetwork.SocialNetwork.content.User;

import java.sql.*;

public class MySource {
    private static final String DB_URL = "jdbc:h2:./src/main/resources\\myDb";

    private Connection con = null;
    private static final MySource instance = new MySource();

    public static MySource getInstance(){
        return instance;
    }

    private MySource(){
        try{
            con = DriverManager
                    .getConnection(DB_URL, "gena","76odarom");
            //stat.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public User getUser(String login, String password) {
        try {
            String sqlQuery = "SELECT * FROM USERS WHERE USERNAME = " +
                    "'" + login + "'"
                     + " AND PASSWORD = " +
                    "'" + password + "'";
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            rs.next();
            return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"));
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getResultSet(String sqlQuery) {
        try {
            return con.createStatement().executeQuery(sqlQuery);
        }catch (SQLException e){
            return null;
        }
    }

    public boolean executeQuery(String sqlQuery) {
        try {
            con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                    .execute(sqlQuery);
            return true;
        }catch (SQLException e){
            return false;
        }
    }
}
