package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.swiftoffice.swifttrace.R;
import com.swiftoffice.swifttrace.adapters.TemperatureRecordAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemperatureRecordActivity extends AppCompatActivity {

    //Variables
    RecyclerView tempRecordList;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<HashMap<String, String>> TemperatureRecordList = new ArrayList<>();    //Used to store all temperature records

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_temp_record);

        initViews();
        getUserTemperatureRecord();

        //To set the toolbar title
        tvToolBarTitle.setText(getResources().getString(R.string.temperature_record));
    }

    //Set Temperature list adapter
    private void setAdapter() {
        //Adapter
        Log.d("success1", TemperatureRecordList.toString());
        TemperatureRecordAdapter tempRecordAdapter = new TemperatureRecordAdapter(this, TemperatureRecordList);
        tempRecordList.setAdapter(tempRecordAdapter);
        tempRecordList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getUserTemperatureRecord() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {

            db.collection("TemperatureReading")
                    .whereEqualTo("UserID",user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Retrieves and stores all temperature records in a dictionary
                                    Map<String, String> TemperatureRecord = new HashMap<>();
                                    TemperatureRecord.put("Time", document.getData().get("Time").toString());
                                    TemperatureRecord.put("Date", document.getData().get("Date").toString());
                                    TemperatureRecord.put("Temperature", document.getData().get("Temperature").toString());
                                    TemperatureRecordList.add((HashMap) TemperatureRecord);

                                    Log.d("success", TemperatureRecordList.toString());

                                }
                            } else {
                                Log.w("fail", "Error getting documents.", task.getException());
                            }

                            setAdapter();
                        }
                    });
        }
    }


    //Initializations
    private void initViews() {
        tempRecordList = findViewById(R.id.tempRecordList);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        tempRecordList.setLayoutManager(linearLayoutManager);

        //SetToolbar
        setSupportActionBar(toolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }


    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.xml.menu, menu);
        MenuItem tickItem = menu.findItem(R.id.action_Tick);
        tickItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();


        switch (menuItem) {
            case R.id.action_Add:
                openInsertTempratureActivity();
                Intent intent = new Intent(this, InsertTemperatureActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.action_Tick:
                Toast.makeText(this, "Action Tick", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInsertTempratureActivity() {

    }


    //Back Button
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
}
