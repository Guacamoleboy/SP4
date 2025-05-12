package App;

import util.TextUI;

public class User {

    // Attributes

    // DATATYPE //
    private String username;
    private String password;
    private String email;
    private String status;
    private String banned;
    

    // ____________________________________________________

    public User(String username, String password, String email, String status, String banned){

        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.banned = banned;

    }

    // ____________________________________________________

    public String getEmail(){
        return this.email;

    }

    // ____________________________________________________

    public String getPassword(){
        return this.password;
    }

    // ____________________________________________________

    public String getUsername(){
        return this.username;
    }

    // ____________________________________________________

    public String getBanned(){
        return this.banned;
    }

    // ____________________________________________________

    public String getStatus(){
        return this.status;
    }

    // ____________________________________________________

    public void setStatus(String status){
        this.status = status;
    }

    // ____________________________________________________

    public String toCSV(){

        return this.username + ", " + this.password + ", " + this.email + ", " + this.status + ", " + this.banned;

    }

}
