package com.example.cecs491project.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cecs491project.R;
import com.google.firebase.auth.FirebaseAuth;

public class resetPasswordActivity extends AppCompatActivity {

    private EditText email;

    private Button reset;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = findViewById(R.id.email);
        reset = findViewById(R.id.send_request);
        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(view -> {
            String email_str = email.getText().toString();
            if(TextUtils.isEmpty(email_str)){
                email.setError("Email cannot be empty");
            }

            auth.sendPasswordResetEmail(email_str).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(resetPasswordActivity.this, "Reset Email Sent", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(resetPasswordActivity.this, "Reset Email Failed to Sent", Toast.LENGTH_LONG).show());

        });

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

    //go Back to previous activity
    public void goBack(View view) {
       startActivity(new Intent(resetPasswordActivity.this, LoginActivity.class));
    }
}
