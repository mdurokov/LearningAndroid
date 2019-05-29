package com.mdurokov.timerdemo;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView timerTextView;
    private Button button;
    private boolean timeStarted;
    private long minutes;
    private int seconds;
    private int time;
    private MediaPlayer mediaPlayer;


    private long calculateMinutes(int time){
        return (time / 1000)  / 60;
    }

    private int calculateSeconds(int time){
        return (int)((time / 1000) % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeStarted = false;
        mediaPlayer = MediaPlayer.create(this, R.raw.horn);
        seekBar = findViewById(R.id.seekBar);
        timerTextView = findViewById(R.id.textViewTimer);
        button = findViewById(R.id.btnStart);
        seekBar.setProgress(30);
        seekBar.setMax(600);

        time = seekBar.getProgress();
        time = time * 1000;
        minutes = calculateMinutes(time);
        seconds = calculateSeconds(time);

        if(seconds <= 10){
            timerTextView.setText(minutes + ":0" + seconds);
        }else{
            timerTextView.setText(minutes+ ":" + seconds);
        }


        button.setOnClickListener(new View.OnClickListener() {

            private void resetTimer() {
                timeStarted = false;
                countDownTimer.cancel();
                button.setText("Start");
                time = seekBar.getProgress();
                time = time * 1000;
                minutes = calculateMinutes(time);
                seconds = calculateSeconds(time);

                if(seconds <= 10){
                    timerTextView.setText(minutes + ":0" + seconds);
                }else{
                    timerTextView.setText(minutes+ ":" + seconds);
                }
            }

            CountDownTimer countDownTimer;
            @Override
            public void onClick(View v) {
                if(!timeStarted){
                    button.setText("Reset");
                    countDownTimer = new CountDownTimer(time, 1000) {
                        long countdownM = minutes;
                        int countdownS = seconds;
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(countdownS == 0){
                                countdownS = 60;
                                timerTextView.setText(--countdownM + ":" + countdownS);
                            }else{
                                if(countdownS <= 10){
                                    timerTextView.setText(countdownM + ":0" + --countdownS);
                                }else{
                                    timerTextView.setText(countdownM + ":" + --countdownS);
                                }
                            }
                        }

                        @Override
                        public void onFinish() {
                            resetTimer();
                            mediaPlayer.start();
                        }
                    };

                    countDownTimer.start();
                    timeStarted = true;
                }else{
                    resetTimer();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!timeStarted){
                    time = seekBar.getProgress();
                    time = time * 1000;
                    minutes = calculateMinutes(time);
                    seconds = calculateSeconds(time);

                    if(seconds < 10){
                        timerTextView.setText(minutes + ":0" + seconds);
                    }else{
                        timerTextView.setText(minutes+ ":" + seconds);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
