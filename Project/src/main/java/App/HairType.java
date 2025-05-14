package App;

import App.Main.*;

public class HairType {
    public int id;
    public String texture;
    public String color;
    public String length;
    public String gender;

    public HairType(int id, String texture, String color, String length, String gender) {
        this.id = id;
        this.texture = texture;
        this.color = color;
        this.length = length;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "HairType{id=" + id + ", texture='" + texture + "', color='" + color +
                "', length='" + length + "', gender='" + gender + "'}";
    }


}
