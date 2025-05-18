// Packages
package App;

// Import
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Random;

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
        return userData.get(7);
    }

    // ______________________________________

    public String getProfileBannerHex(){
        return userData.get(8);
    }

    // ______________________________________

    public String getProfileRoleHex(){
        return userData.get(9);
    }

    // ______________________________________

    public double getRating() {
        System.out.println("RATING SKAL TILFØJES TIL DATABASEN!!!!!");
        return 5.5;//Double.parseDouble(userData.get(11));
    }

    // ______________________________________

    public String getProfileAboutmeHeader() {
        System.out.println("About me header Skal sættes ind i database!!!!");
        return "Welcome to my page!";
    }

    // ______________________________________

    public String getProfileAboutDescription() {
        System.out.println("About me description Skal sættes ind i database!!!!");
        String message = "Hi, I'm " + getUsername() + ". I love cutting hair and making people happy. " +
                "Would you like to get farted on? I bet you do you sicko. Book me today or regret it forever!!";
        return message;
    }

    // ______________________________________

    public String[] getInterests() {
        System.out.println("INTERESTS SKAL SÆTTES IND I DATABASE!!!");
        return new String[]{"Gym", "Creativity", "Gaming"};
    }

    // ______________________________________

    public String getFunFacts() {
        System.out.println("About me fun facts Skal sættes ind i database!!!!");
        String message = "A random fun fact that isnt fun at all...";
        return message;
    }

    // ______________________________________

    public String getLastOnline() {
        System.out.println("Last online skal sættes ind i database!!!!");
        String lastOnline = "";
        if (getStatus().equals("online")) {
            return "ONLINE";
        }
        Random random = new Random();
        int randomInt = random.nextInt(24)+1;
        lastOnline = randomInt + " hours ago";
        return lastOnline;
    }

    // ______________________________________

    public String getCity() {
        System.out.println("Get city skal sættes ind i database!!!!!");
        return "Hillerød";
    }

    // ______________________________________

    public String getSocial(String social) {
        String link = "";
        switch (social.toLowerCase()) {
            case "instagram":
                link = "https://www.instagram.com/";
                break;
            case "facebook":
                link = "https://www.facebook.com/";
                break;
            case "github":
                link = "https://github.com/";
                break;
        }

        return link;
    }

    // ______________________________________

    public String getContactHeader() {
        return "I'm glad you're here.";
    }

    // ______________________________________

    public String getContactDescription() {
        return "Hi, I'm " + getUsername() + ". I love cutting hair and making people happy.\nNeed a fresh cut or just wanna talk? Reach out!";
    }

    // ______________________________________

    public String getPhoneNumber() {
        return "12345678";
    }

    // ______________________________________

    public ArrayList<ArrayList<String>> getReviews() {
        ArrayList<ArrayList<String>> reviews = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ArrayList<String> review = new ArrayList<String>();
            Random random = new Random();
            double randomInt = random.nextDouble(5);
            String header = "Rigtig god klip" + i;
            String description = "Dette er beskrivelsen" + i;
            String rating = ""+randomInt;
            review.add(header);
            review.add(description);
            review.add(rating);
            reviews.add(review);
        }
        return reviews;
    }

    // ______________________________________

    public ArrayList<ArrayList<String>> getCuts() {
        ArrayList<ArrayList<String>> cuts = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ArrayList<String> cut = new ArrayList<>();
            String header = "Fresh fade " + i;
            String pngPath = "/assets/slideshow/" + i + ".png";
            cut.add(header);
            cut.add(pngPath);
            cuts.add(cut);
        }
        return cuts;
    }


}
