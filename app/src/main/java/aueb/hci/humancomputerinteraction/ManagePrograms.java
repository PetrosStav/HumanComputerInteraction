package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class ManagePrograms extends AppCompatActivity {

    ArrayList<Program> programData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_programs);
        Button cancel = findViewById(R.id.btnManageCancel);
        Button save = findViewById(R.id.btnManageSave);
        GridView grid_images_manage = (GridView) findViewById(R.id.grid_images_manage);

        Bundle data = getIntent().getExtras();

        programData = (ArrayList<Program>) data.get("PROGRAMS");

        ProgramAdapter adapter = new ProgramAdapter(this);
        adapter.loadData(programData, true);
        grid_images_manage.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programData = (ArrayList<Program>) adapter.getCopyOfData();
                Intent intent = new Intent();
                intent.putExtra("PROGRAMS",programData);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("PROGRAMS",programData);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
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
