package com.codewizard.vaffi;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class loanActivity extends AppCompatActivity {

    Dialog doneLoan;
    int result;

    TextInputEditText name;
    TextInputEditText mobile;
    TextInputEditText email;
    TextInputEditText street;
    TextInputEditText area;
    TextInputEditText landmark;
    TextInputEditText state;
    TextInputEditText city;
    TextInputEditText pincode;
    TextInputEditText bankname;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextInputEditText bankaccount;
    TextInputEditText aadhar;
    TextInputEditText pancard;
    TextInputEditText loantype;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("loanApplications");
    TextToSpeech tts;

    String appName, appMobile, appEmail, appStreet, appArea, appLandmark, appState, appCity, appPin, appBnk, accType, appAccNo, appAadhar, appPan, appLoanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        doneLoan = new Dialog(loanActivity.this);

        final TextView radErr = findViewById(R.id.radError);

        final TextInputLayout namelayer = findViewById(R.id.namelayout);
        final TextInputLayout mobilelayer = findViewById(R.id.mobilenolayout);
        final TextInputLayout emaillayer = findViewById(R.id.emaillayout);
        final TextInputLayout streetlayer = findViewById(R.id.streetlayout);
        final TextInputLayout arealayer = findViewById(R.id.arealayout);
        final TextInputLayout landmarklayer = findViewById(R.id.landmarklayout);
        final TextInputLayout statelayer = findViewById(R.id.statelayout);
        final TextInputLayout citylayer = findViewById(R.id.citylayout);
        final TextInputLayout pincodelayer = findViewById(R.id.pincodelayout);
        final TextInputLayout banknamelayer = findViewById(R.id.banknamelayout);
        final TextInputLayout bankaccountlayer = findViewById(R.id.bankaccountnolayout);
        final TextInputLayout aadharlayer = findViewById(R.id.aadharcardlayout);
        final TextInputLayout pancardlayer = findViewById(R.id.pancardlayout);
        final TextInputLayout loantypelayer = findViewById(R.id.loantypelayout);

        name = findViewById(R.id.fnamelayout);
        mobile = findViewById(R.id.imobilenolayout);
        email = findViewById(R.id.iemaillayout);
        street = findViewById(R.id.istreetlayout);
        area = findViewById(R.id.iarealayout);
        landmark = findViewById(R.id.ilandmarklayout);
        state = findViewById(R.id.istatelayout);
        city = findViewById(R.id.icitylayout);
        pincode = findViewById(R.id.ipincodelayout);
        bankname = findViewById(R.id.ibanknamelayout);
        bankaccount = findViewById(R.id.ibankaccountnolayout);
        aadhar = findViewById(R.id.iaadharcardlayout);
        pancard = findViewById(R.id.ipancardlayout);
        loantype = findViewById(R.id.iloantypelayout);
        radioGroup = findViewById(R.id.radiogroup);

        FloatingActionButton submit = findViewById(R.id.fab);
        FloatingActionButton nameBtn = findViewById(R.id.nameIcon);
        FloatingActionButton contactBtn = findViewById(R.id.contactIcon);
        final FloatingActionButton emailBtn = findViewById(R.id.emailIcon);
        FloatingActionButton addrStreet = findViewById(R.id.streetIcon);
        FloatingActionButton addrArea = findViewById(R.id.areaIcon);
        FloatingActionButton addrLand = findViewById(R.id.landmarkIcon);
        FloatingActionButton addrState = findViewById(R.id.stateIcon);
        FloatingActionButton addrCity = findViewById(R.id.cityIcon);
        FloatingActionButton addrPin = findViewById(R.id.pinIcon);
        final FloatingActionButton bnkName = findViewById(R.id.bankNameIcon);
        FloatingActionButton accNo = findViewById(R.id.accountIcon);
        FloatingActionButton idBtn = findViewById(R.id.aadharIcon);
        FloatingActionButton panBtn = findViewById(R.id.panIcon);
        FloatingActionButton lTypeBtn = findViewById(R.id.lTypeIcon);

        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = tts.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(loanActivity.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recAction(nameBtn, 0);
        recAction(contactBtn, 1);
        recAction(emailBtn, 2);
        recAction(addrStreet, 3);
        recAction(addrArea, 4);
        recAction(addrLand, 5);
        recAction(addrState, 6);
        recAction(addrCity, 7);
        recAction(addrPin, 8);
        recAction(bnkName, 9);
        recAction(accNo, 10);
        recAction(idBtn, 11);
        recAction(panBtn, 12);
        recAction(lTypeBtn, 13);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                boolean isvalid = true;
                if (selectedId == -1) {
                    radErr.setVisibility(View.VISIBLE);
                    isvalid = false;
                } else {
                    radErr.setVisibility(View.GONE);
                    radioButton = findViewById(selectedId);
                    accType = radioButton.getText().toString();
                }
                if (Objects.requireNonNull(name.getText()).toString().isEmpty()) {
                    namelayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    namelayer.setErrorEnabled(false);
                    appName = name.getText().toString();
                }
                if (Objects.requireNonNull(mobile.getText()).toString().isEmpty() || (mobile.getText().toString().length() > 10) || (mobile.getText().toString().length() < 10)) {
                    mobilelayer.setError("Enter a valid mobile number");
                    isvalid = false;
                } else {
                    mobilelayer.setErrorEnabled(false);
                    appMobile = mobile.getText().toString();
                }
                if (Objects.requireNonNull(email.getText()).toString().isEmpty() || !email.getText().toString().contains("@") || (!email.getText().toString().endsWith(".com") && !email.getText().toString().endsWith(".in"))) {
                    emaillayer.setError("Enter a valid email address");
                    isvalid = false;
                } else {
                    emaillayer.setErrorEnabled(false);
                    appEmail = email.getText().toString();
                }
                if (Objects.requireNonNull(street.getText()).toString().isEmpty()) {
                    streetlayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    streetlayer.setErrorEnabled(false);
                    appStreet = street.getText().toString();
                }
                if (Objects.requireNonNull(area.getText()).toString().isEmpty()) {
                    arealayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    arealayer.setErrorEnabled(false);
                    appArea = area.getText().toString();
                }
                if (Objects.requireNonNull(landmark.getText()).toString().isEmpty()) {
                    landmarklayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    landmarklayer.setErrorEnabled(false);
                    appLandmark = landmark.getText().toString();
                }
                if (Objects.requireNonNull(state.getText()).toString().isEmpty()) {
                    statelayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    statelayer.setErrorEnabled(false);
                    appState = state.getText().toString();
                }
                if (Objects.requireNonNull(city.getText()).toString().isEmpty()) {
                    citylayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    citylayer.setErrorEnabled(false);
                    appCity = city.getText().toString();
                }
                if (Objects.requireNonNull(pincode.getText()).toString().isEmpty() || pincode.getText().toString().length() != 6) {
                    pincodelayer.setError("Enter a valid pin-code");
                    isvalid = false;
                } else {
                    pincodelayer.setErrorEnabled(false);
                    appPin = pincode.getText().toString();
                }
                if (Objects.requireNonNull(bankname.getText()).toString().isEmpty()) {
                    banknamelayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    banknamelayer.setErrorEnabled(false);
                    appBnk = bankname.getText().toString();
                }
                if (Objects.requireNonNull(bankaccount.getText()).toString().isEmpty()) {
                    bankaccountlayer.setError("This field is mandatory");
                    isvalid = false;
                } else {
                    bankaccountlayer.setErrorEnabled(false);
                    appAccNo = bankaccount.getText().toString();
                }
                if (Objects.requireNonNull(aadhar.getText()).toString().isEmpty() || (aadhar.getText().toString().length() != 12)) {
                    aadharlayer.setError("Enter a valid aadhar card number");
                    isvalid = false;
                } else {
                    aadharlayer.setErrorEnabled(false);
                    appAadhar = aadhar.getText().toString();
                }
                if (Objects.requireNonNull(pancard.getText()).toString().isEmpty() || pancard.getText().toString().length() != 10) {
                    pancardlayer.setError("Enter a valid pan card number");
                    isvalid = false;
                } else {
                    pancardlayer.setErrorEnabled(false);
                    appPan = pancard.getText().toString();
                }
                if (Objects.requireNonNull(loantype.getText()).toString().isEmpty()) {
                    loantypelayer.setError("Enter a vaild loan type");
                    isvalid = false;
                } else {
                    loantypelayer.setErrorEnabled(false);
                    appLoanType = loantype.getText().toString();
                }
                if (isvalid) {
                    final String id = dbRef.push().getKey();
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int mailErr = 0, numErr = 0, addrErr = 0, accErr = 0, aadharErr = 0, panErr = 0, reApplyErr = 0;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getValue(loanApplication.class).getApplicantEmail().equals(appEmail)) {
                                    mailErr = 1;
                                }
                                if (data.getValue(loanApplication.class).getApplicantContact().equals(appMobile)) {
                                    numErr = 1;
                                }
                                if (data.getValue(loanApplication.class).getAddrStreet().equals(appStreet) && data.getValue(loanApplication.class).getAddrArea().equals(appArea) && data.getValue(loanApplication.class).getAddrLandmark().equals(appLandmark) && data.getValue(loanApplication.class).getAddrState().equals(appState) && data.getValue(loanApplication.class).getAddrCity().equals(appCity) && data.getValue(loanApplication.class).getAddrPin().equals(appPin)) {
                                    addrErr = 1;
                                }
                                if (data.getValue(loanApplication.class).getAccNo().equals(appAccNo)) {
                                    accErr = 1;
                                }
                                if (data.getValue(loanApplication.class).getAadharNo().equals(appAadhar)) {
                                    aadharErr = 1;
                                }
                                if (data.getValue(loanApplication.class).getPanNo().equals(appPan)) {
                                    panErr = 1;
                                }
                                if (mailErr == 1 && numErr == 1 && addrErr == 1 && accErr == 1 && aadharErr == 1 && panErr == 1) {
                                    if (data.getValue(loanApplication.class).getLoanType().equals(appLoanType)) {
                                        reApplyErr = 1;
                                        break;
                                    }
                                }
                            }
                            if (reApplyErr == 0 && mailErr == 1 && numErr == 1 && addrErr == 1 && accErr == 1 && aadharErr == 1 && panErr == 1) {
                                loanApplication newLoan = new loanApplication(id, appName, appMobile, appEmail, appStreet, appArea, appLandmark, appState, appCity, appPin, appBnk, accType, appAccNo, appAadhar, appPan, appLoanType);
                                dbRef.child(id).setValue(newLoan);
                                showPop();
                                tts.speak("Great!", TextToSpeech.QUEUE_FLUSH, null);
                            } else if (reApplyErr == 0 && addrErr == 0 && accErr == 0 && aadharErr == 0 && panErr == 0) {
                                loanApplication newLoan = new loanApplication(id, appName, appMobile, appEmail, appStreet, appArea, appLandmark, appState, appCity, appPin, appBnk, accType, appAccNo, appAadhar, appPan, appLoanType);
                                dbRef.child(id).setValue(newLoan);
                                showPop();
                                tts.speak("Great!", TextToSpeech.QUEUE_FLUSH, null);
                            } else {
                                if (reApplyErr == 1) {
                                    namelayer.setError("Same Applicant Exist!");
                                    loantypelayer.setError("Same Loan Application Exist!");
                                    emaillayer.setError("Same Applicant Exist!");
                                    mobilelayer.setError("Same Applicant Exist!");
                                } else {
                                    namelayer.setErrorEnabled(false);
                                    loantypelayer.setErrorEnabled(false);
                                    emaillayer.setErrorEnabled(false);
                                    mobilelayer.setErrorEnabled(false);
                                }
                                if (addrErr == 1) {
                                    streetlayer.setError("Same Address Application Exist!");
                                    arealayer.setError("Same Address Application Exist!");
                                    landmarklayer.setError("Same Address Application Exist!");
                                    statelayer.setError("Same Address Application Exist!");
                                    citylayer.setError("Same Address Application Exist!");
                                    pincodelayer.setError("Same Address Application Exist!");
                                } else {
                                    streetlayer.setErrorEnabled(false);
                                    arealayer.setErrorEnabled(false);
                                    landmarklayer.setErrorEnabled(false);
                                    statelayer.setErrorEnabled(false);
                                    citylayer.setErrorEnabled(false);
                                    pincodelayer.setErrorEnabled(false);
                                }
                                if (accErr == 1) {
                                    bankaccountlayer.setError("Same Account Application Exist!");
                                } else {
                                    bankaccountlayer.setErrorEnabled(false);
                                }
                                if (aadharErr == 1) {
                                    aadharlayer.setError("Same Aadhar ID Application Exist!");
                                } else {
                                    aadharlayer.setErrorEnabled(false);
                                }
                                if (panErr == 1) {
                                    pancardlayer.setError("Same PAN ID Application Exist!");
                                } else {
                                    pancardlayer.setErrorEnabled(false);
                                }
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(loanActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(loanActivity.this,"TextToSpeech Err : Language Not Supported",Toast.LENGTH_SHORT).show();
                    } else {
                        tts.speak("it seems you may have some invalid or incorrect data in your form, do the corrections and try again", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
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
                    street.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 4:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    area.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 5:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    landmark.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 6:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    state.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 7:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    city.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 8:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    pincode.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 9:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    bankname.setText(resultAd.get(0).toUpperCase());
                }
                break;
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    bankaccount.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    aadhar.setText(resultAd.get(0).replaceAll("\\s+",""));
                }
                break;
            case 12:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    pancard.setText(resultAd.get(0).replaceAll("\\s+","").toUpperCase());
                }
                break;
            case 13:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultAd = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    loantype.setText(resultAd.get(0).toUpperCase());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    public void showPop() {
        doneLoan.setContentView(R.layout.loan_pop_up);
        doneLoan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = doneLoan.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        doneLoan.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        doneLoan.show();
        MaterialButton done = doneLoan.findViewById(R.id.popDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneLoan.dismiss();
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