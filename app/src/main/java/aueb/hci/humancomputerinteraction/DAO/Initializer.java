package aueb.hci.humancomputerinteraction.DAO;

import aueb.hci.humancomputerinteraction.Program;

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
        p1.setName("P1");
        p1.setDescription("Program 1 Description");
        p1.setFavorited(true);
        p1.setDryer(true);
        p1.setTIME(60);
        p1.setRPM(400);
        p1.setTEMP(45);

        Program p2 = new Program();
        p2.setName("P2");
        p2.setDescription("Program 2 Description");
        p2.setFavorited(false);
        p2.setTIME(20);
        p2.setRPM(600);
        p2.setTEMP(45);

        Program p3 = new Program();
        p3.setName("Program 3");
        p3.setDescription("Program 3 Description");
        p3.setFavorited(true);
        p3.setDryer(true);
        p3.setTIME(20);
        p3.setRPM(400);
        p3.setTEMP(75);

        Program p4 = new Program();
        p4.setName("Program 4");
        p4.setDescription("Program 4 Description");
        p4.setFavorited(false);
        p4.setTIME(80);
        p4.setRPM(1000);
        p4.setTEMP(90);

        Program p5 = new Program();
        p5.setName("Program 5");
        p5.setDescription("Program 5 Description");
        p5.setFavorited(true);
        p5.setDryer(true);
        p5.setTIME(40);
        p5.setRPM(400);
        p5.setTEMP(60);

        Program p6 = new Program();
        p6.setName("Program for white clothes and easy stains");
        p6.setDescription("Program 6 Description");
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
