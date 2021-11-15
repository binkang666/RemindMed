package com.example.cecs491project.ui.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LinkAccountActivity extends AppCompatActivity {

    final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})";
    Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private EditText email;
    private EditText password;
    private Button link;

    private FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_account);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        link = findViewById(R.id.Link);
        //connect to firebase authorization
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        link.setOnClickListener(view -> {
            if(validateEmail() && validatePassword()){
                String email_str = email.getText().toString();
                String password_str = password.getText().toString();
                linkAccount(email_str,password_str);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Link Account");

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

    public void linkAccount(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        Objects.requireNonNull(user).linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LinkAccountActivity.this, "Link Successfully", Toast.LENGTH_SHORT).show();
                    try {
                        FirebaseFirestore.getInstance().collection("Anonymous User Database")
                                .document(user.getUid()).collection("Medication")
                                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            WriteBatch batch = FirebaseFirestore.getInstance().batch();
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                batch.delete(snapshot.getReference());
                            }
                            batch.commit().addOnSuccessListener(unused -> Log.d("TAG", "Success")).addOnFailureListener(e -> Log.d("TAG", "Failed", e));
                        });

                        FirebaseFirestore.getInstance().collection("Anonymous User Database")
                                .document(user.getUid()).collection("Reminder")
                                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            WriteBatch batch = FirebaseFirestore.getInstance().batch();
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                batch.delete(snapshot.getReference());
                            }
                            batch.commit().addOnSuccessListener(unused -> Log.d("TAG", "Success")).addOnFailureListener(e -> Log.d("TAG", "Failed", e));
                        });

                        FirebaseFirestore.getInstance()
                                .collection("Anonymous User Database")
                                .document(user.getUid())
                                .delete();
                        progressBar.setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    startActivity(new Intent(LinkAccountActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        return true;
    }


}