package mdurokov.androidinterview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SimpleQuestion extends AppCompatActivity implements View.OnClickListener {

    TextView tvTotalLength, tvCurrentQuestion, tvQuestion, tvAnswer;
    Button btnLeft, btnRight, btnAnswer;
    String[] simpleQuestions, simpleAnswers;

    // Text to speach
    TextToSpeech ttsObject;
    int result;


    int index;
    private static final String DEFAULT_ANSWER = "Press \"A\" Button for the Answer";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        ttsObject = new TextToSpeech(SimpleQuestion.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    result = ttsObject.setLanguage(Locale.US);
                }else{
                    Toast.makeText(getApplicationContext(), "Your device is not compatible.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout questionPage = findViewById(R.id.questionsTitleBar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.questions_title_bar);

        Button btnSpeak = findViewById(R.id.btnSpeak);
        Button btnMute = findViewById(R.id.btnMute);
        TextView tvSimple = findViewById(R.id.tvQuestionsTitleBar);
        tvSimple.setText("Simple Questions");


        index = 0;
        simpleQuestions = getResources().getStringArray(R.array.simple_question);
        simpleAnswers = getResources().getStringArray(R.array.simple_answer);
        tvTotalLength = findViewById(R.id.tvYY);

        tvCurrentQuestion = findViewById(R.id.tvXX);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvAnswer = findViewById(R.id.tvAnswer);

        btnAnswer = findViewById(R.id.btnAnswer);
        btnLeft = findViewById(R.id.btnLeftArrow);
        btnRight = findViewById(R.id.btnRightArrow);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnAnswer.setOnClickListener(this);
        btnSpeak.setOnClickListener(this);
        btnMute.setOnClickListener(this);

        tvQuestion.setText(simpleQuestions[index]);
        tvCurrentQuestion.setText(String.valueOf((index + 1)));
        tvAnswer.setText(DEFAULT_ANSWER);
        tvTotalLength.setText("/" + simpleQuestions.length);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAnswer:
                tvAnswer.setText(simpleAnswers[index]);
                break;
            case R.id.btnLeftArrow:
                if(index < 1){
                    break;
                }
                tvQuestion.setText(simpleQuestions[--index]);
                tvAnswer.setText(DEFAULT_ANSWER);
                tvCurrentQuestion.setText(String.valueOf(index +1));
                if(ttsObject != null){
                    ttsObject.stop();
                }
                break;
            case R.id.btnRightArrow:
                if(index > 8){
                    break;
                }
                tvQuestion.setText(simpleQuestions[++index]);
                tvAnswer.setText(DEFAULT_ANSWER);
                tvCurrentQuestion.setText(String.valueOf(index +1));
                if(ttsObject != null){
                    ttsObject.stop();
                }
                break;
            case R.id.btnSpeak:
                if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(getApplicationContext(), "Your device is not compatible",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(tvAnswer.getText().equals(DEFAULT_ANSWER)){
                        Toast.makeText(getApplicationContext(), "First reveal answer.",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        ttsObject.speak(simpleAnswers[index], TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                break;
            case R.id.btnMute:
                if(ttsObject != null){
                    ttsObject.stop();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ttsObject != null){
            ttsObject.stop();
            ttsObject.shutdown();
        }
    }
}
