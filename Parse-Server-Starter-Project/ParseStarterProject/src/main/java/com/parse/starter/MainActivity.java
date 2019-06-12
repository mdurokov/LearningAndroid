package com.parse.starter;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

    private boolean loginSignupSwitch = false;
    private TextView tvSwitchToSignup, tvSwitchToLogin;
    private Button btnLogin, btnSignup;
    private EditText etUsername, etPassword;
    private ImageView ivLogo;
    private ConstraintLayout backgroundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setTitle("Instagram clone");
        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }
        etPassword.setOnKeyListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void initComponents() {
        tvSwitchToLogin = (TextView) findViewById(R.id.tvSwitchToLogin);
        tvSwitchToSignup = (TextView) findViewById(R.id.tvSwitchToSignup);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        backgroundLayout = (ConstraintLayout) findViewById(R.id.backgroundLayout);
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
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();


        if(username.equals("") || password.equals("")){
            Toast.makeText(this, "username and password is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_SHORT).show();
                    showUserList();
                }else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void login(View view){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(username.equals("") || password.equals("")){
            Toast.makeText(this, "username and password is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "Loged In!", Toast.LENGTH_SHORT).show();
                    showUserList();
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUserList() {
        Intent intent = new Intent(getApplicationContext(), ListOfUsersActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if(loginSignupSwitch){
                signup(view);
            }else{
                login(view);
            }
            removeKeyboard(view);
        }
        return false;
    }

    public void removeKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}
