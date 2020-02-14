package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.swiftoffice.swifttrace.R;
import com.swiftoffice.swifttrace.common.AppConstants;
import com.swiftoffice.swifttrace.common.NetworkConnection;
import com.swiftoffice.swifttrace.common.ProgressBarDialog;
import com.swiftoffice.swifttrace.database.MyFirebaseRealTimeDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.swiftoffice.swifttrace.common.AppConstants.COUNTRY_CODE;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnVerify;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;
    private FirebaseAuth mAuth;
    private String verificationId;
    private TextView tvTimerText;
    private String mobile = "";
    private EditText etEnterOtp;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();

        getData();

        initViews();

        //Set toolbar title
        tvToolBarTitle.setText(getResources().getString(R.string.otp));
    }


    //Initializations
    private void initViews() {
        //Declarations
        btnVerify = findViewById(R.id.btnVerify);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        tvTimerText = findViewById(R.id.tvTimerText);
        etEnterOtp = findViewById(R.id.etEnterOtp);

        //Toolbar
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        listenters();
    }


    /**
     * To get the data from the previous screen
     */
    private void getData() {
        Intent intent = getIntent();
        mobile = intent.getStringExtra(AppConstants.MOBILE_NUMBER);
        sendVerificationCode(mobile);
    }

    //Listeners
    private void listenters() {
        btnVerify.setOnClickListener(this);
        tvTimerText.setOnClickListener(this);
    }


    //Redirect To Home Page
    void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    private void sendVerificationCode(String phoneNumber) { //Sends user's phone number to Firebase to get VerifyOtpActivity
        cancelTimer();
        startTimer();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                COUNTRY_CODE + phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);      // OnVerificationStateChangedCallbacks
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {    //Methods to retrieve verificationId from Firebase and for auto submission
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                cancelTimer();
                etEnterOtp.setText(code);

                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            ProgressBarDialog.dismissProgressDialog();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            if (timer != null) {
                timer.cancel();
            }
            tvTimerText.setText(getResources().getText(R.string.resend_otp));
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // Storing the verification id that is sent to the user
            verificationId = s;
        }
    };


    /**
     * To verify the code received via sms
     *
     * @param code
     */
    private void verifyCode(String code) {  //Launches verification check
        if (verificationId != null && !verificationId.isEmpty()) {
            ProgressBarDialog.showProgressBar(this, "");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, getResources().getString(R.string.entered_code_is_not_valid), Toast.LENGTH_LONG).show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    openHomePage();
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //On Click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Button Verify
            case R.id.btnVerify:
                if (NetworkConnection.isConnectedToInternet(this)) {
                    if (checkValidations()) {
                        verifyCode(etEnterOtp.getText().toString().trim());
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                }

                break;

                //To show timer and resend otp
            case R.id.tvTimerText:
                if (NetworkConnection.isConnectedToInternet(this)) {
                    if (tvTimerText.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.resend_otp))) {
                        sendVerificationCode(mobile);
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean checkValidations() {
        boolean flag = true;
        if (etEnterOtp.getText().toString().trim().isEmpty()) {
            etEnterOtp.setError(getResources().getString(R.string.please_enter_your_otp));
            flag = false;
        } else if (etEnterOtp.getText().toString().trim().length() < 6) {
            etEnterOtp.setError(getResources().getString(R.string.please_enter_your_otp));
            flag = false;
        }
        return flag;

    }


    //Back Button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        cancelTimer();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }


    /**
     * To start the timer
     */
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            int second = 60;

            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimerText.setText(getResources().getString(R.string.resend_otp));
                            timer.cancel();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimerText.setText("Sending OTP... " + "(00:" + second-- + ")");
                        }
                    });
                }

            }
        }, 0, 1000);
    }

    /**
     * To cancel the timer
     */
    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * On back pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelTimer();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
