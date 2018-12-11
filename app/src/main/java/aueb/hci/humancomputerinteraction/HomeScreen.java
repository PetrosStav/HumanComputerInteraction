package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import aueb.hci.humancomputerinteraction.ProgramDAOmemory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    List<Program> programData = null;
    ProgramListAdapter adapter = null;
    private static boolean initialized = false;
    ProgramDAOmemory programdao = new ProgramDAOmemory();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Bundle dataB = data.getExtras();
            programData = (ArrayList<Program>) dataB.get("PROGRAMS");
            adapter.loadData(programData);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button select_program = findViewById(R.id.btSelectProgram);
        Button advanced_program = findViewById(R.id.btAdvancedProgram);
        Button manage_programs = findViewById(R.id.btManagePrograms);
        Button help = findViewById(R.id.btHelp);
        Button start = findViewById(R.id.btStart);
        Button start_dryer = findViewById(R.id.btStartDryer);
        ListView lvFavorites = findViewById(R.id.lvFavorites);

        if(!initialized){
            new DataInitializer().prepareData();
            initialized = true;
            Toast.makeText(this,"Loading Data!!",Toast.LENGTH_LONG).show();
        }

        programData = programdao.findAll();
        adapter = new ProgramListAdapter(this);
        adapter.loadData(programData);

        lvFavorites.setAdapter(adapter);

        select_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this,SelectProgram.class);
                intent.putExtra("PROGRAMS", (Serializable) programData);
                startActivityForResult(intent,1);
            }
        });

        advanced_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this,AdvancedProgram.class);
                startActivityForResult(intent,RESULT_OK);
            }
        });

    }


}
