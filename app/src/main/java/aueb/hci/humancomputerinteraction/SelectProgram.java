package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class SelectProgram extends AppCompatActivity {

    ArrayList<Program> programData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_program);
        Button common_select = findViewById(R.id.common_select);
        Button materials_select = findViewById(R.id.materials_select);
        Button stains_select = findViewById(R.id.stains_select);
        Button custom_select = findViewById(R.id.custom_select);
        Button tbSelectPrewash = findViewById(R.id.tbSelectPrewash);
        Button tbSelectEco = findViewById(R.id.tbSelectEco);
        Button tbSelectDryer = findViewById(R.id.tbSelectDryer);
        Button btnSelectCancel = findViewById(R.id.btnSelectCancel);
        Button btnSelectStart = findViewById(R.id.btnSelectStart);
        GridView grid_images_select = (GridView) findViewById(R.id.grid_images_select);

        Bundle data = getIntent().getExtras();

        programData = (ArrayList<Program>) data.get("PROGRAMS");

        ProgramAdapter adapter = new ProgramAdapter(this);
        adapter.loadData(programData, false);

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
