package aueb.hci.humancomputerinteraction;

import java.io.Serializable;

public class Program implements Serializable{

    private String name;
    private boolean favorited;
    private String imagePath;
    private String description;

    private boolean eco;
    private boolean prewash;
    private boolean dryer;

    private int RPM;
    private int TIME;
    private int TEMP;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEco() {
        return eco;
    }

    public void setEco(boolean eco) {
        this.eco = eco;
    }

    public boolean isPrewash() {
        return prewash;
    }

    public void setPrewash(boolean prewash) {
        this.prewash = prewash;
    }

    public boolean isDryer() {
        return dryer;
    }

    public void setDryer(boolean dryer) {
        this.dryer = dryer;
    }

    public int getRPM() {
        return RPM;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }

    public int getTIME() {
        return TIME;
    }

    public void setTIME(int TIME) {
        this.TIME = TIME;
    }

    public int getTEMP() {
        return TEMP;
    }

    public void setTEMP(int TEMP) {
        this.TEMP = TEMP;
    }
}
