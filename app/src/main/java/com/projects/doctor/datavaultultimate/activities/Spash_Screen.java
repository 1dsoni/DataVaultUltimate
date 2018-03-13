package com.projects.doctor.datavaultultimate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.projects.doctor.datavaultultimate.R;

public class Spash_Screen extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash__screen);

       handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Spash_Screen.this,activity_password.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //used to remove the handler call
        handler.removeCallbacksAndMessages(null);
        finish();
    }
}
