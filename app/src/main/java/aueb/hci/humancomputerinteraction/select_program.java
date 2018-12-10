package aueb.hci.humancomputerinteraction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class select_program extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_program);
        Button common = findViewById(R.id.common_select);
        Button materials = findViewById(R.id.materials_select);
        Button stains = findViewById(R.id.stains_select);
        Button custom = findViewById(R.id.custom_select);
        Button prewash = findViewById(R.id.prewash_select);
        Button eco = findViewById(R.id.eco_select);
        Button dryer = findViewById(R.id.dryer_select);
        Button cancel = findViewById(R.id.cancel_select);
        Button start = findViewById(R.id.start_select);
    }
}
