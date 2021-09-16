package com.example.cecs491project.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_btn = findViewById(R.id.register);
        login_btn = findViewById(R.id.login);
        email_txt = findViewById(R.id.email);
        password_txt = findViewById(R.id.password);
        reset_btn = findViewById(R.id.forgot_password);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.icon_logo);
        auth = FirebaseAuth.getInstance();

        setListeners();

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
