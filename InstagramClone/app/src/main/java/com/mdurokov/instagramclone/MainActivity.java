package com.mdurokov.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean loginSignupSwitch = false;
    private TextView tvSwitchToSignup, tvSwitchToLogin;
    private Button btnLogin, btnSignup;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void initComponents() {
        tvSwitchToLogin = findViewById(R.id.tvSwitchToLogin);
        tvSwitchToSignup = findViewById(R.id.tvSwitchToSignup);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
    }

    public void toggleSignupLogin(View view){
        if(loginSignupSwitch){
            tvSwitchToLogin.setVisibility(View.INVISIBLE);
            btnSignup.setVisibility(View.INVISIBLE);
            tvSwitchToSignup.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            loginSignupSwitch = false;
        } else {
            tvSwitchToLogin.setVisibility(View.VISIBLE);
            btnSignup.setVisibility(View.VISIBLE);
            tvSwitchToSignup.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            loginSignupSwitch = true;
        }
    }

    public void signup(View view){

    }

    public void login(View view){

    }

}
