package com.example.cecs491project.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})";
    Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private EditText email;
    private EditText password;
    private Button register;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
                if(validateEmail() && validatePassword()){
                    String email_str = email.getText().toString();
                    String password_str = password.getText().toString();
                    registerUser(email_str,password_str);
                }
            });
    }

    private boolean validateEmail(){
        String email_str = email.getText().toString();
        if(TextUtils.isEmpty(email_str)){
            email.setError("Email cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password_str = password.getText().toString();

        if (TextUtils.isEmpty(password_str)) {
            password.setError("Field can't be empty");
            return false;
        }
        if (!pattern.matcher(password_str).matches()) {
            password.setError("Password too weak");
            return false;
        }
        password.setError(null);
        return true;

    }

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
