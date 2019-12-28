package ru.gena.itmo.SocialNetwork.SocialNetwork.content;

public class User {
    private Integer id;
    private String login;
    private String password;
    private String usersStatus;
    private String firstname;
    private String lastname;

    public User(){
        id = null;
        login = null;
        password = null;
        usersStatus = null;
        firstname = null;
        lastname = null;
    }
    public User(Integer newId,
                String newLogin,
                String newPassword,
                String newStatus,
                String newFirstname,
                String newLastname){
        id = newId;
        login = newLogin;
        password = newPassword;
        usersStatus = newStatus;
        firstname = newFirstname;
        lastname = newLastname;
    }
    public Integer getId(){
        return id;
    }
    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }
    public String getUsersStatus() { return usersStatus; }
    public String getFirstname(){
        return firstname;
    }
    public String getLastname(){
        return lastname;
    }
}
