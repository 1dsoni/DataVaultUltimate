package com.projects.doctor.datavaultultimate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projects.doctor.datavaultultimate.R;

public class activity_password extends AppCompatActivity {

    private EditText editText_get_pass;
    private Button btn_ok, btn_back;

    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        editText_get_pass = (EditText) findViewById(R.id.get_pass_id);
        btn_back = (Button) findViewById(R.id.btn_back_password_id);
        btn_ok = (Button) findViewById(R.id.btn_ok_password_id);

        SharedPreferences sp = getSharedPreferences("myPass",MODE_PRIVATE);

        if (!sp.getBoolean("passStatus",false)) {
            Intent intent = new Intent(activity_password.this, Subject_Note.class);
            startActivity(intent);
            finish();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = editText_get_pass.getText().toString();
                if (pass.isEmpty()){
                    editText_get_pass.setError("invalid!");
                }

                SharedPreferences sp = getSharedPreferences("myPass",MODE_PRIVATE);
                String food = sp.getString("passwordScreen",null);

                java.util.Calendar calendar = java.util.Calendar.getInstance();

                //this is to get access for any account in case of password lost..
                //remove or change as needed
                if (pass.equals("oop$"+calendar.get(calendar.DAY_OF_MONTH)+calendar.get(calendar.HOUR_OF_DAY)+calendar.get(calendar.MINUTE))){
                    Intent intent = new Intent(activity_password.this,Subject_Note.class);
                    startActivity(intent);
                    finish();
                }
                else if (pass.equals(food)){
                    Intent intent = new Intent(activity_password.this,Subject_Note.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    editText_get_pass.setError("invalid!");
                }
            }
        });
    }
}
