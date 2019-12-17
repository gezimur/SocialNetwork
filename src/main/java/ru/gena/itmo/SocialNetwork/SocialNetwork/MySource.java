package ru.gena.itmo.SocialNetwork.SocialNetwork;

import ru.gena.itmo.SocialNetwork.SocialNetwork.content.Pattern;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.PatternsTree;
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
            //admin la-la-la
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

    public User getInformationOfUser(String id) {
        try {
            String sqlQuery = "SELECT * FROM USERS WHERE ID = " + id;
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            rs.next();
            return new User(
                    rs.getInt("id"),
                    "",
                    "",
                    rs.getString("firstname"),
                    rs.getString("lastname"));
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean changeInformationOfUser(String newName, String newSurname, String id){
        String sqlQuery = "UPDATE USERS SET FIRSTNAME = '" + newName + "'" +
                ", LASTNAME = '" + newSurname + "'" +
                " WHERE ID = " + id;
        return executeQuery(sqlQuery);
    }

    public boolean findUser(String login){
        try{
            String sqlQuery = "SELECT 1 FROM USERS WHERE USERNAME = '" +
                    login +
                    "' LIMIT 1;";
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            return rs.next();
        }catch(SQLException e){
            return false;
        }
    }

    public boolean addUser(User newUser){
        int id = 0;
        try{
            id = getResultSet("SELECT MAX(ID) AS ID FROM USERS")
                    .getInt("ID") + 1;
        }catch (SQLException e){ e.printStackTrace(); }
        if (id != 0) {
            return executeQuery("INSERT INTO USERS VALUES(" +
                    id + ", " +
                    "'" + newUser.getLogin() + "', " +
                    "'" + newUser.getPassword() + "', " +
                    "'" + newUser.getFirstname() + "', " +
                    "'" + newUser.getLastname() + "');");
        }
        return false;
    }

    public PatternsTree getPatternsTree(){
        try {
            String sqlQuery = "SELECT T.PATTERN, T.DESCENDANTS, P.PATTERNSNAME" +
                    " FROM PATTERNSTREE T INNER JOIN PATTERNS P ON T.DESCENDANTS = P.ID" +
            " ORDER BY T.PATTERN";
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            PatternsTree newTree = new PatternsTree();
            while (rs.next()){//сделать реализацию для Map(PATTERN, DESCENDANTS)
                int pattern = rs.getInt("PATTERN");
                int descendants = rs.getInt("DESCENDANTS");
                if (!newTree.isThereThisP(pattern)){
                    newTree.addLine(pattern);
                }
                if (!newTree.isThereThisP(descendants)){
                    newTree.addLine(descendants);
                }
                newTree.addValueInLine(pattern,
                        descendants,
                        rs.getString("PATTERNSNAME"));
            }
            return newTree;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Pattern getPattern(int id){
        try {
            String sqlQuery = "SELECT * FROM PATTERNS WHERE ID = " + id;
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            rs.next();
            return new Pattern(id,
                    rs.getString("PATTERNSNAME"),
                    rs.getString("SITESWAP"),
                    rs.getString("DESCRIPTION"));
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getResultSet(String sqlQuery) {
        try {
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            rs.next();
            return rs;
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
            e.printStackTrace();
            return false;
        }
    }
}
