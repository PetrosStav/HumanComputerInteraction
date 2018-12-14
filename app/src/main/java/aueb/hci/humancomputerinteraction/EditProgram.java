package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;
import it.beppi.knoblibrary.Knob;

public class EditProgram extends AppCompatActivity {

    Button btnEditCancel = null;
    Button btnEditSave = null;
    ToggleButton tbEco = null;
    ToggleButton tbDryer = null;
    ToggleButton tbPrewash = null;
    ProgramDAO programdao = new ProgramDAOmemory();

    Program selectedProgram = null;
    Program newProgram = null;

    Integer[] rpms = {200,400,600,800,1000,1200};
    List<Integer> Rpms = Arrays.asList(rpms);
    Integer[] temps = {15,30,45,60,75,90};
    List<Integer> Temps = Arrays.asList(temps);
    Integer[] times = {10,20,30,40,50,60};
    List<Integer> Times = Arrays.asList(times);

    EditText etName = null;
    EditText etEditDescription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_program);

        Knob knob1 = (Knob) findViewById(R.id.knob1Edit);
        Knob knob2 = (Knob) findViewById(R.id.knob2Edit);
        Knob knob3 = (Knob) findViewById(R.id.knob3Edit);

        etName = findViewById(R.id.etName);
        etEditDescription = findViewById(R.id.etEditDescription);

        btnEditCancel = findViewById(R.id.btnEditCancel);
        btnEditSave = findViewById(R.id.btnEditSave);

        tbEco = findViewById(R.id.tbEditEco);
        tbDryer = findViewById(R.id.tbEditDryer);
        tbPrewash = findViewById(R.id.tbEditPrewash);

        Bundle data = getIntent().getExtras();
        String pname = (String)data.get("PROGRAM");

        selectedProgram = programdao.find(pname);

        etName.setText(selectedProgram.getName());
        etEditDescription.setText(selectedProgram.getDescription());

        tbEco.setChecked(selectedProgram.isEco());
        tbDryer.setChecked(selectedProgram.isDryer());
        tbPrewash.setChecked(selectedProgram.isPrewash());

        knob1.setState((Rpms.indexOf(selectedProgram.getRPM())-2) % 6);
        knob2.setState((Temps.indexOf(selectedProgram.getTEMP())-2) % 6);
        knob3.setState((Times.indexOf(selectedProgram.getTIME())-2) % 6);

        tbEco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProgram.setEco(tbEco.isChecked());
            }
        });

        tbDryer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProgram.setDryer(tbDryer.isChecked());
            }
        });

        tbPrewash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProgram.setPrewash(tbPrewash.isChecked());
            }
        });

        ((ImageView)findViewById(R.id.ivEditHeart)).setImageResource(selectedProgram.isFavorited()?R.drawable.filled_heart:R.drawable.heartico);

        ((ImageView)findViewById(R.id.ivEditHeart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProgram.setFavorited(!newProgram.isFavorited());
                ((ImageView)findViewById(R.id.ivEditHeart)).setImageResource(newProgram.isFavorited()?R.drawable.filled_heart:R.drawable.heartico);
            }
        });

        newProgram = new Program(selectedProgram);

        knob1.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                newProgram.setRPM(rpms[(state+2)%6]);
            }
        });

        knob2.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                newProgram.setTEMP(temps[(state+2)%6]);
            }
        });

        knob3.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                newProgram.setTIME(times[(state+2)%6]);
            }
        });

        btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedProgram.setRPM(newProgram.getRPM());
                selectedProgram.setTEMP(newProgram.getTEMP());
                selectedProgram.setTIME(newProgram.getTIME());

                selectedProgram.setFavorited(newProgram.isFavorited());

                selectedProgram.setName(etName.getText().toString());
                selectedProgram.setDescription(etEditDescription.getText().toString());
                selectedProgram.setImagePath(newProgram.getImagePath());

                selectedProgram.setEco(newProgram.isEco());
                selectedProgram.setDryer(newProgram.isDryer());
                selectedProgram.setPrewash(newProgram.isPrewash());

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });


    }
}
