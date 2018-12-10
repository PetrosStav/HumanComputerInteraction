package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class SelectProgram extends AppCompatActivity {

    ArrayList<Program> programData = null;

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
        GridView grid_images_select = (GridView) findViewById(R.id.grid_images_select);

        Bundle data = getIntent().getExtras();

        programData = (ArrayList<Program>)data.get("PROGRAMS");

        ProgramAdapter adapter = new ProgramAdapter(this);
        adapter.loadData(programData);

        grid_images_select.setAdapter(adapter);



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("PROGRAMS",programData);
        setResult(Activity.RESULT_OK,intent);
        finish();
//        super.onBackPressed();
    }

}
