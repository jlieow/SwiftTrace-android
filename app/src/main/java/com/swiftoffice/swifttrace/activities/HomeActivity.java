package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.swiftoffice.swifttrace.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnHealthDeclaration;
    private Button btnTemperatureRecord;
    private Button btnEnableTracing;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;

    String firebaseid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();



        //Retrieves for Firebase Messaging ID
        RetrieveFirebaseID();

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


    public void RetrieveFirebaseID() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Fail", "getInstanceId failed", task.getException());
                        }

                        // Get new Instance ID token
                        firebaseid = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, firebaseid);
                        Log.d("Success", msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        try {   //Creates and stores Firebase Messaging ID in a app specific cache storage
                            String filename = getResources().getString(R.string.firebase_messaging_id);
                            File.createTempFile(filename, null, getApplicationContext().getCacheDir());
                            File cacheFile = new File(getApplicationContext().getCacheDir(), filename);

                            FileWriter fileWriter = new FileWriter(cacheFile.getAbsoluteFile());
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(firebaseid);
                            bufferedWriter.close();

                            FileReader fileReader = new FileReader(cacheFile.getAbsoluteFile());
                            BufferedReader bufferedReader = new BufferedReader(fileReader);

                            Log.w("Success", getResources().getString(R.string.successfully_written_to_cache, bufferedReader.readLine()));

                        } catch (IOException e) {
                            Log.w("Fail", getResources().getString(R.string.failed_to_write_to_cache));
                            e.printStackTrace();
                        }
                    }
                });
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
