package aueb.hci.humancomputerinteraction;
import java.util.ArrayList;
import java.util.List;
import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

public class ProgramDAOmemory implements ProgramDAO {

    protected static List<Program> entities = new ArrayList<Program>();

    public void delete(Program Program){
        entities.remove(Program);
    }

    public void save(Program Program){
        if(!entities.contains(Program)){
            entities.add(Program);
        }
    }

    public Program find(String programID){
        for(Program program: entities){
            if(program.getName().equals(programID)){
                return program;
            }
        }
        return null;
    }

    public List<Program> findAll(){
        return new ArrayList<>(entities);
    }
}
