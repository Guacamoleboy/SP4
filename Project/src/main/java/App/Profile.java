// Packages
package App;

// Import
import javafx.scene.layout.Pane;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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
        rolehex 9
        hair_type_id 10
        school_id 11
        student_year 12
        profile_picture 13
        language 14
        accepted 15
        darkmode 16
        lastonline 17
        city 18
        abtmeheader 19
        abtmedesc 20
        abtmefunfacts 21
        phone 22
        rating 23
        social 24
        contactheader 25
        contactdesc 26

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
        return Double.parseDouble(userData.get(23));
    }

    // ______________________________________

    public String getProfileAboutmeHeader() {
        return userData.get(19);
    }

    // ______________________________________

    public String getProfileAboutDescription() {
        return userData.get(20);
    }

    // ______________________________________

    public String[] getInterests() {
        System.out.println("INTERESTS SKAL SÆTTES IND I DATABASE!!!");
        return new String[]{"Gym", "Creativity", "Gaming"};
    }

    // ______________________________________

    public String getFunFacts() {
        return userData.get(21);
    }

    // ______________________________________

    public String getLastOnline() {
        if (getStatus().equalsIgnoreCase("online")) {
            return "ONLINE NOW";
        }

        String lastOnlineStr = userData.get(17);
        LocalDateTime lastOnlineTime = LocalDateTime.parse(lastOnlineStr);
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(lastOnlineTime, now);
        Period period = Period.between(lastOnlineTime.toLocalDate(), now.toLocalDate());

        long years = period.getYears();
        long months = period.getMonths();
        long days = period.getDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds();

        if (years >= 1) {
            return "Last online: " + years + (years == 1 ? " year" : " years") + " ago";
        } else if (months >= 1) {
            return "Last online: " + months + (months == 1 ? " month" : " months") + " ago";
        } else if (days >= 1) {
            return "Last online: " + days + (days == 1 ? " day" : " days") + " ago";
        } else if (hours >= 1) {
            return "Last online: " + hours + (hours == 1 ? " hour" : " hours") + " ago";
        } else if (minutes >= 1) {
            return "Last online: " + minutes + (minutes == 1 ? " minute" : " minutes") + " ago";
        } else {
            return "Last online: " + seconds + (seconds == 1 ? " second" : " seconds") + " ago";
        }
    }

    // ______________________________________

    public String getCity() {
        return userData.get(18);
    }

    // ______________________________________

    public String getSocial(String social) {
        String link = "";
        switch (social.toLowerCase()) {
            case "instagram":
                link = "https://www.instagram.com/"+userData.get(24);
                break;
            case "facebook":
                link = "https://www.facebook.com/"+userData.get(24);
                break;
            case "github":
                link = "https://github.com/"+userData.get(24);
                break;
        }

        return link;
    }

    // ______________________________________

    public String getContactHeader() {
        return userData.get(25);
    }

    // ______________________________________

    public String getContactDescription() {
        return userData.get(26);
    }

    // ______________________________________

    public String getPhoneNumber() {
        return userData.get(22);
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
