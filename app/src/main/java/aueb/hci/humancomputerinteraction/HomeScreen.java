package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    ArrayList<Program> programData = null;
    ProgramListAdapter adapter = null;

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

        programData = new ArrayList<>();

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

        programData.add(p1);
        programData.add(p2);
        programData.add(p3);
        programData.add(p4);
        programData.add(p5);
        programData.add(p6);

        adapter = new ProgramListAdapter(this);
        adapter.loadData(programData);

        lvFavorites.setAdapter(adapter);

        select_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this,SelectProgram.class);
                intent.putExtra("PROGRAMS",programData);
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
