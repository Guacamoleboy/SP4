// Packages
package App;

// Import
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Profile extends Pane {

    // Attributes
    private ArrayList<String> userData;

    /*

        id 0
        username 1
        password 2
        email 3
        status 4
        role 5
        banned 6
        profile picture 7
        profileBanner 8

     */

    // ______________________________________

    public Profile(String username){
        this.userData = Main.db.getUserData(username);

    }

    // ______________________________________

    public String getBanned(){
        return userData.get(6);
    }

    // ______________________________________

    public String getRole(){
        return userData.get(5);
    }

    // ______________________________________

    public String getStatus(){
        return userData.get(4);
    }

    // ______________________________________

    public String getPassword(){
        return userData.get(2);
    }

    // ______________________________________

    public int getID(){
        try {
            return Integer.parseInt(userData.get(0));
        } catch (NumberFormatException e) {
            System.out.println("ID MUST BE A NUMBER!!!");
        }
        return -1; //nigga
    }

    // ______________________________________

    public String getEmail(){
        return userData.get(3);
    }

    // ______________________________________

    public String getProfilePicture(){
        return userData.get(8);
    }

    // ______________________________________

    public String getProfileBanner(){
        return userData.get(9);
    }

}
