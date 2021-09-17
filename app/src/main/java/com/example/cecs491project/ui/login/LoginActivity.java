package com.example.cecs491project.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button register_btn;
    private Button login_btn;
    private EditText email_txt;
    private EditText password_txt;
    private Button reset_btn;

    private CheckBox remember;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "PrefsFile";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        bindWidget();
        setListeners();
        getPreferencesData();

    }

    private void getPreferencesData(){
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(remember.isChecked()){
                    Boolean checked = remember.isChecked();
                    SharedPreferences.Editor editor =preferences.edit();
                    editor.putString("pref_name",email_txt.getText().toString());
                    editor.putString("pref_pass",password_txt.getText().toString());
                    editor.putBoolean("pref_check", checked);
                    editor.apply();

                }else{
                    preferences.edit().clear().apply();
                }
            }
        });

        SharedPreferences sp =getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u =sp.getString("pref_name","not found");
            email_txt.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p = sp.getString("pref_pass","not found");
            password_txt.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check", false);
            remember.setChecked(b);
        }
    }

    private void bindWidget(){
        register_btn = findViewById(R.id.register);
        login_btn = findViewById(R.id.login);
        email_txt = findViewById(R.id.email);
        password_txt = findViewById(R.id.password);
        reset_btn = findViewById(R.id.forgot_password);
        remember = findViewById(R.id.checkBox);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.icon_logo);
        auth = FirebaseAuth.getInstance();
    }

    //Hide input keypad after focus changed
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    //set all Listeners
    private void setListeners(){
        OnClick onClick = new OnClick();
        register_btn.setOnClickListener(onClick);
        login_btn.setOnClickListener(onClick);
        reset_btn.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.register:
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                    finish();
                    break;
                case R.id.login:
                    String email_str = email_txt.getText().toString();
                    String password_str = password_txt.getText().toString();
                    loginUser(email_str, password_str);
                    break;
                case R.id.forgot_password:
                    startActivity(new Intent(LoginActivity.this, resetPasswordActivity.class));
                    finish();
                    break;
            }
        }
    }

    //login in user
    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,"Email or Password is Incorrect!", Toast.LENGTH_LONG).show());
    }

    //Make sure the user will stay logged in
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }
}
