package com.codewizard.vaffi;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class walkthroughActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    FloatingActionButton btnNxt;
    SharedPreferences sharedPreferences;
    Handler handler = new Handler();
    boolean firstTime;
    TextToSpeech tts;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        sharedPreferences = getSharedPreferences("walkThrough",MODE_PRIVATE);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.select, "1. Select Form", "First you need to select a form which you want to fill, you can select any form from the available forms."));
        models.add(new Model(R.drawable.input_data, "2. Input Data", "After selecting the form, just press the column button on the left side of each column to input data on that column."));
        models.add(new Model(R.drawable.validate, "3. Validate Data", "After filling all data ensure that all data filled is correct, if data filled is incorrect then again input data."));
        models.add(new Model(R.drawable.confirm_sub, "4. Confirm Submission", "After ensuring that data filled is correct then, press the submit button to make final submission."));
        models.add(new Model(R.drawable.done, "5. Done", "If the form data get submitted successfully then you will get the acknowledged and the work gets completed."));
        models.add(new Model(R.drawable.note, "NOTE !", "Make sure that your device is connected with an active network connection before continuing with form filling."));

        btnNxt = findViewById(R.id.btnNxt);
        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);
        viewPager.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(walkthroughActivity.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int position) {
                if (position == models.size() - 1) {
                    btnNxt.setVisibility(View.VISIBLE);
                } else {
                    btnNxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                firstTime = false;
                editor.putBoolean("firstTime",firstTime);
                editor.apply();
                handler.postDelayed(speechRun, 3500);
                finish();
            }
        });
    }

    Runnable speechRun = new Runnable() {
        @Override
        public void run() {
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(walkthroughActivity.this, "TextToSpeech Err : Language Not Supported", Toast.LENGTH_SHORT).show();
            } else {
                tts.speak("Welcome!, Select a form to continue.", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    };
}
