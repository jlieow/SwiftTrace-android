package com.swiftoffice.swifttrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
