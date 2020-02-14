package com.swiftoffice.swifttrace.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.swiftoffice.swifttrace.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InsertTemperatureActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnPickDateTime;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_temprature);

        initViews();

        //Set Toolbar Title
        tvToolBarTitle.setText(getResources().getString(R.string.insert_temperature));


    }


    //Initializations
    private void initViews() {
        //Declarations
        btnPickDateTime = findViewById(R.id.btnPickDateTime);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);

        //SetToolbar
        setSupportActionBar(toolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        listenters();
    }

    //Listeners
    private void listenters() {
        btnPickDateTime.setOnClickListener(this);
    }


    //Date Time picker
    private void openDateTimePicker() {

        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());


                //Set date and time on button
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                btnPickDateTime.setText(dateFormat.format(calendar.getTime()));


                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
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
                openDateTimePicker();
                break;
        }
    }
}
