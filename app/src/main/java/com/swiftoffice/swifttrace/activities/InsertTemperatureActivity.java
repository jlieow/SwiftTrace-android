package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swiftoffice.swifttrace.R;
import com.swiftoffice.swifttrace.classes.TemperatureRecord;
import com.swiftoffice.swifttrace.common.ProgressBarDialog;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class InsertTemperatureActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnPickDateTime;
    private Button btnSubmit;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;
    private EditText eTemperature;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TemperatureRecord temperatureRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_temperature);

        getData();


        initViews();

        //Set Toolbar Title
        tvToolBarTitle.setText(getResources().getString(R.string.insert_temperature));
    }

    private void getData() {    //Get data passed from previous activity
        Intent intent = getIntent();

        temperatureRecord = intent.getParcelableExtra("TemperatureRecord");
    }

    //Initializations
    private void initViews() {
        //Declarations
        btnPickDateTime = findViewById(R.id.btnPickDateTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        eTemperature = findViewById(R.id.eTemperature);

        //SetToolbar
        setSupportActionBar(toolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if(temperatureRecord.getDocID() != null) { //If directed from recyclerview takes in recyclerview values
            btnPickDateTime.setText(temperatureRecord.getDate() + " " + temperatureRecord.getTime());
            eTemperature.setText(temperatureRecord.getTemperature());
            eTemperature.clearFocus();
        }

        listeners();
    }

    //Listeners
    private void listeners() {
        btnPickDateTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    //Date Time picker
    private void openDateTimePicker() {

        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute());


                //Set date and time on button
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                btnPickDateTime.setText(dateFormat.format(calendar.getTime()));

                temperatureRecord = new TemperatureRecord(dateFormat.format(calendar.getTime()).trim().substring(0, 10).trim(), dateFormat.format(calendar.getTime()).trim().substring(10).trim(), null, null);

                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void editinsertTemperatureData() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, String> Temperature = new HashMap<>();
        Temperature.put("Date", temperatureRecord.getDate());
        Temperature.put("Time", temperatureRecord.getTime());
        Temperature.put("Temperature", eTemperature.getText().toString().trim());
        Temperature.put("UserID", user.getUid());

        if (temperatureRecord.getDocID() != null){
            db.collection(getResources().getString(R.string.TemperatureReading))
                    .document(temperatureRecord.getDocID())
                    .set(Temperature)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully written!");
                            ProgressBarDialog.dismissProgressDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Fail", "Error writing document", e);
                            ProgressBarDialog.dismissProgressDialog();
                        }
                    });

            Log.w("success", "edit");
        } else {
            db.collection(getResources().getString(R.string.TemperatureReading))
                    .add(Temperature)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Success", "DocumentSnapshot successfully written!");
                            ProgressBarDialog.dismissProgressDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Fail", "Error writing document", e);
                            ProgressBarDialog.dismissProgressDialog();
                        }
                    });

            Log.w("success", "insert");
        }

        openTemperatureRecordActivity();
    }

    private void openTemperatureRecordActivity() {
        Intent intent = new Intent(this, TemperatureRecordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    //Back Button Action
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    //On Click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPickDateTime:
                btnPickDateTime.setError(null);
                openDateTimePicker();
                break;

            case R.id.btnSubmit:
                ProgressBarDialog.showProgressBar(this, "");

                if (temperatureRecord.getDate() == null || temperatureRecord.getTime() == null) {
                    btnPickDateTime.setError(getResources().getString(R.string.please_select_date_time));
                    btnPickDateTime.requestFocus();

                } else if (eTemperature.getText().toString().isEmpty()) {
                    eTemperature.setError(getResources().getString(R.string.please_enter_your_temperature));
                    eTemperature.requestFocus();

                } else {
                    editinsertTemperatureData();
                }

                break;
        }
    }
}
