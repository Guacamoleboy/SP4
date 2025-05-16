// Packages
package App;

// Import
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Profile extends Pane {

    // Attributes

    // OBJECT //
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
        return -1;
    }

    // ______________________________________

    public String getEmail(){
        return userData.get(3);
    }

    // ______________________________________

    public String getUsername(){
        return userData.get(1);
    }

    // ______________________________________

    public String getProfilePictureHex(){
        if (userData.size() > 7) {
            return userData.get(7);
        }
        // default
        return "#ADD8E6FF";
    }

    // ______________________________________

    public String getProfileBannerHex(){
        if (userData.size() > 8) {
            return userData.get(8);
        }
        // default
        return "#D3D3D3FF";
    }

    // ______________________________________

    public String getProfileRoleHex(){
        // check profile color
        if (userData.size() > 9) {
            return userData.get(9);
        }
        // default
        return "#d0e6f7";
    }

    // ______________________________________

    public String getBannerUrl(){
        // check for url in usedrdata
        if (userData.size() > 11) {
            return userData.get(11);
        }
        return null;
    }
}
