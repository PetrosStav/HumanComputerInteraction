package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

public class SelectProgram extends AppCompatActivity {

    List<Program> programData = null;
    ProgramDAO programdao = new ProgramDAOmemory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_program);
        ToggleButton tbSelectPrewash = findViewById(R.id.tbSelectPrewash);
        ToggleButton tbSelectEco = findViewById(R.id.tbSelectEco);
        ToggleButton tbSelectDryer = findViewById(R.id.tbSelectDryer);
        Button btnSelectCancel = findViewById(R.id.btnSelectCancel);
        Button btnSelectStart = findViewById(R.id.btnSelectStart);
        GridView grid_images_select = (GridView) findViewById(R.id.grid_images_select);

        ImageView info = findViewById(R.id.ivSelectInfo);

        programData = programdao.findAll();

        ProgramAdapter adapter = new ProgramAdapter(this);
        adapter.loadData(programData, false);

        grid_images_select.setAdapter(adapter);

        btnSelectCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });

        btnSelectStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeScreen.runningProgram!=null){
                    Toast.makeText(SelectProgram.this, HomeScreen.runningProgram.getName() + " is running. Please wait for it to finish, or stop it first!", Toast.LENGTH_LONG).show();
                }else if(adapter.selectedProgram!=null){
                    Intent intent = new Intent();
                    //intent.putExtra("PROGRAM_NAME",adapter.selectedProgram.getName()); // TODO esbhsa tote kai egw apo to select program to intent
                    Program overProg = new Program(adapter.selectedProgram);
                    // Override only if checked
                    if(tbSelectEco.isChecked()){
                        overProg.setEco(true);
                    }
                    if(tbSelectPrewash.isChecked()) {
                        overProg.setPrewash(true);
                    }
                    if(tbSelectDryer.isChecked()) {
                        overProg.setDryer(true);
                    }

                    HomeScreen.selectedProgram = overProg;
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(SelectProgram.this, "Choose a program to start!", Toast.LENGTH_LONG).show();
                    // Play warning sound
                    MediaPlayer media = MediaPlayer.create(getApplicationContext(), R.raw.sound_effect);
                    media.start();
                }

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: REMOVE
//                Toast.makeText(SelectProgram.this, "In this screen you can select a Program from the saved programs by clicking on it and pressing 'Start' to start a wash.\n" +
//                        "By pressing in one of the above buttons you can sort the programs based on their type.\n" +
//                        "By pressing on the shirt's info icon, you get info about that program.\n" +
//                        "By pressing on the shirt's heart icon, you can favorite and unfavorite the program.\n" +
//                        "By pressing on the shirt's gear icon, you can do configurations for the program.\n" +
//                        "By enabling one of the override toggle-buttons, you override that mode for any chosen program.", Toast.LENGTH_LONG).show();

                InfoScreen.textId = 0;
                Intent intent = new Intent(SelectProgram.this, InfoScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        //intent.putExtra("PROGRAMS",programData);
        setResult(Activity.RESULT_CANCELED,intent);
        finish();
        //super.onBackPressed();
    }

}
