package com.swiftoffice.swifttrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    //Variables
    private Button buttonHealthDeclaration;
    private Button buttonTemperatureRecord;
    private Button buttonEnableTracing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Initialisations
        buttonHealthDeclaration = findViewById(R.id.healthdeclarationButton);
        buttonTemperatureRecord = findViewById(R.id.temperaturerecordButton);
        buttonEnableTracing = findViewById(R.id.enabletracingButton);

        //Listeners
        buttonHealthDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHealthDeclaration();
            }
        });
        buttonTemperatureRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTemperatureRecord();
            }
        });
        buttonEnableTracing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnableTracing();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            //Retrieve UID and user's handphone if user is logged in
            String uid = user.getUid();
            String uhp = user.getPhoneNumber();
            Toast.makeText(getApplicationContext(), uid + uhp, Toast.LENGTH_LONG).show();
        }
    }

    void openHealthDeclaration() {
        Intent intent = new Intent(this, HealthDeclaration.class);
        startActivity(intent);
    }

    void openTemperatureRecord() {
        Intent intent = new Intent(this, TemperatureRecord.class);
        startActivity(intent);
    }

    void EnableTracing() {

    }

}
