package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.Arrays;
import java.util.List;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;
import it.beppi.knoblibrary.Knob;

public class EditProgram extends AppCompatActivity {

    Button btnEditCancel = null;
    Button btnEditSave = null;
    Button btnEditSelectIco = null;
    ToggleButton tbEco = null;
    ToggleButton tbDryer = null;
    ToggleButton tbPrewash = null;
    ProgramDAO programdao = new ProgramDAOmemory();

    Program selectedProgram = null;
    Program newProgram = null;

    Integer[] rpms = {400,600,800,1000,1200,1400};
    List<Integer> Rpms = Arrays.asList(rpms);
    Integer[] temps = {20,30,45,60,75,95};
    List<Integer> Temps = Arrays.asList(temps);
    Integer[] times = {20,40,60,80,100,120,140,160,180,200,210};
    List<Integer> Times = Arrays.asList(times);

    EditText etName = null;
    EditText etRpm = null;
    EditText etTemp = null;
    EditText etTime = null;
    EditText etEditDescription = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==5){
            if(data!=null) {
                final Bundle extras = data.getExtras();
                if(extras!=null){
                    Bitmap btm = extras.getParcelable("data");
                    newProgram.setImage(btm);
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_program);

        Knob knob1 = (Knob) findViewById(R.id.knob1Edit);
        Knob knob2 = (Knob) findViewById(R.id.knob2Edit);
        Knob knob3 = (Knob) findViewById(R.id.knob3Edit);

        ImageView info = findViewById(R.id.ivEditInfo);

        etName = findViewById(R.id.etName);
        etEditDescription = findViewById(R.id.etEditDescription);
        etRpm = findViewById(R.id.etRpm);
        etTime = findViewById(R.id.etTime);
        etTemp = findViewById(R.id.etTemp);
        btnEditCancel = findViewById(R.id.btnEditCancel);
        btnEditSave = findViewById(R.id.btnEditSave);
        btnEditSelectIco = findViewById(R.id.btnEditSelectIco);

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

        int rpm_index = Rpms.indexOf(selectedProgram.getRPM());
        int temp_index = Temps.indexOf(selectedProgram.getTEMP());
        int time_index = Times.indexOf(selectedProgram.getTIME());

        knob1.setState(rpm_index);
        etRpm.setText(Integer.toString(Rpms.get(rpm_index)));

        knob2.setState(temp_index);
        etTemp.setText(Integer.toString(Temps.get(temp_index)));

        knob3.setState(time_index);
        etTime.setText(Integer.toString(Times.get(time_index)));

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
                newProgram.setRPM(rpms[(state)]);
                etRpm.setText(Integer.toString(rpms[(state)]));
            }
        });

        knob2.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                newProgram.setTEMP(temps[(state)]);
                etTemp.setText(Integer.toString(temps[(state)]));
            }
        });

        knob3.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                newProgram.setTIME(times[(state)]);
                etTime.setText(Integer.toString(times[(state)]));
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
                selectedProgram.setImage(newProgram.getImage());

                selectedProgram.setEco(newProgram.isEco());
                selectedProgram.setDryer(newProgram.isDryer());
                selectedProgram.setPrewash(newProgram.isPrewash());

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        btnEditSelectIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 136);
                intent.putExtra("outputY", 119);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(Intent.createChooser(intent,"Select Icon"),5);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoScreen.textId = 3;
                Intent intent = new Intent(EditProgram.this, InfoScreen.class);
                startActivity(intent);
            }
        });

    }
}
