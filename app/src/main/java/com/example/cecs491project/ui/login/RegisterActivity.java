package com.example.cecs491project.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cecs491project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})";
    Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private EditText email;
    private EditText password;
    private Button register;
    private EditText password_confirm;
    private EditText userName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        password_confirm = findViewById(R.id.password_confirm);
        userName = findViewById(R.id.user_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //connect to firebase authorization
        auth = FirebaseAuth.getInstance();

        //make hyperlink clickable and direct to web browser
        TextView website = (TextView) findViewById(R.id. note_2);
        website.setMovementMethod(LinkMovementMethod.getInstance());

        register.setOnClickListener(view -> {
            if(validateEmail() && validatePassword()){
                String email_str = email.getText().toString();
                String password_str = password.getText().toString();
                registerUser(email_str,password_str);
            }
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
        String confirm_str = password_confirm.getText().toString();

        if (TextUtils.isEmpty(password_str) || TextUtils.isEmpty(confirm_str)) {
            password.setError("Field can't be empty");
            return false;
        }
        if (!pattern.matcher(password_str).matches() || !pattern.matcher(confirm_str).matches() ) {
            password.setError("Password too weak");
            return false;
        }
        if(!password_str.equals(confirm_str)){
            password.setError("Password does not match");
            password_confirm.setError("Password does not match");
            return false;
        }
        password.setError(null);
        return true;

    }

    private void registerUser(String email1, String password){
        auth.createUserWithEmailAndPassword(email1,password).addOnCompleteListener(RegisterActivity.this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Map<String, Object> userMap = new HashMap<>();
                String name = userName.getText().toString();
                String email_str = email.getText().toString();
                userMap.put("userName",name);
                userMap.put("userEmailId",email_str);
                DocumentReference users = null;
                users = db.collection("User Database")
                        .document(userID).collection("User Information").document("Personal Information");
                User user = new User(name, email_str, null);
                users.set(user);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
            else{
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
