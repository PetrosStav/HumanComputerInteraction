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
import android.widget.ImageView;
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
    static Program selectedProgram = null;
    static Program runningProgram = null;

    TextView tvProgram = null;
    TextView tvTimeRemaining = null;
    ProgressBar progressBar = null;
    ValueAnimator animator = null;
    ValueAnimator animatorTime = null;

    ValueAnimator animatorSpecial = null;
    ValueAnimator animatorTimeSpecial = null;

    ImageView ivCircleWasher, ivCircleDryer, ivCirclePrewasher;
    Button start = null;

    ImageView ivHomeMic, ivHomeSpeaker, ivHomeNotification;

    private boolean animCanceled = false;
    private boolean animSpecialCanceled = false;

    private boolean micEnabled = true;
    private boolean speakerEnabled = true;
    private boolean notificationsEnabled = true;

    private void programRunHelper(){

        // Clear the list adapter selection
        adapter.selectedProgram = null;
        if(adapter.previousView!=null) adapter.previousView.setBackgroundColor(Color.parseColor("#FAFAFAFA"));
        adapter.previousView = null;

        runningProgram = selectedProgram;

        selectedProgram = null;

        if(runningProgram.isPrewash()){
            tvProgram.setText("Program: " + runningProgram.getName()+ " - Prewash");

            // Start the Prewash
            animatorSpecial = ValueAnimator.ofInt(0, progressBar.getMax());
            animatorSpecial.setInterpolator(new LinearInterpolator());
            animatorSpecial.setDuration(10*1000); // 10 mins for the dryer
            animatorSpecial.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    progressBar.setProgress((Integer) valueAnimator.getAnimatedValue());
                }
            });

            animatorSpecial.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Prewash was cancelled!", Toast.LENGTH_LONG).show();
                    animSpecialCanceled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    super.onAnimationEnd(animation);
                    start.setText("Start");
                    ivCirclePrewasher.setImageResource(R.drawable.circle);
                    if(animSpecialCanceled){
                        animSpecialCanceled=false;
                        return;
                    }
                    Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Prewash has finished!", Toast.LENGTH_LONG).show();

                    // Start program
                    tvProgram.setText("Program: " + runningProgram.getName());

                    animator = ValueAnimator.ofInt(0, progressBar.getMax());
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setDuration(runningProgram.getTIME()*1000);
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
                            start.setText("Start");
                            ivCircleWasher.setImageResource(R.drawable.circle);
                            if(animCanceled) {
                                animCanceled = false;
                                return;
                            }
                            Toast.makeText(HomeScreen.this, runningProgram.getName() + " has finished washing!", Toast.LENGTH_LONG).show();

                            if(runningProgram.isDryer()){

                                tvProgram.setText("Program: " + runningProgram.getName()+ " - Dryer");

                                // Start the Dryer
                                animatorSpecial = ValueAnimator.ofInt(0, progressBar.getMax());
                                animatorSpecial.setInterpolator(new LinearInterpolator());
                                animatorSpecial.setDuration(10*1000); // 10 mins for the dryer
                                animatorSpecial.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        progressBar.setProgress((Integer) valueAnimator.getAnimatedValue());
                                    }
                                });

                                animatorSpecial.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        super.onAnimationCancel(animation);
                                        Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Dryer was cancelled!", Toast.LENGTH_LONG).show();
                                        animSpecialCanceled = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                                        super.onAnimationEnd(animation);
                                        start.setText("Start");
                                        ivCircleDryer.setImageResource(R.drawable.circle);
                                        if(animSpecialCanceled){
                                            animSpecialCanceled=false;
                                            return;
                                        }
                                        Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Dryer has finished!", Toast.LENGTH_LONG).show();
                                        runningProgram = null;
                                    }
                                });

                                animatorTimeSpecial = ValueAnimator.ofInt(10,0);
                                animatorTimeSpecial.setInterpolator(new LinearInterpolator());
                                animatorTimeSpecial.setDuration(10*1000);
                                animatorTimeSpecial.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        tvTimeRemaining.setText("Time Remaining: " + valueAnimator.getAnimatedValue() + " sec");
                                    }
                                });

                                animatorTimeSpecial.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        super.onAnimationCancel(animation);
                                        tvProgram.setText("Program: ");
                                        tvTimeRemaining.setText("Time Remaining: ");
                                        animSpecialCanceled = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        if(animSpecialCanceled) {
                                            animSpecialCanceled = false;
                                            return;
                                        }
                                        tvTimeRemaining.setText("Time Remaining: Finished!");
                                    }
                                });

                                start.setText("Stop");

                                ivCircleDryer.setImageResource(R.drawable.circlered);

                                animatorSpecial.start();
                                animatorTimeSpecial.start();

                            }else {
                                runningProgram = null;
                            }

                        }
                    });

                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            Toast.makeText(HomeScreen.this, runningProgram.getName() + " was canceled!", Toast.LENGTH_LONG).show();
                            runningProgram = null;
                            animCanceled = true;
                        }
                    });

                    animatorTime = ValueAnimator.ofInt(runningProgram.getTIME(),0);
                    animatorTime.setInterpolator(new LinearInterpolator());
                    animatorTime.setDuration(runningProgram.getTIME()*1000);
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
                            if(animCanceled) {
                                animCanceled = false;
                                return;
                            }
//                            tvProgram.setText("Program: ");
                            tvTimeRemaining.setText("Time Remaining: Finished!");
                        }
                    });

                    animatorTime.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            tvProgram.setText("Program: ");
                            tvTimeRemaining.setText("Time Remaining: ");
                            animCanceled = true;
                        }
                    });

                    start.setText("Stop");

                    ivCircleWasher.setImageResource(R.drawable.circlered);

                    animator.start();
                    animatorTime.start();

                }
            });

            animatorTimeSpecial = ValueAnimator.ofInt(10,0);
            animatorTimeSpecial.setInterpolator(new LinearInterpolator());
            animatorTimeSpecial.setDuration(10*1000);
            animatorTimeSpecial.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    tvTimeRemaining.setText("Time Remaining: " + valueAnimator.getAnimatedValue() + " sec");
                }
            });

            animatorTimeSpecial.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    tvProgram.setText("Program: ");
                    tvTimeRemaining.setText("Time Remaining: ");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                }
            });

            start.setText("Stop");

            ivCirclePrewasher.setImageResource(R.drawable.circlered);

            animatorSpecial.start();
            animatorTimeSpecial.start();
        }else{ // No Prewash

            tvProgram.setText("Program: " + runningProgram.getName());

            animator = ValueAnimator.ofInt(0, progressBar.getMax());
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(runningProgram.getTIME()*1000);
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
                    start.setText("Start");
                    ivCircleWasher.setImageResource(R.drawable.circle);
                    if(animCanceled) {
                        animCanceled = false;
                        return;
                    }
                    Toast.makeText(HomeScreen.this, runningProgram.getName() + " has finished washing!", Toast.LENGTH_LONG).show();

                    if(runningProgram.isDryer()){

                        tvProgram.setText("Program: " + runningProgram.getName()+ " - Dryer");

                        // Start the Dryer
                        animatorSpecial = ValueAnimator.ofInt(0, progressBar.getMax());
                        animatorSpecial.setInterpolator(new LinearInterpolator());
                        animatorSpecial.setDuration(10*1000); // 10 mins for the dryer
                        animatorSpecial.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                progressBar.setProgress((Integer) valueAnimator.getAnimatedValue());
                            }
                        });

                        animatorSpecial.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationCancel(Animator animation) {
                                super.onAnimationCancel(animation);
                                Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Dryer was cancelled!", Toast.LENGTH_LONG).show();
                                animSpecialCanceled = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation, boolean isReverse) {
                                super.onAnimationEnd(animation);
                                start.setText("Start");
                                ivCircleDryer.setImageResource(R.drawable.circle);
                                if(animSpecialCanceled){
                                    animSpecialCanceled=false;
                                    return;
                                }
                                Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Dryer has finished!", Toast.LENGTH_LONG).show();
                                runningProgram = null;
                            }
                        });

                        animatorTimeSpecial = ValueAnimator.ofInt(10,0);
                        animatorTimeSpecial.setInterpolator(new LinearInterpolator());
                        animatorTimeSpecial.setDuration(10*1000);
                        animatorTimeSpecial.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                tvTimeRemaining.setText("Time Remaining: " + valueAnimator.getAnimatedValue() + " sec");
                            }
                        });

                        animatorTimeSpecial.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationCancel(Animator animation) {
                                super.onAnimationCancel(animation);
                                tvProgram.setText("Program: ");
                                tvTimeRemaining.setText("Time Remaining: ");
                                animSpecialCanceled = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if(animSpecialCanceled) {
                                    animSpecialCanceled = false;
                                    return;
                                }
                                tvTimeRemaining.setText("Time Remaining: Finished!");
                            }
                        });

                        start.setText("Stop");

                        ivCircleDryer.setImageResource(R.drawable.circlered);

                        animatorSpecial.start();
                        animatorTimeSpecial.start();

                    }else {
                        runningProgram = null;
                    }

                }
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    Toast.makeText(HomeScreen.this, runningProgram.getName() + " was canceled!", Toast.LENGTH_LONG).show();
                    runningProgram = null;
                    animCanceled = true;
                }
            });

            animatorTime = ValueAnimator.ofInt(runningProgram.getTIME(),0);
            animatorTime.setInterpolator(new LinearInterpolator());
            animatorTime.setDuration(runningProgram.getTIME()*1000);
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
                    if(animCanceled) {
                        animCanceled = false;
                        return;
                    }
//                            tvProgram.setText("Program: ");
                    tvTimeRemaining.setText("Time Remaining: Finished!");
                }
            });

            animatorTime.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    tvProgram.setText("Program: ");
                    tvTimeRemaining.setText("Time Remaining: ");
                    animCanceled = true;
                }
            });

            start.setText("Stop");

            ivCircleWasher.setImageResource(R.drawable.circlered);

            animator.start();
            animatorTime.start();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 3:case 2:
                programData = programdao.findAll();
                adapter.loadData(programData);
                if(resultCode == Activity.RESULT_OK){
                    programRunHelper();
                }
                break;
            case 1:default:
                if(resultCode == Activity.RESULT_OK){
                    programData = programdao.findAll();
                    adapter.loadData(programData);
                }
                break;
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
        ivCircleWasher = findViewById(R.id.ivCircleWasher);
        ivCircleDryer = findViewById(R.id.ivCircleDryer);
        ivCirclePrewasher = findViewById(R.id.ivCirclePrewasher);
        start = findViewById(R.id.btStart);
        Button start_dryer = findViewById(R.id.btStartDryer);
        ListView lvFavorites = findViewById(R.id.lvFavorites);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        tvProgram = findViewById(R.id.tvProgram);
        tvTimeRemaining = findViewById(R.id.tvTimeRemaining);

        ivHomeMic = findViewById(R.id.ivHomeMic);
        ivHomeSpeaker = findViewById(R.id.ivHomeSpeaker);
        ivHomeNotification = findViewById(R.id.ivHomeNotification);

        if(!initialized){
            new DataInitializer().prepareData();
            initialized = true;
            Toast.makeText(this,"Loading Data!!",Toast.LENGTH_LONG).show();
            progressBar.setProgress(0);
        }

        findViewById(R.id.ivCartoon).setBackgroundColor(Color.parseColor("#FAFAFAFA")); // set cartoon's color to gray

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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start.getText().toString().equals("Start")){
                    if(selectedProgram != null){
                        programRunHelper();
                    }else{
                        Toast.makeText(HomeScreen.this, "Choose a favorite program to start!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(animatorSpecial!=null) {
                        animatorSpecial.cancel();
                        animatorTimeSpecial.cancel();
                    }
                    if(animator!=null) {
                        animator.cancel();
                        animatorTime.cancel();
                    }
                    progressBar.setProgress(0);
                    // TODO IF WE WANT ADD PAUSE
                    start.setText("Start");
                    runningProgram = null;
                }


            }
        });

        ivHomeMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                micEnabled = !micEnabled;
                if(micEnabled){
                    ivHomeMic.setImageResource(R.drawable.micnot);
                }else{
                    ivHomeMic.setImageResource(R.drawable.mic);
                }
            }
        });

        ivHomeSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakerEnabled = !speakerEnabled;
                if(speakerEnabled){
                    ivHomeSpeaker.setImageResource(R.drawable.speaknot);
                }else{
                    ivHomeSpeaker.setImageResource(R.drawable.speak);
                }
            }
        });

        ivHomeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationsEnabled = !notificationsEnabled;
                if(notificationsEnabled){
                    ivHomeNotification.setImageResource(R.drawable.notifynot);
                }else{
                    ivHomeNotification.setImageResource(R.drawable.notify);
                }
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, HelpScreen.class);
                startActivity(intent);
            }
        });

    }


}
