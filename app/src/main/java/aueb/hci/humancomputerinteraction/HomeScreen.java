package aueb.hci.humancomputerinteraction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    List<Program> programData = null;
    ProgramListAdapter adapter = null;
    private static boolean initialized = false;
    ProgramDAO programdao = new ProgramDAOmemory();
    Program selectedProgram = null;

    TextView tvProgram = null;
    TextView tvTimeRemaining = null;
    ProgressBar progressBar = null;
    ValueAnimator animator = null;
    ValueAnimator animatorTime = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 3:
                if(resultCode == Activity.RESULT_OK){
                    Bundle data_select = data.getExtras();
                    selectedProgram = (Program)data_select.get("PROGRAM");

                    tvProgram.setText("Program: " + selectedProgram.getName());

                    animator = ValueAnimator.ofInt(0, progressBar.getMax());
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setDuration(selectedProgram.getTIME()*1000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            progressBar.setProgress((Integer) valueAnimator.getAnimatedValue());
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // start your activity here
                            Toast.makeText(HomeScreen.this, "Program: " + selectedProgram.getName() + " has finished bitch!!!", Toast.LENGTH_LONG).show();
                        }
                    });

                    animatorTime = ValueAnimator.ofInt(selectedProgram.getTIME(),0);
                    animatorTime.setInterpolator(new LinearInterpolator());
                    animatorTime.setDuration(selectedProgram.getTIME()*1000);
                    animatorTime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tvTimeRemaining.setText("Time Remaining: " + valueAnimator.getAnimatedValue() + " sec");
                        }
                    });
                    animatorTime.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });

                    animator.start();
                    animatorTime.start();

                }
                break;
            case 2:
                programData = programdao.findAll();
                adapter.loadData(programData);
                if(resultCode == Activity.RESULT_OK){
                    Bundle data_select = data.getExtras();
                    String pname = (String)data_select.get("PROGRAM_NAME");

                    selectedProgram = programdao.find(pname);
                    tvProgram.setText("Program: " + selectedProgram.getName());

                    animator = ValueAnimator.ofInt(0, progressBar.getMax());
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setDuration(selectedProgram.getTIME()*1000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            progressBar.setProgress((Integer) valueAnimator.getAnimatedValue());
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // start your activity here
                            Toast.makeText(HomeScreen.this, "Program: " + selectedProgram.getName() + " has finished bitch!!!", Toast.LENGTH_LONG).show();
                        }
                    });

                    animatorTime = ValueAnimator.ofInt(selectedProgram.getTIME(),0);
                    animatorTime.setInterpolator(new LinearInterpolator());
                    animatorTime.setDuration(selectedProgram.getTIME()*1000);
                    animatorTime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tvTimeRemaining.setText("Time Remaining: " + valueAnimator.getAnimatedValue() + " sec");
                        }
                    });
                    animatorTime.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });

                    animator.start();
                    animatorTime.start();

                }
                break;
            case 1:default:
                if(resultCode == Activity.RESULT_OK){
                    programData = programdao.findAll();
                    adapter.loadData(programData);
                }
                break;
        }
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            programData = programdao.findAll();
            adapter.loadData(programData);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button select_program = findViewById(R.id.btSelectProgram);
        Button advanced_program = findViewById(R.id.btAdvancedProgram);
        Button manage_programs = findViewById(R.id.btManagePrograms);
        Button help = findViewById(R.id.btHelp);
        Button start = findViewById(R.id.btStart);
        Button start_dryer = findViewById(R.id.btStartDryer);
        ListView lvFavorites = findViewById(R.id.lvFavorites);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        tvProgram = findViewById(R.id.tvProgram);
        tvTimeRemaining = findViewById(R.id.tvTimeRemaining);

        if(!initialized){
            new DataInitializer().prepareData();
            initialized = true;
            Toast.makeText(this,"Loading Data!!",Toast.LENGTH_LONG).show();
            progressBar.setProgress(0);
        }

        programData = programdao.findAll();
        adapter = new ProgramListAdapter(this);
        adapter.loadData(programData);

        lvFavorites.setAdapter(adapter);

        select_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this,SelectProgram.class);
                startActivityForResult(intent,2);
            }
        });

        advanced_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this,AdvancedProgram.class);
                startActivityForResult(intent,3);
            }
        });

        manage_programs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this,ManagePrograms.class);
                startActivityForResult(intent,1);
            }
        });
    }


}
