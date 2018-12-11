package aueb.hci.humancomputerinteraction;

import aueb.hci.humancomputerinteraction.DAO.Initializer;
import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

public class DataInitializer extends Initializer {
    /**
     * This method deletes all the saved data.
     */
    protected void eraseData(){

        for(Program program : getProgramData().findAll()){
            getProgramData().delete(program);
        }
    }

    @Override
    public ProgramDAO getProgramData() {
        return new ProgramDAOmemory();
    }




}
