package com.codewizard.vaffi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Locale;

public class loginActivity extends AppCompatActivity {
    Dialog doneLogged;
    TextInputEditText email;
    TextInputEditText pass;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("registeredUser");
    TextToSpeech tts;
    int result;

    String logID;
    String logPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        doneLogged = new Dialog(this);

        final TextInputLayout emailLayer = findViewById(R.id.emailLayout);
        final TextInputLayout passLayer = findViewById(R.id.passLayout);

        email = findViewById(R.id.emailInpuText);
        pass = findViewById(R.id.passInputText);

        FloatingActionButton flog = findViewById(R.id.flogin);
        FloatingActionButton fEmail = findViewById(R.id.nameIcon);
        FloatingActionButton fPass = findViewById(R.id.passIcon);
        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(loginActivity.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recAction(fEmail,0);
        recAction(fPass,1);

        flog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (email.getText().toString().isEmpty() ) {
                    emailLayer.setError("Enter a Valid Email/Contact!");
                    isValid = false;
                } else {
                    emailLayer.setErrorEnabled(false);
                    logID = email.getText().toString();
                }
                if (pass.getText().toString().length() < 8 || pass.getText().toString().length() > 30) {
                    passLayer.setError("Password Entered is Incorrect!");
                    isValid = false;
                } else {
                    passLayer.setErrorEnabled(false);
                    logPass = pass.getText().toString();
                }
                if (isValid) {
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int idErr = 0, passErr = 0, logStatus = 0;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if(logID.contains("@")) {
                                    if (!data.getValue(newUser.class).getUserEmail().equals(logID)) {
                                        idErr = 1;
                                    } else {
                                        idErr = 0;
                                    }
                                    if (!data.getValue(newUser.class).getPass().equals(logPass)) {
                                        passErr = 1;
                                    } else {
                                        passErr = 0;
                                    }
                                } else {
                                    if (!data.getValue(newUser.class).getUserContact().equals(logID)) {
                                        idErr = 1;
                                    } else {
                                        idErr = 0;
                                    }
                                    if (!data.getValue(newUser.class).getPass().equals(logPass)) {
                                        passErr = 1;
                                    } else {
                                        passErr = 0;
                                    }
                                }
                                if (idErr == 0 && passErr == 0) {
                                    logStatus = 1;
                                    break;
                                }
                            }
                            if (logStatus == 1) {
                                showPop();
                                tts.speak("Great!", TextToSpeech.QUEUE_FLUSH, null);
                            }
                            else {
                                if (idErr == 1) {
                                    emailLayer.setError("Invalid Login Credentials!");
                                } else {
                                    emailLayer.setErrorEnabled(false);
                                }
                                if (passErr == 1) {
                                    passLayer.setError("Invalid Login Credentials!");
                                } else {
                                    passLayer.setErrorEnabled(false);
                                }
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(loginActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                                } else {
                                    tts.speak("Invalid Login Credentials Found, make sure that you have signed up before login and try again", TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(loginActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                    } else {
                        tts.speak("it seems you may have entered a invalid data, do the corrections and try again", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    void recAction(FloatingActionButton flog, final int buttonCode) {
        flog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recAudio(buttonCode);
            }
        });
    }

    private void recAudio (int REQ_CODE_SPEECH) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Recognizing Speech");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH);
        } catch (ActivityNotFoundException a) {
            a.getStackTrace();
            a.getMessage();
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    email.setText(resultAd.get(0).replaceAll("\\s+","").toLowerCase());
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    pass.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    public void showPop() {
        doneLogged.setContentView(R.layout.logged_in_pop);
        doneLogged.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = doneLogged.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        doneLogged.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        doneLogged.show();
        MaterialButton done = doneLogged.findViewById(R.id.popDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneLogged.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}