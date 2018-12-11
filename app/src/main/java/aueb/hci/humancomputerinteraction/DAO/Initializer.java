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
        p1.setDescription("Program 1 bitch");
        p1.setFavorited(true);
        p1.setDryer(true);

        Program p2 = new Program();
        p2.setName("P2");
        p2.setDescription("Program 2 bitch");
        p2.setFavorited(false);

        Program p3 = new Program();
        p3.setName("Program 3");
        p3.setDescription("Program 3 bitch");
        p3.setFavorited(true);
        p3.setDryer(true);

        Program p4 = new Program();
//        p4.setName("Program h istoria ths zohs mou noumero 4 : THE ULTIMATE SEQUEL");
        p4.setName("Program 4");
        p4.setDescription("Program 4 bitch");
        p4.setFavorited(false);

        Program p5 = new Program();
        p5.setName("Program 5");
        p5.setDescription("Program 5 bitch");
        p5.setFavorited(true);
        p5.setDryer(true);

        Program p6 = new Program();
        p6.setName("Program for white clothes and easy stains");
        p6.setDescription("Program 6 bitch");
        p6.setFavorited(true);
        p6.setDryer(true);

        programDAO.save(p1);
        programDAO.save(p2);
        programDAO.save(p3);
        programDAO.save(p4);
        programDAO.save(p5);
        programDAO.save(p6);
    }

    public abstract ProgramDAO getProgramData();
}
