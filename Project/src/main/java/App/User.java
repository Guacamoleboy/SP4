package App;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String status;
    private String banned;
    private String firstName;
    private String lastName;

    // Tom constructor
    public User() {
    }

    // constructor til database (uden first/last name)
    public User(String username, String password, String email, String status, String banned) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.banned = banned;
    }

    // constructor med first/last name
    public User(String username, String password, String firstName, String lastName, String email, String status, String banned) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.banned = banned;
    }

    // constructor med id
    public User(int id, String username, String password, String firstName, String lastName, String email, String status, String banned) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.banned = banned;
    }

    // whole lotta getter and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    // få fuldt navn
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return username;
        }
    }


    public String toCSV() {
        return username + ", " + password + ", " + email + ", " + status + ", " + banned;
    }

    @Override
    public String toString() {
        return "User [id=" + id +
                ", username=" + username +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", status=" + status +
                ", banned=" + banned + "]";
    }
}
