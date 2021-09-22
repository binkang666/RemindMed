package com.example.cecs491project.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.window.SplashScreen;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoadingActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    LottieAnimationView lottieAnimationbubble;
    TextView loadingName;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.animate().setDuration(1500).setStartDelay(4000);
        lottieAnimationbubble = findViewById(R.id.lottie_bubble);
        lottieAnimationbubble.animate().setDuration(1500).setStartDelay(4000);

        Animation topAnim, botAnim;
        topAnim= AnimationUtils.loadAnimation(this, R.anim.top_slide_in);
        botAnim=AnimationUtils.loadAnimation(this, R.anim.bot_slide_in);
        slogan = findViewById(R.id.slogan);
        loadingName = findViewById(R.id.loading_name);
        slogan.setAnimation(botAnim);
        loadingName.setAnimation(topAnim);

        new Handler().postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user !=null){
                startActivity(new Intent(LoadingActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            else{
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            finish();
        }, 4000);
    }
}