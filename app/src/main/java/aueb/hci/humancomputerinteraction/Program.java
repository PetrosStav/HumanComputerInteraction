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

    public Program() {
        this.name = "DEFAULT";
        this.favorited = false;
        this.imagePath = "NA";
        this.description = "NA";
        this.eco = false;
        this.prewash = false;
        this.dryer = false;
        this.RPM = 200;
        this.TIME = 10;
        this.TEMP = 15;
    }

    public Program(Program p){
        this.name = p.name;
        this.favorited = p.favorited;
        this.imagePath = p.imagePath;
        this.description = p.description;
        this.eco = p.eco;
        this.prewash = p.prewash;
        this.dryer = p.dryer;
        this.RPM = p.RPM;
        this.TIME = p.TIME;
        this.TEMP = p.TEMP;
    }

    public Program(String name, boolean favorited, String imagePath, String description, boolean eco, boolean prewash, boolean dryer, int RPM, int TIME, int TEMP) {
        this.name = name;
        this.favorited = favorited;
        this.imagePath = imagePath;
        this.description = description;
        this.eco = eco;
        this.prewash = prewash;
        this.dryer = dryer;
        this.RPM = RPM;
        this.TIME = TIME;
        this.TEMP = TEMP;
    }

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
