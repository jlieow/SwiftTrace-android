package com.swiftoffice.swifttrace.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swiftoffice.swifttrace.R;
import com.swiftoffice.swifttrace.common.AppConstants;
import com.swiftoffice.swifttrace.common.NetworkConnection;

import static com.swiftoffice.swifttrace.common.AppConstants.COUNTRY_CODE;
import static com.swiftoffice.swifttrace.common.AppConstants.PHONE_NUMBER_LENGHT;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnSubmit;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        //Set Toolbar title
        tvToolBarTitle.setText(getResources().getString(R.string.login));


    }

    //Initializations
    private void initViews() {
        btnSubmit = findViewById(R.id.btnSubmit);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

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
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (NetworkConnection.isConnectedToInternet(this)) {
                    openDialog();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    //Alert dialog (To confirm entered number is correct or not)
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle(getString(R.string.phone_number));
        builder.setMessage(getString(R.string.confirm_phone_text) + "\n" + COUNTRY_CODE + etPhoneNumber.getText().toString().trim());
        String positiveText = getResources().getString(R.string.yes);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Check validations
                        if (checkValidations()) {
                            // dismiss dialog
                            dialog.dismiss();
                            openVerifyOtpScreen();
                        }
                    }
                });

        String negativeText = getResources().getString(R.string.no);
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


    //Check validation
    private boolean checkValidations() {
        boolean flag = true;
        if (etPhoneNumber.getText().toString().trim().isEmpty()) {
            etPhoneNumber.setError(getResources().getString(R.string.please_enter_your_phone_number));
            etPhoneNumber.requestFocus();
            flag = false;
        } else if (etPhoneNumber.getText().toString().length() < PHONE_NUMBER_LENGHT) {
            etPhoneNumber.setError(getResources().getString(R.string.please_enter_valid_phone_number));
            etPhoneNumber.requestFocus();
            flag = false;
        }
        return flag;
    }

    //Redirect to otp screen
    void openVerifyOtpScreen() {
        String phoneNo = etPhoneNumber.getText().toString().trim();

        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.putExtra(AppConstants.MOBILE_NUMBER, phoneNo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
