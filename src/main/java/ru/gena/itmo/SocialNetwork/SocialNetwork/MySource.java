package ru.gena.itmo.SocialNetwork.SocialNetwork;

import ru.gena.itmo.SocialNetwork.SocialNetwork.content.Message;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.Pattern;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.PatternsTree;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.User;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySource {

    //@Value("${spring.datasource.url}")
    private String dbUrl = "jdbc:postgresql://ec2-107-22-234-204.compute-1.amazonaws.com:5432/d2kohhhgqe3rso";

    private Connection con = null;
    private static final MySource instance = new MySource();

    public static MySource getInstance(){
        return instance;
    }

    private MySource(){
        try{
            con = DriverManager.getConnection(
                    dbUrl,
                    "rergtqommbarxw",
                    "cd5ee47b2176482b064ea9f7d236d9b2abde303fadabc470d64566d16e9716d2");
            //new Preparer().preparer();
            //admin la-la-la
        }catch (SQLException e){
            System.out.println("LOl\n\n");
            e.printStackTrace();
            System.out.println("\n\n");
        }catch (NullPointerException e){
            System.out.println("LO2\n\n");
            e.printStackTrace();
            System.out.println("\n\n");
        }//*/
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
                    rs.getString("usersStatus"),
                    rs.getString("firstName"),
                    rs.getString("lastName"));
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public User getInformationOfUser(String id) {
        try {
            String sqlQuery = "SELECT * FROM USERS WHERE ID = " + id;
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            if (!rs.next()) return null;
            return new User(
                    rs.getInt("id"),
                    "",
                    "",
                    "",
                    rs.getString("firstname"),
                    rs.getString("lastname"));
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void changeInformationOfUser(String newName, String newSurname, String id){
        String sqlQuery = "UPDATE USERS SET FIRSTNAME = '" + newName + "'" +
                ", LASTNAME = '" + newSurname + "'" +
                " WHERE ID = " + id;
        executeQuery(sqlQuery);
    }

    public String getPagingOfUsersConversations(String userId, int page, int itemPerPage){
        String sqlQuery = "SELECT C.NAME AS NAME, C.ID AS ID" +
                " FROM CONVERSATIONS C INNER JOIN USERS_CONVERSATIONS U ON C.ID = U.CONVERSATION" +
                " WHERE U.MEMBER = " + userId;
        return getPaging("conversation", sqlQuery, page, itemPerPage);
    }

    public String getPagingOfUsers(String name, String surname, int page, int itemPerPage){
        String sqlQuery = "SELECT CONCAT(FIRSTNAME, ' ', LASTNAME) AS NAME, ID" +
                " FROM USERS" +
                " WHERE FIRSTNAME = '" + name + "' OR" +
                " LASTNAME = '" + surname + "';";
        return getPaging("profile" ,sqlQuery, page, itemPerPage);
    }

    private String getPaging(String objects, String sqlQuery, int page, int itemPerPage){
        ResultSet rs = getResultSet(sqlQuery);
        if (rs != null){
            StringBuilder sb = new StringBuilder();
            Designer d = new Designer();
            try{
                int p = (page * itemPerPage - 1 > 0)? page * itemPerPage - 1 : 0;
                rs.absolute(p);
                while(rs.next()){
                    sb.append("<span class=\"message\"><a href=\"/");
                    sb.append(objects);
                    sb.append("/id");
                    sb.append( d.toNeddedForm("" + rs.getInt("id")) );
                    sb.append("\">");
                    sb.append(rs.getString("name"));
                    sb.append("</a><span class=\"invitation\">\n");
                    sb.append("add to conv\n<input type=\"text\" style=\"width: 100px;\" ");
                    if ("profile".equals(objects)){
                        sb.append("placeholder=\"conv\">");
                    }else{
                        sb.append("placeholder=\"juggler\">");
                    }
                    sb.append("<button onclick=\"addUserConv(this)\">add</button>\n</span>\n</span>\n");
                }
                return sb.toString();
            }catch (SQLException e){
                e.printStackTrace();
                return "Problems with ResultSet";
            }
        }else{
            return "Problems with getResultSet";
        }
    }

    public String addRecordUsersConversation(int user, String invited, String convName){
        try {
            if (!findUserById(invited)){
                String sqlQuery = "SELECT ID FROM USERS WHERE USERNAME = '" + invited + "' LIMIT 1;";
                ResultSet rs = con.createStatement().executeQuery(sqlQuery);
                if (!rs.next()){
                    System.out.println(invited);
                    return "can not find invited";
                }else{
                    invited = String.valueOf(rs.getInt("ID"));
                }
            }
            String sqlQuery = "SELECT ID FROM CONVERSATIONS WHERE NAME = '" + convName + "' LIMIT 1;";
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            int idConv = -1;
            if (!rs.next()) {
                idConv = createConversation(convName);
                executeQuery("INSERT INTO USER_CONVERSATIONS VALUES(" +
                        user + ", " +
                        idConv + ");");
            }
            idConv = (idConv == -1)? rs.getInt("ID") : idConv;
            executeQuery("INSERT INTO USER_CONVERSATIONS VALUES(" +
                    invited + ", " +
                    idConv + ");");
            return "OK";
        }catch(SQLException e){
            e.printStackTrace();
            return "some problem";
        }
    }

    private int createConversation(String convName){
        int id = 0;
        try{
            id = getResultSet("SELECT MAX(ID) AS ID FROM CONVERSATIONS")
                    .getInt("ID") + 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        executeQuery("INSERT INTO CONVERSATIONS VALUES(" +
                id + ", " +
                "'" + convName + "');");
        return id;
    }

    private boolean findUserById(String id){
        try{
            String sqlQuery = "SELECT 1 FROM USERS WHERE ID = " + id + " LIMIT 1;";
            ResultSet rs = con.createStatement().executeQuery(sqlQuery);
            return rs.next();
        }catch(SQLException e){
            return false;
        }
    }

    public boolean findUser(String login){
        try{
            String sqlQuery = "SELECT 1 FROM USERS WHERE USERNAME = '" + login + "' LIMIT 1;";
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
                    "'" + newUser.getUsersStatus() + "', " +
                    "'" + newUser.getFirstname() + "', " +
                    "'" + newUser.getLastname() + "');");
        }
        return false;
    }

    public void saveMessage(String conversation, String sender, String text){
        int id = 0;
        try{
            id = getResultSet("SELECT MAX(ID) AS ID FROM MESSAGES")
                    .getInt("ID") + 1;
        }catch (SQLException e){
            e.printStackTrace();
        }

        executeQuery("INSERT INTO MESSAGES VALUES(" +
                id + ", " +
                conversation + ", " +
                sender + ", " +
                "'" + text + "');");
    }

    public List<Message> getMessagesFromId(int id){
        try{
            ResultSet rs = getResultSet("SELECT * FROM MESSAGES WHERE ID > " + id +
                    " ORDER BY ID;");
            if (rs == null) return null;
            List<Message> ans = new ArrayList<>();
            ans.add(new Message(
                    rs.getInt("ID"),
                    rs.getInt("CONVERSATION"),
                    rs.getInt("SENDER"),
                    rs.getString("TEXT")
            ));
            while (rs.next()){
                ans.add(new Message(
                        rs.getInt("ID"),
                        rs.getInt("CONVERSATION"),
                        rs.getInt("SENDER"),
                        rs.getString("TEXT")
                ));
            }
            return ans;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
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

    public Map<Integer, Integer> getPatterns(int id){
        String sqlQuery = "SELECT * FROM USERS_PATTERNS WHERE JUGGLER = " + id;
        ResultSet rs = getResultSet(sqlQuery);
        Map<Integer, Integer> ans = new HashMap<>();
        try {
            if (rs == null) return new HashMap<>();
            ans.put(rs.getInt("PATTERN"), rs.getInt("STATUS"));
            while (rs.next()) {
                ans.put(rs.getInt("PATTERN"), rs.getInt("STATUS"));
            }
            return ans;
        }catch (SQLException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void updatePatterns(int id, Map<Integer,Integer> newPatterns){
        Map<Integer, Integer> lastPatterns = getPatterns(id);
        for (int i : newPatterns.keySet()){
            if (lastPatterns.containsKey(i)){
                if ( !lastPatterns.get(i).equals(newPatterns.get(i)) ){
                    String sqlQuery = "UPDATE USERS_PATTERNS SET STATUS = " + newPatterns.get(i) +
                            " WHERE JUGGLER = " + id +
                            " AND PATTERN = " + i;
                    executeQuery(sqlQuery);
                    //изменить содержание
                }else if (newPatterns.get(i) == 0){
                    String sqlQuery = "DELETE FROM USERS_PATTERNS" +
                            " WHERE JUGGLER = " + id +
                            " AND PATTERN = " + i;
                    executeQuery(sqlQuery);
                    //удалить запись
                }
            }else{
                String sqlQuery = "INSERT INTO USERS_PATTERNS VALUES(" +
                        id + ", " +
                        i + ", " +
                        newPatterns.get(i) + ");";
                executeQuery(sqlQuery);
                //вставить
            }
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
//доделать editingPattern и сделать addPattern, deletePattern
    public void editingPattern(String id, String name, String siteswap, String description){
        //Integer.parseInt(id.replaceFirst("0", ""));
        StringBuilder sqlQuery = new StringBuilder("UPDATE PATTERNS SET ");
        boolean add = false;
        if (name != null)if ( !"".equals(name) ){
            add = true;
            sqlQuery.append("PATTERNSNAME = '"); sqlQuery.append(name); sqlQuery.append("' ");
        }
        if (siteswap != null)if ( !"".equals(siteswap) ){
            if (add) sqlQuery.append(", ");
            add = true;
            sqlQuery.append("SITESWAP = '"); sqlQuery.append(siteswap); sqlQuery.append("' ");
        }
        if (description != null)if ( !"".equals(description) ){
            if (add) sqlQuery.append(", ");
            sqlQuery.append("DESCRIPTION = '"); sqlQuery.append(description); sqlQuery.append("' ");
            add = true;
        }
        if (add){
            sqlQuery.append("WHERE ID = ");
            sqlQuery.append(id);
            executeQuery(sqlQuery.toString());
        }
    }

    public void addPattern(String name, String descendants, String ancestors){
        int id = -1;
        try{
            id = getResultSet("SELECT MAX(ID) AS ID FROM PATTERNS")
                    .getInt("ID") + 1;
        }catch (SQLException e){ e.printStackTrace(); }
        if (id != -1) {
            boolean ans = executeQuery("INSERT INTO PATTERNS VALUES(" +
                    id + ", " +
                    "'" + name + "', " +
                    "'', " +
                    "'');");
            if (ans) {
                if ( "".equals(ancestors) ) ancestors = "0";
                if ("".equals(descendants)) descendants = "";
                String[] ancestorsArr = ancestors.split(" ");
                String[] descendantsArr = descendants.split(" ");
                for (String i : ancestorsArr){
                    if (ans) ans = executeQuery("INSERT INTO PATTERNSTREE VALUES( " +
                            i + ", " +
                            id + ");");
                }
                for (String i : descendantsArr){
                    if (ans) ans = executeQuery("INSERT INTO PATTERNSTREE VALUES( " +
                            id + ", " +
                            i + ");");
                }
            }
        }
    }

    public void deletePattern(int id) {


        String sqlQuery = "SELECT 1 FROM PATTERNSTREE WHERE PATTERN = " + id + " LIMIT 1;";

        boolean ans;
        ResultSet rs;
        try{
            rs = con.createStatement().executeQuery(sqlQuery);
            ans = !rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            ans = false;
        }
        if (ans){
            sqlQuery = "DELETE FROM PATTERNSTREE WHERE DESCENDANTS = " + id;
            ans = executeQuery(sqlQuery);
        }
        if (ans){
            sqlQuery = "DELETE FROM PATTERNS WHERE ID = " + id;
            executeQuery(sqlQuery);
        }
    }

    private ResultSet getResultSet(String sqlQuery) {
        try {
            ResultSet rs = con
                    .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_READ_ONLY)
                    .executeQuery(sqlQuery);
            rs.next();
            return rs;
        }catch (SQLException e){
            e.printStackTrace();
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
