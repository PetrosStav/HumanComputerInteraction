package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;
import java.util.List;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;
import it.beppi.knoblibrary.Knob;

public class AdvancedProgram extends AppCompatActivity {

    Button btnCancel = null;
    Button btnSelectIco = null;
    Button btnStart = null;
    ToggleButton tbAdvancedEco = null;
    ToggleButton tbAdvancedPrewash = null;
    ToggleButton tbAdvancedDryer = null;
    ImageView ivHeart = null;

    Program advProg = null;
    ProgramDAO programDAO = new ProgramDAOmemory();

    Integer[] rpms = {400,600,800,1000,1200,1400};
    List<Integer> Rpms = Arrays.asList(rpms);
    Integer[] temps = {20,30,45,60,75,95};
    List<Integer> Temps = Arrays.asList(temps);
    Integer[] times = {20,40,60,80,100,120,140,160,180,200,210};
    List<Integer> Times = Arrays.asList(times);

    EditText etRpmAdv = null;
    EditText etTempAdv = null;
    EditText etTimeAdv = null;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED,intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_program);

        Knob knob1 = (Knob) findViewById(R.id.knob1Advanced);
        Knob knob2 = (Knob) findViewById(R.id.knob2Advanced);
        Knob knob3 = (Knob) findViewById(R.id.knob3Advanced);

        final CheckBox cbCustomPrograms = (CheckBox) findViewById(R.id.cbAdvancedCustomPrograms);

        final TextView tvName = (TextView) findViewById(R.id.tvAdvancedName);
        final EditText etName = (EditText) findViewById(R.id.etAdvancedName);
        final TextView tvFavorites = (TextView) findViewById(R.id.tvAdvancedFavorites);
        final TextView tvIcon = (TextView) findViewById(R.id.tvAdvancedIcon);
        etRpmAdv = findViewById(R.id.etRpmAdv);
        etTimeAdv = findViewById(R.id.etTimeAdv);
        etTempAdv = findViewById(R.id.etTempAdv);
        btnSelectIco = (Button) findViewById(R.id.btnAdvancedSelectIco);
        btnStart = (Button) findViewById(R.id.btnAdvancedStart);
        btnCancel = (Button) findViewById(R.id.btnAdvancedCancel);
        ivHeart = (ImageView) findViewById(R.id.ivAdvancedHeart);
        tbAdvancedEco = findViewById(R.id.tbAdvancedEco);
        tbAdvancedDryer = findViewById(R.id.tbAdvancedDryer);
        tbAdvancedPrewash = findViewById(R.id.tbAdvancedPrewash);


        advProg = new Program();
        advProg.setName("Advanced Program");

        tbAdvancedEco.setChecked(advProg.isEco());
        tbAdvancedDryer.setChecked(advProg.isDryer());
        tbAdvancedPrewash.setChecked(advProg.isPrewash());

        knob1.setState(0);
        etRpmAdv.setText(Integer.toString(advProg.getRPM()));

        knob2.setState(0);
        etTempAdv.setText(Integer.toString(advProg.getTEMP()));

        knob3.setState(0);
        etTimeAdv.setText(Integer.toString((advProg.getTIME())));

        knob1.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                advProg.setRPM(rpms[state]);
                etRpmAdv.setText(Integer.toString(rpms[state]));
            }
        });

        knob2.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                advProg.setTEMP(temps[state]);
                etTempAdv.setText(Integer.toString(temps[state]));
            }
        });

        knob3.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
                advProg.setTIME(times[state]);
                etTimeAdv.setText(Integer.toString(times[state]));
            }
        });

        tbAdvancedEco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advProg.setEco(tbAdvancedEco.isChecked());
            }
        });

        tbAdvancedDryer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advProg.setDryer(tbAdvancedDryer.isChecked());
            }
        });

        tbAdvancedPrewash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advProg.setPrewash(tbAdvancedPrewash.isChecked());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeScreen.selectedProgram!=null){
                    Toast.makeText(AdvancedProgram.this, "Program " + HomeScreen.selectedProgram.getName() + " is running. Please wait for it to finish, or stop it first!", Toast.LENGTH_LONG).show();
                }else {
                    if (btnStart.getText().toString().equals("Save and Start")) { //TODO IDIOUS ELEGXOUS KAI STHN EDIT ME TA ONOMATA
                        //TODO FANTAZOMAI DEN XREIAZOMASTE ELEGXOUS GIA DESCRIPTION
                        if (advProg.getName().equals("Advanced Program") && etName.getText().toString().equals("")) {

                            Toast.makeText(view.getContext(), "Please specify a name for the program!!", Toast.LENGTH_SHORT).show();

                        } else if (programDAO.find(etName.getText().toString()) != null) {

                            Toast.makeText(view.getContext(), "Program name already exists bitch!!", Toast.LENGTH_SHORT).show();

                        } else {

                            advProg.setName(etName.getText().toString());
                            programDAO.save(advProg);
                            HomeScreen.selectedProgram = advProg;
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();

                        }
                    } else {
                        HomeScreen.selectedProgram = advProg;
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        cbCustomPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbCustomPrograms.isChecked()){
                    tvName.setVisibility(View.VISIBLE);
                    tvName.setEnabled(true);
                    etName.setVisibility(View.VISIBLE);
                    etName.setEnabled(true);
                    tvFavorites.setVisibility(View.VISIBLE);
                    tvFavorites.setEnabled(true);
                    tvIcon.setVisibility(View.VISIBLE);
                    tvIcon.setEnabled(true);
                    btnSelectIco.setVisibility(View.VISIBLE);
                    btnSelectIco.setEnabled(true);
                    ivHeart.setVisibility(View.VISIBLE);
                    ivHeart.setEnabled(true);
                    btnStart.setText("Save and Start");

                    ivHeart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            advProg.setFavorited(!advProg.isFavorited());
                            if(advProg.isFavorited()){
                                ((ImageView)view.findViewById(R.id.ivAdvancedHeart)).setImageResource(R.drawable.filled_heart);
                            }else{
                                ((ImageView)view.findViewById(R.id.ivAdvancedHeart)).setImageResource(R.drawable.heartico);
                            }
                            Toast.makeText(view.getContext(), (advProg.isFavorited()?" Favorited!":" Unfavorited!"), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    tvName.setVisibility(View.GONE);
                    tvName.setEnabled(false);
                    etName.setVisibility(View.GONE);
                    etName.setEnabled(false);
                    tvFavorites.setVisibility(View.GONE);
                    tvFavorites.setEnabled(false);
                    tvIcon.setVisibility(View.GONE);
                    tvIcon.setEnabled(false);
                    btnSelectIco.setVisibility(View.GONE);
                    btnSelectIco.setEnabled(false);
                    ivHeart.setVisibility(View.GONE);
                    ivHeart.setEnabled(false);
                    btnStart.setText("Start");
                }
            }
        });


    }
}
