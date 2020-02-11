package com.swiftoffice.swifttrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    //Variables
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declarations
        button = findViewById(R.id.submitButton);
        editText = findViewById(R.id.phonenumberInput);

        //Listeners
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOTP();
            }
        });
    }

    void openOTP() {
        String phonenumber = editText.getText().toString().trim();

        if(phonenumber.isEmpty()) {
            editText.setError("Please enter your phone number");
            editText.requestFocus();
        }

        if(phonenumber.length() != 8) {
            editText.setError("Please enter a valid phone number");
            editText.requestFocus();
        }

        else {
            Intent intent = new Intent(this, OTP.class);
            intent.putExtra("phonenumber", phonenumber);
            startActivity(intent);
        }
    }
}
