package com.swiftoffice.swifttrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    //Variables
    private Button buttonVerify;
    private Button buttonResendCode;
    private EditText editText;
    private String verificationId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();

        final String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode("+65" + phonenumber);

        //Declarations
        buttonVerify = findViewById(R.id.verifyButton);
        buttonResendCode = findViewById(R.id.resendcodeButton);
        editText = findViewById(R.id.otpInput);

        //Listeners
        buttonVerify.setOnClickListener(new View.OnClickListener() {    //
            @Override
            public void onClick(View view) {

                if(editText.getText().toString().trim().isEmpty()) {
                    editText.setError("Please enter the OTP sent to your phone number");
                    editText.requestFocus();
                }

                else {
                    verifyCode(editText.getText().toString().trim());
                }
            }
        });

        buttonResendCode.setOnClickListener(new View.OnClickListener() {    //To resend user's phone number to Firebase to get OTP
            @Override
            public void onClick(View view) {
                sendVerificationCode("+65" + phonenumber);
            }
        });
    }

    void openHomePage() {   //Launches Home Page
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void verifyCode(String code) {  //Launches verification check
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {    //Checks if verificationId matches code. If successful, launches Home Page.
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

    private void sendVerificationCode(String phoneNumber) { //Sends user's phone number to Firebase to get OTP

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {    //Methods to retrieve verificationId from Firebase and for auto submission
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }
    };
}
