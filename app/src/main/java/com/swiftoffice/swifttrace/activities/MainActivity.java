package com.swiftoffice.swifttrace.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.swiftoffice.swifttrace.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }


    //Initializations
    private void initViews() {
        btnLogin = findViewById(R.id.btnLogin);

        listeners();
    }


    //Listeners
    private void listeners() {
        btnLogin.setOnClickListener(this);
    }




    //On Click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                openLogin();
                break;
        }
    }


    //Redirect  to LoginActivity Screen
    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    //On Backpressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
