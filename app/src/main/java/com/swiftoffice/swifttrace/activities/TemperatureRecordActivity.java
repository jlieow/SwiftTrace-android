package com.swiftoffice.swifttrace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.swiftoffice.swifttrace.classes.TemperatureRecord;
import com.swiftoffice.swifttrace.common.ProgressBarDialog;

import java.io.File;
import java.io.FileOutputStream;
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

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {    //On click listener for recyclerview
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            HashMap<String, String> TemperatureRecord = TemperatureRecordList.get(position);

            com.swiftoffice.swifttrace.classes.TemperatureRecord temperatureRecord = new TemperatureRecord(TemperatureRecord.get("Date"), TemperatureRecord.get("Time"), TemperatureRecord.get("Temperature"), TemperatureRecord.get("DocID"));

            openInsertTemperatureActivity(temperatureRecord);
        }
    };

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
        TemperatureRecordAdapter tempRecordAdapter = new TemperatureRecordAdapter(this, TemperatureRecordList);
        tempRecordList.setAdapter(tempRecordAdapter);
        tempRecordList.setLayoutManager(new LinearLayoutManager(this));
        tempRecordAdapter.setOnItemClickListener(onItemClickListener);
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
                                    TemperatureRecord.put("Date", document.getData().get("Date").toString());
                                    TemperatureRecord.put("Time", document.getData().get("Time").toString());
                                    TemperatureRecord.put("Temperature", document.getData().get("Temperature").toString());
                                    TemperatureRecord.put("DocID", document.getId());   //Get's document ID
                                    TemperatureRecordList.add((HashMap) TemperatureRecord);

                                }

                            } else {
                                Log.w("fail", "Error getting documents.", task.getException());
                            }

                            setAdapter();
                            ProgressBarDialog.dismissProgressDialog();
                        }
                    });
        }
    }


    private void generateCSV() {    //Generates CSV after task is successful

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
                                    TemperatureRecord.put("Date", document.getData().get("Date").toString());
                                    TemperatureRecord.put("Time", document.getData().get("Time").toString());
                                    TemperatureRecord.put("Temperature", document.getData().get("Temperature").toString());
                                    TemperatureRecord.put("DocID", document.getId());   //Get's document ID
                                    TemperatureRecordList.add((HashMap) TemperatureRecord);

                                }

                                exportcsv();    //Outputs CSV
                            } else {
                                Log.w("fail", "Error getting documents.", task.getException());
                            }

                            setAdapter();
                            ProgressBarDialog.dismissProgressDialog();
                        }
                    });
        }
    }


    //Initializations
    private void initViews() {
        tempRecordList = findViewById(R.id.tempRecordList);
        toolBar = findViewById(R.id.toolBar);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);

        ProgressBarDialog.showProgressBar(this, "");


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
                openInsertTemperatureActivity(new TemperatureRecord(null, null, null, null));
                break;

            case R.id.action_Tick:
                Toast.makeText(this, "Action Tick", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInsertTemperatureActivity(TemperatureRecord temperatureRecord) {
        Intent intent = new Intent(this, InsertTemperatureActivity.class);
        intent.putExtra("TemperatureRecord", (Parcelable) temperatureRecord);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void exportcsv() {

        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Date,Time,Temperature");
        for(int i = 0; i<TemperatureRecordList.size(); i++){
            data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
            data.append("\n"+TemperatureRecordList.get(i).get("Date")+","+TemperatureRecordList.get(i).get("Time")+","+TemperatureRecordList.get(i).get("Temperature"));
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", getApplicationContext().MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(getApplicationContext(), "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

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
