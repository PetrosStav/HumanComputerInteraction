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

import it.beppi.knoblibrary.Knob;

public class AdvancedProgram extends AppCompatActivity {

    Button btnCancel = null;
    Button btnSelectIco = null;
    Button btnStart = null;
    ImageView ivHeart = null;

    Program advProg = null;

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
        btnSelectIco = (Button) findViewById(R.id.btnAdvancedSelectIco);
        btnStart = (Button) findViewById(R.id.btnAdvancedStart);
        btnCancel = (Button) findViewById(R.id.btnAdvancedCancel);
        ivHeart = (ImageView) findViewById(R.id.ivAdvancedHeart);

        advProg = new Program();
        advProg.setName("Advanced Program");
        advProg.setFavorited(false);
        advProg.setRPM(200);
        advProg.setTEMP(15);
        advProg.setTIME(10);

        knob1.setState(4);
        knob2.setState(4);
        knob3.setState(4);

        knob1.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
            }
        });

        knob2.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
            }
        });

        knob3.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                // do something
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
                Intent intent = new Intent();
                intent.putExtra("PROGRAM", advProg);
                setResult(Activity.RESULT_OK,intent);
                finish();
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
