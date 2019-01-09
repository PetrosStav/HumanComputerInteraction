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
        GridView grid_images_select = findViewById(R.id.grid_images_select);

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

                InfoScreen.textId = 0;
                Intent intent = new Intent(SelectProgram.this, InfoScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED,intent);
        finish();
    }

}
