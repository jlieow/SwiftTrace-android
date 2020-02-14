package com.swiftoffice.swifttrace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.swiftoffice.swifttrace.R;
import com.swiftoffice.swifttrace.adapters.TemperatureRecordAdapter;

public class TemperatureRecordActivity extends AppCompatActivity {

    //Variables
    RecyclerView tempRecordList;
    private Toolbar toolBar;
    private TextView tvToolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_temp_record);

        initViews();

        //To set the toolbar title
        tvToolBarTitle.setText(getResources().getString(R.string.temperature_record));

        setAdapter();
    }

    //Set Temperature list adapter
    private void setAdapter() {
        //Adapter
        TemperatureRecordAdapter tempRecordAdapter = new TemperatureRecordAdapter(this);
        tempRecordList.setAdapter(tempRecordAdapter);
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
