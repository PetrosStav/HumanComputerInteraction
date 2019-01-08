package aueb.hci.humancomputerinteraction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Locale;

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

    ImageView ivHomeSpeaker, ivHomeNotification;

    private boolean animCanceled = false;
    private boolean animSpecialCanceled = false;

    private boolean speakerEnabled = true;
    private boolean notificationsEnabled = true;

    TextToSpeech tts = null;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("chan_wash", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void programRunHelper(){

        // Clear the list adapter selection
        adapter.selectedProgram = null;
        if(adapter.previousView!=null) adapter.previousView.setBackgroundColor(Color.parseColor("#FAFAFAFA"));
        adapter.previousView = null;

        runningProgram = selectedProgram;

        selectedProgram = null;

        if(runningProgram.isPrewash()){
            tvProgram.setText("Program: " + runningProgram.getName()+ " - Prewash");
            if(speakerEnabled) {
                tts.speak(runningProgram.getName() + ", Prewashing...", TextToSpeech.QUEUE_ADD, null);
            }
            if(notificationsEnabled){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "chan_wash")
                        .setSmallIcon(R.drawable.info)
                        .setContentTitle(runningProgram.getName())
                        .setContentText(runningProgram.getName() + ", Prewashing...")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, mBuilder.build());
            }

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
                    if(speakerEnabled){
                        tts.speak(runningProgram.getName() + " was cancelled!", TextToSpeech.QUEUE_ADD, null);
                    }
                    if(notificationsEnabled){
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                .setSmallIcon(R.drawable.info)
                                .setContentTitle(runningProgram.getName())
                                .setContentText(runningProgram.getName() + " was cancelled!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, mBuilder.build());
                    }
                    animSpecialCanceled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    super.onAnimationEnd(animation);
                    start.setText("Start Favorite");
                    ivCirclePrewasher.setImageResource(R.drawable.circle);
                    if(animSpecialCanceled){
                        animSpecialCanceled=false;
                        return;
                    }
                    Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Prewash has finished!", Toast.LENGTH_LONG).show();
                    if(speakerEnabled) {
                        tts.speak(runningProgram.getName() + ", Prewash has finished!", TextToSpeech.QUEUE_ADD, null);
                        tts.speak(runningProgram.getName() + ", Washing...", TextToSpeech.QUEUE_ADD, null);
                    }
                    if(notificationsEnabled){
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                .setSmallIcon(R.drawable.info)
                                .setContentTitle(runningProgram.getName())
                                .setContentText(runningProgram.getName() + ", Prewash has finished!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, mBuilder.build());
                    }
                    if(notificationsEnabled){
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                .setSmallIcon(R.drawable.info)
                                .setContentTitle(runningProgram.getName())
                                .setContentText(runningProgram.getName() + ", Washing...")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, mBuilder.build());
                    }
                    // Start Program
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
                            start.setText("Start Favorite");
                            ivCircleWasher.setImageResource(R.drawable.circle);
                            if(animCanceled) {
                                animCanceled = false;
                                return;
                            }
                            Toast.makeText(HomeScreen.this, runningProgram.getName() + " has finished washing!", Toast.LENGTH_LONG).show();
                            if(speakerEnabled) {
                                tts.speak(runningProgram.getName() + " has finished washing!", TextToSpeech.QUEUE_ADD, null);
                            }
                            if(notificationsEnabled){
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                        .setSmallIcon(R.drawable.info)
                                        .setContentTitle(runningProgram.getName())
                                        .setContentText(runningProgram.getName() + " has finished washing!")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                        .setAutoCancel(true);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                // notificationId is a unique int for each notification that you must define
                                notificationManager.notify(0, mBuilder.build());
                            }
                            if(runningProgram.isDryer()){

                                tvProgram.setText("Program: " + runningProgram.getName()+ " - Dryer");
                                if(speakerEnabled) {
                                    tts.speak(runningProgram.getName() + ", Drying...", TextToSpeech.QUEUE_ADD, null);
                                }
                                if(notificationsEnabled){
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                            .setSmallIcon(R.drawable.info)
                                            .setContentTitle(runningProgram.getName())
                                            .setContentText(runningProgram.getName() + ", Drying...")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                            .setAutoCancel(true);

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(0, mBuilder.build());
                                }
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
                                        if(speakerEnabled){
                                            tts.speak(runningProgram.getName() + " was cancelled!", TextToSpeech.QUEUE_ADD, null);
                                        }
                                        if(notificationsEnabled){
                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                                    .setSmallIcon(R.drawable.info)
                                                    .setContentTitle(runningProgram.getName())
                                                    .setContentText(runningProgram.getName() + "was cancelled!")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                            // notificationId is a unique int for each notification that you must define
                                            notificationManager.notify(0, mBuilder.build());
                                        }
                                        animSpecialCanceled = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                                        super.onAnimationEnd(animation);
                                        start.setText("Start Favorite");
                                        ivCircleDryer.setImageResource(R.drawable.circle);
                                        if(animSpecialCanceled){
                                            animSpecialCanceled=false;
                                            return;
                                        }
                                        Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Dryer has finished!", Toast.LENGTH_LONG).show();
                                        if(speakerEnabled) {
                                            tts.speak(runningProgram.getName() + ", Dryer has finished!", TextToSpeech.QUEUE_ADD, null);
                                        }
                                        if(notificationsEnabled){
                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                                    .setSmallIcon(R.drawable.info)
                                                    .setContentTitle(runningProgram.getName())
                                                    .setContentText(runningProgram.getName() + ", Dryer has finished!")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                            // notificationId is a unique int for each notification that you must define
                                            notificationManager.notify(0, mBuilder.build());
                                        }
                                        if(speakerEnabled) {
                                            tts.speak(runningProgram.getName() + " has finished!", TextToSpeech.QUEUE_ADD, null);
                                        }
                                        if(notificationsEnabled){
                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                                    .setSmallIcon(R.drawable.info)
                                                    .setContentTitle(runningProgram.getName())
                                                    .setContentText(runningProgram.getName() + " has finished!")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                            // notificationId is a unique int for each notification that you must define
                                            notificationManager.notify(0, mBuilder.build());
                                        }
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
                            if(speakerEnabled) {
                                tts.speak(runningProgram.getName() + "was canceled!", TextToSpeech.QUEUE_ADD, null);
                            }
                            if(notificationsEnabled){
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                        .setSmallIcon(R.drawable.info)
                                        .setContentTitle(runningProgram.getName())
                                        .setContentText(runningProgram.getName() + "was canceled!")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                        .setAutoCancel(true);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                // notificationId is a unique int for each notification that you must define
                                notificationManager.notify(0, mBuilder.build());
                            }
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

            if(speakerEnabled) {
                tts.speak(runningProgram.getName() + ", Washing...", TextToSpeech.QUEUE_ADD, null);
            }
            if(notificationsEnabled){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                        .setSmallIcon(R.drawable.info)
                        .setContentTitle(runningProgram.getName())
                        .setContentText(runningProgram.getName() + ", Washing...")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, mBuilder.build());
            }
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
                    start.setText("Start Favorite");
                    ivCircleWasher.setImageResource(R.drawable.circle);
                    if(animCanceled) {
                        animCanceled = false;
                        return;
                    }
                    Toast.makeText(HomeScreen.this, runningProgram.getName() + " has finished washing!", Toast.LENGTH_LONG).show();
                    if(speakerEnabled) {
                        tts.speak(runningProgram.getName() + " has finished washing!", TextToSpeech.QUEUE_ADD, null);
                    }
                    if(notificationsEnabled){
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                .setSmallIcon(R.drawable.info)
                                .setContentTitle(runningProgram.getName())
                                .setContentText(runningProgram.getName() + " has finished washing!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, mBuilder.build());
                    }
                    if(runningProgram.isDryer()){

                        tvProgram.setText("Program: " + runningProgram.getName()+ " - Dryer");
                        if(speakerEnabled) {
                            tts.speak(runningProgram.getName() + ", Drying...", TextToSpeech.QUEUE_ADD, null);
                        }
                        if(notificationsEnabled){
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                    .setSmallIcon(R.drawable.info)
                                    .setContentTitle(runningProgram.getName())
                                    .setContentText(runningProgram.getName() + ", Drying...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(0, mBuilder.build());
                        }
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
                                if(speakerEnabled){
                                    tts.speak(runningProgram.getName() + " was cancelled!", TextToSpeech.QUEUE_ADD, null);
                                }
                                if(notificationsEnabled){
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                            .setSmallIcon(R.drawable.info)
                                            .setContentTitle(runningProgram.getName())
                                            .setContentText(runningProgram.getName() + " was cancelled!")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                            .setAutoCancel(true);

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(0, mBuilder.build());
                                }
                                animSpecialCanceled = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation, boolean isReverse) {
                                super.onAnimationEnd(animation);
                                start.setText("Start Favorite");
                                ivCircleDryer.setImageResource(R.drawable.circle);
                                if(animSpecialCanceled){
                                    animSpecialCanceled=false;
                                    return;
                                }
                                Toast.makeText(HomeScreen.this, runningProgram.getName()+" - Dryer has finished!", Toast.LENGTH_LONG).show();
                                if(speakerEnabled) {
                                    tts.speak(runningProgram.getName() + ", Dryer has finished!", TextToSpeech.QUEUE_ADD, null);
                                    tts.speak(runningProgram.getName() + " has finished!", TextToSpeech.QUEUE_ADD, null);
                                }
                                if(notificationsEnabled){
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                            .setSmallIcon(R.drawable.info)
                                            .setContentTitle(runningProgram.getName())
                                            .setContentText(runningProgram.getName() + ", Dryer has finished!")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                            .setAutoCancel(true);

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(0, mBuilder.build());
                                }
                                if(notificationsEnabled){
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                            .setSmallIcon(R.drawable.info)
                                            .setContentTitle(runningProgram.getName())
                                            .setContentText(runningProgram.getName() + " has finished!")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                            .setAutoCancel(true);

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(0, mBuilder.build());
                                }
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
                    if(speakerEnabled) {
                        tts.speak(runningProgram.getName() + " was cancelled!", TextToSpeech.QUEUE_ADD, null);
                    }
                    if(notificationsEnabled){
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "chan_wash")
                                .setSmallIcon(R.drawable.info)
                                .setContentTitle(runningProgram.getName())
                                .setContentText(runningProgram.getName() + " was cancelled!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, mBuilder.build());
                    }
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
        ListView lvFavorites = findViewById(R.id.lvFavorites);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        tvProgram = findViewById(R.id.tvProgram);
        tvTimeRemaining = findViewById(R.id.tvTimeRemaining);

        ivHomeSpeaker = findViewById(R.id.ivHomeSpeaker);
        ivHomeNotification = findViewById(R.id.ivHomeNotification);

        if(!initialized){
            new DataInitializer().prepareData();
            initialized = true;
            progressBar.setProgress(0);
        }

        createNotificationChannel();

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

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this,HelpScreen.class);
                startActivityForResult(intent,1);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start.getText().toString().equals("Start Favorite")){
                    if(selectedProgram != null){
                        programRunHelper();
                    }else{
                        Toast.makeText(HomeScreen.this, "Choose a favorite program to start!", Toast.LENGTH_LONG).show();
                        // Play warning sound
                        MediaPlayer media = MediaPlayer.create(getApplicationContext(), R.raw.sound_effect);
                        media.start();
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
                    start.setText("Start Favorite");
                    runningProgram = null;
                }


            }
        });

        ivHomeSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakerEnabled = !speakerEnabled;
                if(!speakerEnabled){
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
                if(!notificationsEnabled){
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

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");

                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.ivCartoon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("That tickles!", TextToSpeech.QUEUE_ADD, null);
            }
        });

    }


}
