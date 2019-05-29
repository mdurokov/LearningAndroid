package mdurokov.androidinterview;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.net.URI;

public class FrontPage extends AppCompatActivity implements View.OnClickListener {

    private Button btnSimple, btnTough, btnSeeOther, btnRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);

        LinearLayout frontPage = findViewById(R.id.frontPageTitleBar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.frontpage_title_bar);

        btnSimple = findViewById(R.id.btnSimpleQ);
        btnTough = findViewById(R.id.btnToughQ);
        btnSeeOther = findViewById(R.id.btnSeeOther);
        btnRate = findViewById(R.id.btnRateApp);

        btnSimple.setOnClickListener(this);
        btnTough.setOnClickListener(this);
        btnSeeOther.setOnClickListener(this);
        btnRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSimpleQ:
                Intent intentSimpleQuestions = new Intent(FrontPage.this, SimpleQuestion.class);
                startActivity(intentSimpleQuestions);
                break;
            case R.id.btnToughQ:
                Intent intentToughQuestions = new Intent(FrontPage.this, ToughQuestions.class);
                startActivity(intentToughQuestions);
                break;
            case R.id.btnSeeOther:
                try{
                    Uri uri = Uri.parse("market://search?q=Sriyank");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                }catch (ActivityNotFoundException e){
                    Uri uri = Uri.parse("https://play.google.com/store/search?q=Sriyank");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                }
                break;
            case R.id.btnRateApp:
                try{
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                }catch (ActivityNotFoundException e){
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                }

                break;
        }
    }
}
