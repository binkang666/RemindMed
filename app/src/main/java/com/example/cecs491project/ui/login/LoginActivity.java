package com.example.cecs491project.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button register_btn;
    private Button login_btn;
    private EditText email_txt;
    private EditText password_txt;
    private Button reset_btn;
    private Button Skip;

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
                    email_txt.getText().clear();
                    password_txt.getText().clear();
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
        Skip = findViewById(R.id.skip);

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
        Skip.setOnClickListener(onClick);

    }


    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();
                if(id == R.id.register) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
                if(id == R.id.login) {
                    String email_str = email_txt.getText().toString();
                    String password_str = password_txt.getText().toString();
                    loginUser(email_str, password_str);
                }
                if(id == R.id.forgot_password) {
                    startActivity(new Intent(LoginActivity.this, resetPasswordActivity.class));
                    finish();
                }
                if(id == R.id.skip){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setMessage("Sign in anonymously? \n\n(Beware that if you don't link your account " +
                                    "before signing out, you will lose all data.)");

                    alertDialog.setCancelable(false);

                    alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

                        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Signed in Anonymously", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("account", "anonymous account");
                            }
                        });

                    });
                    alertDialog.setNegativeButton("No", (dialogInterface, i) -> {

                    });
                    alertDialog.create().show();

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
