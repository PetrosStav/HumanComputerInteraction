package aueb.hci.humancomputerinteraction.DAO;
import java.util.List;
import aueb.hci.humancomputerinteraction.Program;

public interface ProgramDAO {

    Program find(String ProgramID);

    void save(Program program);

    void delete(Program program);

    List<Program> findAll();
}
