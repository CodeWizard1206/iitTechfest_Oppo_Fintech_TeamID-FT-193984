package com.codewizard.vaffi;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import static android.view.View.*;

public class signUpActivity extends AppCompatActivity {
    Dialog doneRegister;
    TextInputEditText fname;
    TextInputEditText email;
    TextInputEditText contact;
    TextInputEditText pass;
    TextInputEditText passRe;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("registeredUser");
    TextToSpeech tts;
    int result;

    String name;
    String emailTxt;
    String contactTxt;
    String passTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        doneRegister = new Dialog(this);


        final TextInputLayout fnameLayer = findViewById(R.id.fnameLayout);
        final TextInputLayout emailLayer = findViewById(R.id.emailLayout);
        final TextInputLayout contactLayer = findViewById(R.id.contactLayout);
        final TextInputLayout passLayer = findViewById(R.id.passLayout);
        final TextInputLayout passReLayer = findViewById(R.id.passReLayout);

        fname = findViewById(R.id.fnameInpuText);
        email = findViewById(R.id.emailInpuText);
        contact = findViewById(R.id.contactInputText);
        pass = findViewById(R.id.passInputText);
        passRe = findViewById(R.id.passReInputText);

        FloatingActionButton fSubmit = findViewById(R.id.fab);
        FloatingActionButton fName = findViewById(R.id.nameIcon);
        FloatingActionButton fEmail = findViewById(R.id.emailIcon);
        FloatingActionButton fCon = findViewById(R.id.contactIcon);
        FloatingActionButton fPass = findViewById(R.id.passIcon);
        FloatingActionButton fPassRe = findViewById(R.id.passReIcon);
        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(signUpActivity.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recAction(fName,0);
        recAction(fEmail,1);
        recAction(fCon,2);
        recAction(fPass,3);
        recAction(fPassRe,4);

        fSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (fname.getText().toString().isEmpty()) {
                    fnameLayer.setError("This field is mandatory");
                    isValid = false;
                } else {
                    fnameLayer.setErrorEnabled(false);
                    name = fname.getText().toString();
                }
                if (email.getText().toString().isEmpty() || !email.getText().toString().contains("@") || (!email.getText().toString().endsWith(".com") && !email.getText().toString().endsWith(".in"))) {
                    emailLayer.setError("Enter a valid email address");
                    isValid = false;
                } else {
                    emailLayer.setErrorEnabled(false);
                    emailTxt = email.getText().toString();
                }
                if (contact.getText().toString().isEmpty() || contact.getText().toString().length() > 10 || contact.getText().toString().length() < 10 ) {
                    contactLayer.setError("Enter a valid contact number");
                    isValid = false;
                } else {
                    contactLayer.setErrorEnabled(false);
                    contactTxt = contact.getText().toString();
                }
                if (pass.getText().toString().length() < 8 || pass.getText().toString().length() > 30) {
                    passLayer.setError("Password must be 8-30 characters long.");
                    isValid = false;
                } else {
                    passLayer.setErrorEnabled(false);
                }
                if (!passRe.getText().toString().equals(pass.getText().toString()) || passRe.getText().toString().isEmpty()) {
                    passReLayer.setError("Password Doesn't Match");
                    isValid = false;
                } else {
                    passReLayer.setErrorEnabled(false);
                    passTxt = pass.getText().toString();
                }
                if (isValid) {
                    final String id = dbRef.push().getKey();
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int mailErr = 0, numErr = 0;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getValue(newUser.class).getUserEmail().equals(emailTxt)) {
                                    mailErr = 1;
                                }
                                if (data.getValue(newUser.class).getUserContact().equals(contactTxt)) {
                                    numErr = 1;
                                }
                            }
                            if (mailErr == 0 && numErr == 0) {
                                newUser newU = new newUser(id, name, emailTxt, contactTxt, passTxt);
                                dbRef.child(id).setValue(newU);
                                showDone();
                                tts.speak("Great!", TextToSpeech.QUEUE_FLUSH, null);
                            } else {
                                if (mailErr == 1) {
                                    emailLayer.setError("Email Already Exist!");
                                } else {
                                    emailLayer.setErrorEnabled(false);
                                }
                                if (numErr == 1) {
                                    contactLayer.setError("Contact Already Exist!");
                                } else {
                                    contactLayer.setErrorEnabled(false);
                                }
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(signUpActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                                } else {
                                    tts.speak("it seems a user has already been registered with the same data, do the correction and try again", TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(signUpActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                    } else {
                        tts.speak("it seems you may have entered a invalid or no data, do the corrections and try again", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    void recAction(FloatingActionButton fab, final int buttonCode) {
        fab.setOnClickListener(new OnClickListener() {
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
                    fname.setText(resultAd.get(0));
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    email.setText(resultAd.get(0).replaceAll("\\s+","").toLowerCase());
                }
                break;
            case 2:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    contact.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 3:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    pass.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 4:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    passRe.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    public void showDone() {
        doneRegister.setContentView(R.layout.registration_pop_up);
        doneRegister.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = doneRegister.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        doneRegister.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        doneRegister.show();
        MaterialButton done = doneRegister.findViewById(R.id.popDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneRegister.dismiss();
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

