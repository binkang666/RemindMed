package com.example.cecs491project.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button register;
    private Button login;
    private EditText email;
    private EditText password;
    private Button reset;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        reset = findViewById(R.id.forgot_password);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            finish();
        });

        login.setOnClickListener(view -> {
            String email_str = email.getText().toString();
            String password_str = password.getText().toString();
            loginUser(email_str, password_str);
        });

        reset.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, resetPasswordActivity.class));
            finish();
        });

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