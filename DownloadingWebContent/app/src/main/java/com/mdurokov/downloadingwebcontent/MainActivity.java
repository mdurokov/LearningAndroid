package com.mdurokov.downloadingwebcontent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames= new ArrayList<String>();
    int chosenCeleb = 0;
    ImageView imageView;
    String[] answers = new String[4];
    int correctAnswer = 0;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;

    public void celebChosen(View view){
        if(view.getTag().toString().equals(Integer.toString(correctAnswer))){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "It was " + celebNames.get(correctAnswer), Toast.LENGTH_SHORT).show();
        }

        newQuestion();
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;


            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public void newQuestion(){
        try{
            Random rand = new Random();
            chosenCeleb = rand.nextInt(celebURLs.size());

            ImageDownloader imageTask = new ImageDownloader();
            Bitmap celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();

            imageView.setImageBitmap(celebImage);

            correctAnswer = rand.nextInt(4);
            int incorectAnswer;
            for(int i = 0; i < 4; i++){
                if(i == correctAnswer){
                    answers[i] = celebNames.get(chosenCeleb);
                } else {
                    incorectAnswer = rand.nextInt(celebURLs.size());
                    while(incorectAnswer == chosenCeleb){
                        incorectAnswer = rand.nextInt(celebURLs.size());
                    }
                    answers[i] = celebNames.get(incorectAnswer);
                }
            }

            btnAnswer1.setText(answers[0]);
            btnAnswer2.setText(answers[1]);
            btnAnswer3.setText(answers[2]);
            btnAnswer4.setText(answers[3]);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        imageView = findViewById(R.id.imageView);

        DownloadTask task = new DownloadTask();
        String result = null;
        try{
            result = task.execute("http://posh24.se/kandisar").get();
            String[] splitResult = result.split("<div class=\"listedArticles\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while(m.find()) {
                celebURLs.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);

            while (m.find()){
                celebNames.add(m.group(1));
            }

            newQuestion();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
