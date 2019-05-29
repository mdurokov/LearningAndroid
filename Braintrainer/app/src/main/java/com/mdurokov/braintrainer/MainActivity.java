package com.mdurokov.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnGo, btnPlayAgain;
    private TextView textViewTimer, textViewResult, textViewStatus, textViewQuestion;
    private GridLayout buttonsGrid;
    private int question1, question2, answer, answeredTotal, answeredCorrect;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answeredTotal = 0;
        answeredCorrect = 0;
        findComponents();
    }

    public void startGame(View view){

        // SHOW AND HIDE ITEMS
        btnGo.setVisibility(View.INVISIBLE);
        buttonsGrid.setVisibility(View.VISIBLE);
        textViewTimer.setVisibility(View.VISIBLE);
        textViewResult.setVisibility(View.VISIBLE);
        textViewQuestion.setVisibility(View.VISIBLE);
        textViewStatus.setVisibility(View.VISIBLE);
        btnPlayAgain.setVisibility(View.VISIBLE);

        // SET QUESTION
        setQuestion();

        // SET ONE CORRECT ANSWER AND THREE INCORRECT
        setAnswers();

        // CREATE TIMER
        countDownTimer = new CountDownTimer(30000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                btnPlayAgain.setText("Play Again!");
                textViewStatus.setText("Done!");

                btnAnswer1.setEnabled(false);
                btnAnswer2.setEnabled(false);
                btnAnswer3.setEnabled(false);
                btnAnswer4.setEnabled(false);
            }
        }.start();
    }

    private void setQuestion() {
        question1 = (int) Math.floor(Math.random()*(50)+1);
        question2 = (int) Math.floor(Math.random()*(50)+1);
        answer = question1 + question2;
        textViewQuestion.setText(String.valueOf(question1) + " + " + String.valueOf(question2));
        Log.i("TAG", "setQuestion: " + question1 + " + " + question2);
        Log.i("TAG", "setAnswer: " + answer);

}

    private void setAnswers() {
        int correctAnswerPosition = (int) Math.floor(Math.random()*(4)+1);
        int randomAnswer = randomiseAnswer();
        btnAnswer1.setText(String.valueOf(randomAnswer));
        randomAnswer = randomiseAnswer();
        btnAnswer2.setText(String.valueOf(randomAnswer));
        randomAnswer = randomiseAnswer();
        btnAnswer3.setText(String.valueOf(randomAnswer));
        randomAnswer = randomiseAnswer();
        btnAnswer4.setText(String.valueOf(randomAnswer));

        switch (correctAnswerPosition){
            case 1:
                btnAnswer1.setText(String.valueOf(answer));
                break;
            case 2:
                btnAnswer2.setText(String.valueOf(answer));
                break;
            case 3:
                btnAnswer3.setText(String.valueOf(answer));
                break;
            case 4:
                btnAnswer4.setText(String.valueOf(answer));
                break;
        }
    }

    private int randomiseAnswer(){
        int randomAnswer = (int) Math.floor(Math.random()*100+1);

        if(randomAnswer != answer){
            return randomAnswer;
        }else{
            return randomiseAnswer();
        }
    }

    // ASSOCIATE VARIABLE WITH VIEW
    private void findComponents() {
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        btnGo = findViewById(R.id.btnGo);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        textViewResult = findViewById(R.id.textViewResult);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewQuestion = findViewById(R.id.textViewQuestion);

        buttonsGrid = findViewById(R.id.buttonsGrid);
    }

    private void updateResult(Button button){
        answeredTotal++;
        if(button.getText().equals(String.valueOf(answer))){
            answeredCorrect++;
            textViewStatus.setText("Correct!");
        } else {
            textViewStatus.setText("Incorrect!");
        }
        textViewResult.setText(answeredCorrect + "/" + answeredTotal);
    }

    public void btn1Clicked(View view){
        updateResult(btnAnswer1);
        setQuestion();
        setAnswers();
    }
    public void btn2Clicked(View view){
        updateResult(btnAnswer2);
        setQuestion();
        setAnswers();
    }
    public void btn3Clicked(View view){
        updateResult(btnAnswer3);
        setQuestion();
        setAnswers();
    }
    public void btn4Clicked(View view){
        updateResult(btnAnswer4);
        setQuestion();
        setAnswers();
    }

    public void reset(View view){
        countDownTimer.cancel();
        countDownTimer.start();
        setQuestion();
        setAnswers();

        textViewStatus.setText("");
        textViewResult.setText("0/0");
        answeredCorrect = 0;
        answeredTotal = 0;
        btnPlayAgain.setText("Reset!");

        btnAnswer1.setEnabled(true);
        btnAnswer2.setEnabled(true);
        btnAnswer3.setEnabled(true);
        btnAnswer4.setEnabled(true);

    }

}
