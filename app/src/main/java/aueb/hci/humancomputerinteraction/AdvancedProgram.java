package aueb.hci.humancomputerinteraction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.beppi.knoblibrary.Knob;

public class AdvancedProgram extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_program);

        Knob knob1 = (Knob) findViewById(R.id.knob1);
        Knob knob2 = (Knob) findViewById(R.id.knob2);
        Knob knob3 = (Knob) findViewById(R.id.knob3);

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
    }
}
