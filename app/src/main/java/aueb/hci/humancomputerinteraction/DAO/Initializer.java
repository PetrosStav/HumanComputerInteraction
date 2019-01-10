package aueb.hci.humancomputerinteraction.DAO;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import aueb.hci.humancomputerinteraction.HomeScreen;
import aueb.hci.humancomputerinteraction.Program;
import aueb.hci.humancomputerinteraction.R;

public abstract class Initializer {
    protected abstract void eraseData();

    /**
     * This method inserts the training data.
     */
    public void prepareData() {

        //erase the data first
        eraseData();

        ProgramDAO programDAO = getProgramData();

        Program p1 = new Program();
        p1.setName("White Clothes");
        p1.setDescription("Program only for white clothes.");
        p1.setFavorited(true);
        p1.setDryer(true);
        p1.setTIME(60);
        p1.setRPM(400);
        p1.setTEMP(45);
        p1.setImage(BitmapFactory.decodeResource(HomeScreen.res, R.drawable.white_clothes));

        Program p2 = new Program();
        p2.setName("Color Clothes");
        p2.setDescription("Program for all color clothes.");
        p2.setFavorited(false);
        p2.setTIME(20);
        p2.setRPM(600);
        p2.setTEMP(45);
        p2.setImage(BitmapFactory.decodeResource(HomeScreen.res, R.drawable.color_clothes));

        Program p3 = new Program();
        p3.setName("Cotton");
        p3.setDescription("Program for cotton clothes.");
        p3.setFavorited(true);
        p3.setDryer(true);
        p3.setTIME(20);
        p3.setRPM(400);
        p3.setTEMP(75);
        p3.setImage(BitmapFactory.decodeResource(HomeScreen.res, R.drawable.cotton_clothes));

        Program p4 = new Program();
        p4.setName("Wool");
        p4.setDescription("Program for woolen clothes.");
        p4.setFavorited(false);
        p4.setTIME(80);
        p4.setRPM(1000);
        p4.setTEMP(90);
        p4.setImage(BitmapFactory.decodeResource(HomeScreen.res, R.drawable.woolen_clothes));

        Program p5 = new Program();
        p5.setName("Synthetic");
        p5.setDescription("Program for synthetic clothes.");
        p5.setFavorited(true);
        p5.setDryer(true);
        p5.setTIME(40);
        p5.setRPM(400);
        p5.setTEMP(60);
        p5.setImage(BitmapFactory.decodeResource(HomeScreen.res, R.drawable.synthetic_clothes));

        Program p6 = new Program();
        p6.setName("Default Program");
        p6.setDescription("Default Program Description");
        p6.setFavorited(true);
        p6.setDryer(true);
        p6.setTIME(60);
        p6.setRPM(1200);
        p6.setTEMP(30);

        programDAO.save(p1);
        programDAO.save(p2);
        programDAO.save(p3);
        programDAO.save(p4);
        programDAO.save(p5);
        programDAO.save(p6);
    }

    public abstract ProgramDAO getProgramData();
}
