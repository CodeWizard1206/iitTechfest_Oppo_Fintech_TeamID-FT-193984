package com.codewizard.vaffi;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Locale;

public class splashActivity extends AppCompatActivity {

    RelativeLayout relLay2, relLay3;
    AppCompatImageView appLogo;
    Handler handler = new Handler();
    Dialog quiter;
    SharedPreferences sharedPreferences;
    boolean code;
    TextToSpeech tts;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        quiter = new Dialog(this);

        ((ViewGroup) findViewById(R.id.root)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        appLogo = findViewById(R.id.app_logo);
        relLay2 = findViewById(R.id.relLay2);
        relLay3 = findViewById(R.id.relLay3);

        sharedPreferences = getSharedPreferences("walkThrough",MODE_PRIVATE);
        code = sharedPreferences.getBoolean("firstTime", true);
        if (code) {
            handler.postDelayed(oneStartRun, 1500);
            handler.postDelayed(regularRun, 2500);
        } else {
            handler.postDelayed(regularRun, 1500);
            handler.postDelayed(speechRun, 2500);
        }

        FloatingActionButton signUp = findViewById(R.id.signUpBtn);
        FloatingActionButton logIn = findViewById(R.id.logInBtn);
        FloatingActionButton loanBtn = findViewById(R.id.loanBtn);
        FloatingActionButton admitBtn = findViewById(R.id.admitBtn);

        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(splashActivity.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splashActivity.this, signUpActivity.class);
                startActivity(intent);
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splashActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
        loanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splashActivity.this, loanActivity.class);
                startActivity(intent);
            }
        });
        admitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splashActivity.this, admissionActivity.class);
                startActivity(intent);
            }
        });
    }

    Runnable oneStartRun = new Runnable() {
        @Override
        public void run() {
                Intent intent = new Intent(splashActivity.this, walkthroughActivity.class);
                startActivity(intent);
        }
    };
    Runnable regularRun = new Runnable() {
        @Override
        public void run() {
            relLay2.setVisibility(View.VISIBLE);
            appLogo.getLayoutParams().height = dpToPx(splashActivity.this, 75);
            appLogo.getLayoutParams().width = dpToPx(splashActivity.this, 81);
            appLogo.setScaleType(AppCompatImageView.ScaleType.FIT_XY);
            relLay3.setVisibility(View.VISIBLE);
        }
    };
    Runnable speechRun = new Runnable() {
        @Override
        public void run() {
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(splashActivity.this, "TextToSpeech Err : Language Not Supported", Toast.LENGTH_SHORT).show();
            } else {
                tts.speak("Welcome!, Select a form to continue.", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    };

    @Override
    public void onBackPressed() {
        quitPopUp();
    }

    public void quitPopUp() {
        quiter.setContentView(R.layout.exit_pop);
        quiter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = quiter.getWindow();
        window.setGravity(Gravity.CENTER);
        quiter.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        quiter.show();
        MaterialButton quitYes = quiter.findViewById(R.id.quitYes);
        MaterialButton quitNo = quiter.findViewById(R.id.quitNo);
        quitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiter.dismiss();
                finish();
            }
        });
        quitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiter.dismiss();
            }
        });
    }

    public int dpToPx(Context c,int dp) {
        float density = c.getResources().getDisplayMetrics().density;
        return  Math.round((float)dp*density);
    }
}