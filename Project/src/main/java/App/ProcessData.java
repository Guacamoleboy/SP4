package App;

import util.*;
import java.util.ArrayList;

public class ProcessData {

    // Attributes
    private Login login = new Login(900, 600);
    private  FileIO io = new FileIO();
    private ArrayList <User> user;

    // ____________________________________________________

    public ProcessData(){
        this.user = new ArrayList<>();
        loadUserData();
    }

    // ____________________________________________________

    public void saveData(){

        ArrayList<String> data = new ArrayList<>();

        for (User u : user){

                String s = u.toCSV();
                data.add(s);

        }

        io.saveData(data, "src/main/java/data/userData.csv", "Username, Password, Email, Status, Banned");

    }

    // ____________________________________________________

    public void setStatus(String username, String status){

        for (User u : user){
            if(u.getUsername().equalsIgnoreCase(username)){
                u.setStatus(status);
                break;
            }

        }

        saveData();

    }

    // ____________________________________________________

    public void loadUserData(){

        ArrayList <String> userdata = io.readData("src/main/java/data/userData.csv");

        if(!userdata.isEmpty()) {

            for (String s : userdata) {

                String[] values = s.split(", ");
                String valuesUsername = values[0].trim();
                String valuesPassword = values[1].trim();
                String valuesEmail = values[2].trim();
                String valuesStatus = values[3].trim();
                String valuesBanned = values[4].trim();

                createUser(valuesUsername, valuesPassword, valuesEmail, valuesStatus, valuesBanned);

            }

        } // If (1)

    }

    // ____________________________________________________

    public void createUser(String username, String password, String email, String status, String banned){

        User u = new User(username, password, email, status, banned);
        user.add(u);

    }

    // ____________________________________________________

    public ArrayList <User> getUsers(){
        return user;
    }

}
