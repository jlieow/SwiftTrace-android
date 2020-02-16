package com.swiftoffice.swifttrace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.swiftoffice.swifttrace.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnHealthDeclaration;
    private Button btnTemperatureRecord;
    private Button btnEnableTracing;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();

        //Set toolbar title
        tvToolBarTitle.setText(getResources().getString(R.string.swift_trace));


    }


    //Initialization
    private void initViews() {
        btnHealthDeclaration = findViewById(R.id.btnHealthDeclaration);
        btnTemperatureRecord = findViewById(R.id.btnTemperatureRecord);
        btnEnableTracing = findViewById(R.id.btnEnableTracing);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);


        //Toolbar
        setSupportActionBar(toolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        listeners();
    }


    //Listeners
    private void listeners() {
        btnHealthDeclaration.setOnClickListener(this);
        btnTemperatureRecord.setOnClickListener(this);
        btnEnableTracing.setOnClickListener(this);
    }


    //On click listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHealthDeclaration:
                openHealthDeclaration();
                break;

            case R.id.btnTemperatureRecord:
                openTemperatureRecord();
                break;

            case R.id.btnEnableTracing:
                openEnableTracing();
                break;
        }
    }


    //Redirect to health declaration screen
    void openHealthDeclaration() {
        Intent intent = new Intent(this, HealthDeclarationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    //Redirect to Temperature record screen
    void openTemperatureRecord() {
        Intent intent = new Intent(this, TemperatureRecordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //IN Progress
    void openEnableTracing() {
        //TODO NEED TO CHANGE IN TO ALERT
        Toast.makeText(this, getResources().getString(R.string.contact_tracing_is_currently_being_enhanced), Toast.LENGTH_LONG).show();
    }


    /**
     * On back pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
