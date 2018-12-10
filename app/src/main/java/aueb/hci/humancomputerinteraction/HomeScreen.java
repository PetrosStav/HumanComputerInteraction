package aueb.hci.humancomputerinteraction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

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

//        select_program.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeScreen.this,select_program.class);
//                startActivityForResult(intent,RESULT_CANCELED);
//            }
//        });

        advanced_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this,AdvancedProgram.class);
                startActivityForResult(intent,RESULT_CANCELED);
            }
        });

    }


}
