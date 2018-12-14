package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

public class ManagePrograms extends AppCompatActivity {

    List<Program> programData = null;
    ProgramDAO programdao = new ProgramDAOmemory();
    ProgramAdapter adapter = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 4 && resultCode == Activity.RESULT_OK){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_programs);
        Button goback = findViewById(R.id.btnManageGoBack);
        Button edit = findViewById(R.id.btnManageEdit);
        GridView grid_images_manage = (GridView) findViewById(R.id.grid_images_manage);

        programData = programdao.findAll();

        adapter = new ProgramAdapter(this);
        adapter.loadData(programData, true);
        grid_images_manage.setAdapter(adapter);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagePrograms.this,EditProgram.class);
                intent.putExtra("PROGRAM",adapter.selectedProgram.getName());
                startActivityForResult(intent,4);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
//        super.onBackPressed();
    }

}
