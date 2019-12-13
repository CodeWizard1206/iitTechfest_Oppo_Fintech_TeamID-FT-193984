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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.Objects;

public class admissionActivity extends AppCompatActivity {

    Dialog doneAdm;
    TextInputEditText name;
    TextInputEditText mobile;
    TextInputEditText email;
    TextInputEditText address;
    TextInputEditText state;
    TextInputEditText city;
    TextInputEditText pincode;
    TextInputEditText category;
    TextInputEditText aadharcard;
    TextInputEditText collegename;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String appName, appGender, appMobile, appEmail, appAddr, appState, appCity, appPin, appCategory, appAadhar, instName;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("newAddmissions");
    TextToSpeech tts;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);

        doneAdm = new Dialog(this);
        final TextView radErr = findViewById(R.id.radError);

        final TextInputLayout namelayer = findViewById(R.id.namelayout);
        final TextInputLayout mobilelayer = findViewById(R.id.mobilenolayout);
        final TextInputLayout emaillayer = findViewById(R.id.emaillayout);
        final TextInputLayout addresslayer = findViewById(R.id.addresslayout);
        final TextInputLayout statelayer = findViewById(R.id.statelayout);
        final TextInputLayout citylayer = findViewById(R.id.citylayout);
        final TextInputLayout pincodelayer = findViewById(R.id.pincodelayout);
        final TextInputLayout categorylayer = findViewById(R.id.categorylayout);
        final TextInputLayout aadharlayer = findViewById(R.id.aadharcardlayout);
        final TextInputLayout collegenamelayer = findViewById(R.id.collegenamelayout);

        name = findViewById(R.id.fnamelayout);
        mobile = findViewById(R.id.imobilenolayout);
        email = findViewById(R.id.iemaillayout);
        address = findViewById(R.id.iaddresslayout);
        state = findViewById(R.id.istatelayout);
        city = findViewById(R.id.icitylayout);
        pincode = findViewById(R.id.ipincodelayout);
        category = findViewById(R.id.icatogerylayout);
        aadharcard = findViewById(R.id.iaadharcardlayout);
        collegename = findViewById(R.id.icollegenamelayout);

        radioGroup = findViewById(R.id.radiogroup);

        FloatingActionButton submit = findViewById(R.id.fab);
        FloatingActionButton nameBtn = findViewById(R.id.nameIcon);
        FloatingActionButton contactBtn = findViewById(R.id.contactIcon);
        FloatingActionButton emailBtn = findViewById(R.id.emailIcon);
        FloatingActionButton addr = findViewById(R.id.addressIcon);
        FloatingActionButton addrState = findViewById(R.id.stateIcon);
        FloatingActionButton addrCity = findViewById(R.id.cityIcon);
        FloatingActionButton addrPin = findViewById(R.id.pinIcon);
        FloatingActionButton catBtn = findViewById(R.id.catIcon);
        FloatingActionButton idBtn = findViewById(R.id.aadharIcon);
        FloatingActionButton instBtn = findViewById(R.id.instIcon);
        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(admissionActivity.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recAction(nameBtn, 0);
        recAction(contactBtn, 1);
        recAction(emailBtn, 2);
        recAction(addr, 3);
        recAction(addrState, 4);
        recAction(addrCity, 5);
        recAction(addrPin, 6);
        recAction(catBtn, 7);
        recAction(idBtn, 8);
        recAction(instBtn, 9);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                boolean isvalid = true;
                if (selectedId == -1){
                    radErr.setVisibility(View.VISIBLE);
                    isvalid = false;
                } else {
                    radErr.setVisibility(View.GONE);
                    radioButton = findViewById(selectedId);
                    appGender = radioButton.getText().toString();
                }
                if (Objects.requireNonNull(name.getText()).toString().isEmpty()){
                    namelayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    namelayer.setErrorEnabled(false);
                    appName = name.getText().toString();
                }
                if (Objects.requireNonNull(mobile.getText()).toString().isEmpty() || (mobile.getText().toString().length() > 10) || (mobile.getText().toString().length() < 10)){
                    mobilelayer.setError("Enter a valid mobile number");
                    isvalid = false;
                } else {
                    mobilelayer.setErrorEnabled(false);
                    appMobile = mobile.getText().toString();
                }
                if (email.getText().toString().isEmpty() || !email.getText().toString().contains("@") || (!email.getText().toString().endsWith(".com") && !email.getText().toString().endsWith(".in"))){
                    emaillayer.setError("Enter a valid email address");
                    isvalid = false;
                } else {
                    emaillayer.setErrorEnabled(false);
                    appEmail = email.getText().toString();
                }
                if (Objects.requireNonNull(address.getText()).toString().isEmpty()){
                    addresslayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    addresslayer.setErrorEnabled(false);
                    appAddr = address.getText().toString();
                }
                if (Objects.requireNonNull(state.getText()).toString().isEmpty()){
                    statelayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    statelayer.setErrorEnabled(false);
                    appState = state.getText().toString();
                }
                if (Objects.requireNonNull(city.getText()).toString().isEmpty()){
                    citylayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    citylayer.setErrorEnabled(false);
                    appCity = city.getText().toString();
                }
                if (Objects.requireNonNull(pincode.getText()).toString().isEmpty() || pincode.getText().toString().length() != 6 ){
                    pincodelayer.setError("Enter a valid pin-code");
                    isvalid = false;
                } else {
                    pincodelayer.setErrorEnabled(false);
                    appPin = pincode.getText().toString();
                }
                if (Objects.requireNonNull(category.getText()).toString().isEmpty()){
                    categorylayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    categorylayer.setErrorEnabled(false);
                    appCategory = category.getText().toString();
                }
                if (Objects.requireNonNull(aadharcard.getText()).toString().isEmpty() || (aadharcard.getText().toString().length() != 12)){
                    aadharlayer.setError("Enter a valid aadhar card number");
                    isvalid = false;
                }else {
                    aadharlayer.setErrorEnabled(false);
                    appAadhar = aadharcard.getText().toString();
                }
                if (Objects.requireNonNull(collegename.getText()).toString().isEmpty()){
                    collegenamelayer.setError("This field is mandatory");
                    isvalid =false;
                } else {
                    collegenamelayer.setErrorEnabled(false);
                    instName = collegename.getText().toString();
                }
                if (isvalid){
                    final String id = dbRef.push().getKey();
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int mailErr = 0, numErr = 0, addrErr = 0, nameErr = 0, aadharErr = 0, reApplyErr = 0;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getValue(newAdmissions.class).getApplicantName().equals(appName)) {
                                    nameErr = 1;
                                }
                                if (data.getValue(newAdmissions.class).getApplicantEmail().equals(appEmail)) {
                                    mailErr = 1;
                                }
                                if (data.getValue(newAdmissions.class).getApplicantContact().equals(appMobile)) {
                                    numErr = 1;
                                }
                                if (data.getValue(newAdmissions.class).getAddr().equals(appAddr) && data.getValue(newAdmissions.class).getAddrState().equals(appState) && data.getValue(newAdmissions.class).getAddrCity().equals(appCity) && data.getValue(newAdmissions.class).getAddrPin().equals(appPin)) {
                                    addrErr = 1;
                                }
                                if (data.getValue(newAdmissions.class).getAadharNo().equals(appAadhar)) {
                                    aadharErr = 1;
                                }
                                if (mailErr == 1 && numErr == 1 && addrErr == 1 && nameErr == 1 && aadharErr == 1) {
                                    if (data.getValue(newAdmissions.class).getInstName().equals(instName)) {
                                        reApplyErr = 1;
                                        break;
                                    }
                                }
                            }
                            if (reApplyErr == 0 && addrErr == 0 && aadharErr == 0 ) {
                                newAdmissions newAd = new newAdmissions(id, appName, appGender, appMobile, appEmail, appAddr, appState, appCity, appPin, appCategory, appAadhar, instName);
                                dbRef.child(id).setValue(newAd);
                                showPop();
                                tts.speak("Great!", TextToSpeech.QUEUE_FLUSH, null);
                            } else if (reApplyErr == 0 && addrErr == 1 && aadharErr == 0 ) {
                                newAdmissions newAd = new newAdmissions(id, appName, appGender, appMobile, appEmail, appAddr, appState, appCity, appPin, appCategory, appAadhar, instName);
                                dbRef.child(id).setValue(newAd);
                                showPop();
                                tts.speak("Great!", TextToSpeech.QUEUE_FLUSH, null);
                            } else {
                                if (reApplyErr == 1) {
                                    namelayer.setError("Same Applicant Exist!");
                                    emaillayer.setError("Same Applicant Exist!");
                                    mobilelayer.setError("Same Applicant Exist!");
                                    collegenamelayer.setError("Same Applicant Exist!");
                                } else {
                                    namelayer.setErrorEnabled(false);
                                    emaillayer.setErrorEnabled(false);
                                    mobilelayer.setErrorEnabled(false);
                                    collegenamelayer.setErrorEnabled(false);
                                }
                                if (addrErr == 1) {
                                    addresslayer.setError("Same Address Application Exist!");
                                    statelayer.setError("Same Address Application Exist!");
                                    citylayer.setError("Same Address Application Exist!");
                                    pincodelayer.setError("Same Address Application Exist!");
                                } else {
                                    addresslayer.setErrorEnabled(false);
                                    statelayer.setErrorEnabled(false);
                                    citylayer.setErrorEnabled(false);
                                    pincodelayer.setErrorEnabled(false);
                                }
                                if (aadharErr == 1) {
                                    aadharlayer.setError("Same Aadhar ID Application Exist!");
                                } else {
                                    aadharlayer.setErrorEnabled(false);
                                }
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(admissionActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                                } else {
                                    if (reApplyErr == 1) {
                                        tts.speak("it seems that a same application has already been applied , make sure your filled data is correct and then try again!", TextToSpeech.QUEUE_FLUSH, null);
                                    } else {
                                        tts.speak("it seems you may have some invalid or incorrect data in your form, do the corrections and try again", TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(admissionActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                    } else {
                        tts.speak("it seems you may have some invalid or incorrect data in your form, do the corrections and try again", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    public void showPop() {
        doneAdm.setContentView(R.layout.admission_pop_up);
        doneAdm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = doneAdm.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        doneAdm.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        doneAdm.show();
        MaterialButton done = doneAdm.findViewById(R.id.popDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneAdm.dismiss();
                finish();
            }
        });
    }

    void recAction(FloatingActionButton fab, final int buttonCode) {
        fab.setOnClickListener(new View.OnClickListener() {
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
                    name.setText(resultAd.get(0));
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mobile.setText(resultAd.get(0).replaceAll("\\s+","").toLowerCase());
                }
                break;
            case 2:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    email.setText(resultAd.get(0).replaceAll("\\s+","").toLowerCase());
                }
                break;
            case 3:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    address.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 4:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    state.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 5:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    city.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 6:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    pincode.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 7:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    category.setText(resultAd.get(0).replaceAll("\\s+","").toUpperCase());
                }
                break;
            case 8:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    aadharcard.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 9:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    collegename.setText(resultAd.get(0).toUpperCase());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
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
