package com.swiftoffice.swifttrace.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.swiftoffice.swifttrace.R;

import static com.swiftoffice.swifttrace.common.AppConstants.COUNTRY_CODE;

public class HealthDeclarationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    //Variables
    private Toolbar toolBar;
    private TextView tvToolBarTitle;
    private LinearLayout llFullName, llPassportNo, llContactNumber, llCompany;
    private EditText edFullName, edPassportNo, edContactNumber, edCompany;
    private Switch sHH1, sHH2, sHH3, sHH4, sHH5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_declaration);

        initViews();

        tvToolBarTitle.setText(getResources().getString(R.string.health_declaration));


    }

    //Initialization
    private void initViews() {
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);

        llFullName = findViewById(R.id.llFullName);
        llPassportNo = findViewById(R.id.llPassportNo);
        llContactNumber = findViewById(R.id.llContactNumber);
        llCompany = findViewById(R.id.llCompany);

        edFullName = findViewById(R.id.edFullName);
        edPassportNo = findViewById(R.id.edPassportNo);
        edContactNumber = findViewById(R.id.edContactNumber);
        edCompany = findViewById(R.id.edCompany);

        sHH1 = findViewById(R.id.sHH1);
        sHH2 = findViewById(R.id.sHH2);
        sHH3 = findViewById(R.id.sHH3);
        sHH4 = findViewById(R.id.sHH4);
        sHH5 = findViewById(R.id.sHH5);

        //SetToolbar
        setSupportActionBar(toolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        listners();
    }


    //Listenters
    private void listners() {
        sHH1.setOnCheckedChangeListener(this);
        sHH2.setOnCheckedChangeListener(this);
        sHH3.setOnCheckedChangeListener(this);
        sHH4.setOnCheckedChangeListener(this);
        sHH5.setOnCheckedChangeListener(this);
    }

    // Check Validations
    private boolean checkValidations() {
        llFullName.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        llPassportNo.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        llContactNumber.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        llCompany.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        if (edFullName.getText().toString().trim().isEmpty()) {
            llFullName.setBackgroundColor(getResources().getColor(R.color.colorError));
        } else if (edPassportNo.getText().toString().trim().isEmpty()) {
            llPassportNo.setBackgroundColor(getResources().getColor(R.color.colorError));
        } else if (edContactNumber.getText().toString().trim().isEmpty()) {
            llContactNumber.setBackgroundColor(getResources().getColor(R.color.colorError));
        } else if (edCompany.getText().toString().trim().isEmpty()) {
            llCompany.setBackgroundColor(getResources().getColor(R.color.colorError));
        } else {
            return true;
        }

        Toast.makeText(this, getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();

        return false;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.xml.menu, menu);
        MenuItem addItem = menu.findItem(R.id.action_Add);
        addItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.action_Tick:
                if (checkValidations()) {
                    openDialog();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    //Alert dialog (To confirm entered number is correct or not)
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.declaration_text));
        String positiveText = getResources().getString(R.string.i_agree);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialog
                        dialog.dismiss();
                        //TODO NEED TO SYNC DATA ON FIRESTORE
                        Toast.makeText(HealthDeclarationActivity.this, "Work in Progress", Toast.LENGTH_LONG).show();
                    }
                });

        String negativeText = getResources().getString(R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    /**
     * On back pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (sHH1.isChecked()) {

        } else {

        }
        if (sHH2.isChecked()) {

        } else {

        }
        if (sHH3.isChecked()) {

        } else {

        }
        if (sHH4.isChecked()) {

        } else {

        }
        if (sHH5.isChecked()) {

        } else {

        }
    }


}
