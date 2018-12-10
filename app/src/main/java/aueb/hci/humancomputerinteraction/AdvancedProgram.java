package aueb.hci.humancomputerinteraction;

import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import it.beppi.knoblibrary.Knob;

public class AdvancedProgram extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_program);

        Knob knob1 = (Knob) findViewById(R.id.knob1);
        Knob knob2 = (Knob) findViewById(R.id.knob2);
        Knob knob3 = (Knob) findViewById(R.id.knob3);

        final CheckBox cbCustomPrograms = (CheckBox) findViewById(R.id.cbCustomPrograms);

        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final TextView tvFavorites = (TextView) findViewById(R.id.tvFavorites);
        final TextView tvIcon = (TextView) findViewById(R.id.tvIcon);
        final Button btnSelectIco = (Button) findViewById(R.id.btnSelectIco);
        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final ImageView ivHeart = (ImageView) findViewById(R.id.ivHeart);

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
