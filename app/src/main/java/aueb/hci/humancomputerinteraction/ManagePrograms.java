package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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

        ImageView info = findViewById(R.id.ivManageInfo);

        findViewById(R.id.ivManageCartoon).setBackgroundColor(Color.parseColor("#FAFAFAFA"));

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
                if(adapter.selectedProgram!=null){
                    if(HomeScreen.runningProgram!=null && HomeScreen.runningProgram.getName().equals(adapter.selectedProgram.getName())){
                        Toast.makeText(ManagePrograms.this, HomeScreen.runningProgram.getName() + " is running. Please wait for it to finish, or stop it first!", Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(ManagePrograms.this, EditProgram.class);
                        intent.putExtra("PROGRAM", adapter.selectedProgram.getName());
                        startActivityForResult(intent, 4);
                    }
                }else{
                    Toast.makeText(ManagePrograms.this, "Choose a program to edit!", Toast.LENGTH_LONG).show();
                    // Play warning sound
                    MediaPlayer media = MediaPlayer.create(getApplicationContext(), R.raw.sound_effect);
                    media.start();
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoScreen.textId = 2;
                Intent intent = new Intent(ManagePrograms.this, InfoScreen.class);
                startActivity(intent);
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
