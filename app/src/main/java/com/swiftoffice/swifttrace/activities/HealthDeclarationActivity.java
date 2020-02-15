package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swiftoffice.swifttrace.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.swiftoffice.swifttrace.common.AppConstants.COUNTRY_CODE;

public class HealthDeclarationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    //Variables
    private Toolbar toolBar;
    private TextView tvToolBarTitle;
    private LinearLayout llFullName, llPassportNo, llContactNumber, llCompany;
    private EditText edFullName, edPassportNo, edContactNumber, edCompany;
    private Switch sHH1, sHH2, sHH3, sHH4, sHH5, sHH6;
    private String getsHH1 = "false", getsHH2 = "false", getsHH3 = "false", getsHH4 = "false", getsHH5 = "false", getsHH6 = "false";

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Get current Date
    DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
    CharSequence currentDate  = date.format(new Date());

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
        sHH6 = findViewById(R.id.sHH6);

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
        sHH1.setOnCheckedChangeListener(this);
        sHH2.setOnCheckedChangeListener(this);
        sHH3.setOnCheckedChangeListener(this);
        sHH4.setOnCheckedChangeListener(this);
        sHH5.setOnCheckedChangeListener(this);
        sHH6.setOnCheckedChangeListener(this);
    }

    // Check Validations
    private boolean checkValidations() {
        llFullName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        llPassportNo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        llContactNumber.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        llCompany.setBackgroundColor(ContextCompat.getColor(this,R.color.colorWhite));

        if (edFullName.getText().toString().trim().isEmpty()) {
            llFullName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError));
        } else if (edPassportNo.getText().toString().trim().isEmpty() || edPassportNo.length() != 4) {
            llPassportNo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError));
        } else if (edContactNumber.getText().toString().trim().isEmpty() || edContactNumber.length() != 8) {
            llContactNumber.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError));
        } else if (edCompany.getText().toString().trim().isEmpty()) {
            llCompany.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError));
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

        if (menuItem == R.id.action_Tick) {
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
        builder.setMessage(getString(R.string.tracking_declaration_text));
        String positiveText = getResources().getString(R.string.i_agree);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialog
                        dialog.dismiss();
                        //TODO NEED TO SYNC DATA ON FIRESTORE
                        Toast.makeText(HealthDeclarationActivity.this, "Work in Progress", Toast.LENGTH_LONG).show();
                        checkHealthDeclaration();   //Checks if document submitted previously. If submitted previously, do not send data.
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

    private void checkHealthDeclaration() {

        DocumentReference docRef = db.collection(getResources().getString(R.string.HealthDeclaration)).document(user.getUid() + "+" + currentDate.toString().trim());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Success", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("Fail", "No such document, call insertHealthDeclaration()");
                        insertHealthDeclaration();
                    }
                } else {
                    Log.d("Fail", "get failed with ", task.getException());
                }
            }
        });
    }

    private void insertHealthDeclaration() {

        DateFormat datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        CharSequence currentDateandTime  = datetime.format(new Date());
        CharSequence currentDate  = date.format(new Date());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, String> HealthDeclaration = new HashMap<>();
        HealthDeclaration.put("CompanyName", edCompany.getText().toString().trim());
        HealthDeclaration.put("ContactNo", edContactNumber.getText().toString().trim());
        HealthDeclaration.put("DateTime", currentDateandTime.toString().trim());
        HealthDeclaration.put("FullName", edFullName.getText().toString().trim());
        HealthDeclaration.put("HealthQ1", getsHH1);
        HealthDeclaration.put("HealthQ2", getsHH2);
        HealthDeclaration.put("HealthQ3", getsHH3);
        HealthDeclaration.put("HealthQ4", getsHH4);
        HealthDeclaration.put("HealthQ5", getsHH5);
        HealthDeclaration.put("HealthQ6", getsHH6);
        HealthDeclaration.put("PersonID", edPassportNo.getText().toString().trim());

        db.collection(getResources().getString(R.string.HealthDeclaration))
                .document(user.getUid() + "+" + currentDate.toString().trim())
                .set(HealthDeclaration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fail", "Error writing document", e);
                    }
                });

        Log.w("success", "edit");

        //openTemperatureRecordActivity();
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
            getsHH1 = "true";
        } else {
            getsHH1 = "false";
        }
        if (sHH2.isChecked()) {
            getsHH2 = "true";
        } else {
            getsHH2 = "false";
        }
        if (sHH3.isChecked()) {
            getsHH3 = "true";
        } else {
            getsHH3 = "false";
        }
        if (sHH4.isChecked()) {
            getsHH4 = "true";
        } else {
            getsHH4 = "false";
        }
        if (sHH5.isChecked()) {
            getsHH5 = "true";
        } else {
            getsHH5 = "false";
        }
        if (sHH6.isChecked()) {
            getsHH6 = "true";
        } else {
            getsHH6 = "false";
        }
    }


}
