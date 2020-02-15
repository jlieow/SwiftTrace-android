package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swiftoffice.swifttrace.R;

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
    String datetime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_temperature);

        initViews();

        //Set Toolbar Title
        tvToolBarTitle.setText(getResources().getString(R.string.insert_temperature));
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
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                btnPickDateTime.setText(dateFormat.format(calendar.getTime()));

                datetime = dateFormat.format(calendar.getTime());

                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void insertTemperatureData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, String> Temperature = new HashMap<>();
        Temperature.put("Date", datetime.trim().substring(0, 10).trim());
        Temperature.put("Time", datetime.trim().substring(10, btnPickDateTime.getText().toString().trim().length()-2).trim());
        Temperature.put("Temperature", eTemperature.getText().toString().trim());
        Temperature.put("UserID", user.getUid());

        db.collection("TemperatureReading")
                .add(Temperature)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Success", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fail", "Error writing document", e);
                    }
                });
    }

    private void openInsertTemperatureActivity() {
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
                Log.d("Success", "DocumentSnapshot successfully written!");
                btnPickDateTime.setError(null);
                openDateTimePicker();
                break;

            case R.id.btnSubmit:
                Log.d("Success", "DocumentSnapshot successfully written!");

                if (datetime == "") {
                    btnPickDateTime.setError(getResources().getString(R.string.please_select_date_time));
                    btnPickDateTime.requestFocus();

                } else if (eTemperature.getText().toString().isEmpty()) {
                    eTemperature.setError(getResources().getString(R.string.please_enter_your_temperature));
                    eTemperature.requestFocus();

                } else {
                    insertTemperatureData();
                    openInsertTemperatureActivity();
                }

                break;
        }
    }
}
