package com.swiftoffice.swifttrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    //Variables
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declarations
        button = findViewById(R.id.submitButton);

        //Listeners
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOTP();
            }
        });
    }

    void openOTP() {
        Intent intent = new Intent(this, OTP.class);
        startActivity(intent);
    }
}
