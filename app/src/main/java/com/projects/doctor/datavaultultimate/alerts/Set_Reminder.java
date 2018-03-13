package com.projects.doctor.datavaultultimate.alerts;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.projects.doctor.datavaultultimate.InAppReminder.AlarmBroadcast;
import com.projects.doctor.datavaultultimate.R;

import java.util.Calendar;

/**
 * Created by doctor on 21-07-2017.
 */

public class Set_Reminder {

    AlertDialog dialog;

    private Context context;
    private String title;
    private int resource;

    public Set_Reminder(Context context, int resouce, String title){
        this.context = context;
        this.title = title;
        this.resource = resouce;
        makeAlert();
    }

    private void makeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);

        final TextInputLayout layout_title;
        final EditText editText_title;
        final TextView btn_time, btn_date;
       // RadioGroup radioGroup;
        Button btn_set, btn_back, btn_clear;

        layout_title = (TextInputLayout) view.findViewById(R.id.title_edit_layout_reminder_screen_id);
        editText_title = (EditText) view.findViewById(R.id.title_edit_reminder_screen_id);
       // textView_date = (TextView) view.findViewById(R.id.textview_date_reminder_screen_id);
       // textView_time = (TextView) view.findViewById(R.id.textview_time_reminder_screen_id);
        //radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_reminder_screen_id);
        btn_back = (Button) view.findViewById(R.id.btn_back_alarm_reminder_sreen_id);
        btn_clear = (Button) view.findViewById(R.id.btn_clear_alarm_reminder_sreen_id);
        btn_set = (Button) view.findViewById(R.id.btn_set_alarm_reminder_sreen_id);
        btn_date = (TextView) view.findViewById(R.id.btn_date_reminder_screen_id);
        btn_time = (TextView) view.findViewById(R.id.btn_time_reminder_screen_id);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "";
                editText_title.setText("");
            }
        });

        editText_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_title.setText(title);
                editText_title.setOnClickListener(null);
            }
        });

        //completes the text note on clicking it
        if (title.length()<24){
            editText_title.setText(title);
        }
        else {
            editText_title.setText(title.substring(0, 24)+" ...");
        }
        //setting the current date and time
        Calendar c = Calendar.getInstance();
        btn_date.setText(""+c.get(c.DAY_OF_MONTH)+"/"+(c.get(c.MONTH)+1)+"/"+c.get(c.YEAR));
        btn_time.setText(""+c.get(c.HOUR_OF_DAY)+" : "+c.get(c.MINUTE));

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btn_date.setText(""+dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },cal.get(cal.YEAR),cal.get(cal.MONTH),cal.get(cal.DAY_OF_MONTH));

                datePickerDialog.updateDate(cal.get(cal.YEAR),cal.get(cal.MONTH),cal.get(cal.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        btn_time.setText(""+hourOfDay+" : "+minute);
                    }
                }, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);

                timePickerDialog.updateTime(cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE));
                timePickerDialog.show();
            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_title.getText().toString().isEmpty()){
                    layout_title.setError("Title cannot Be Empty! ");
                    return;
                }
                title = editText_title.getText().toString();
                String date, time;
                int hour, minute, year, month, dayOfMonth;


                date = btn_date.getText().toString();
                time = btn_time.getText().toString();

                hour = Integer.parseInt(time.split(" : ")[0]);
                minute = Integer.parseInt(time.split(" : ")[1]);

                dayOfMonth = Integer.parseInt(date.split("/")[0]);
                month = Integer.parseInt(date.split("/")[1]);
                year = Integer.parseInt(date.split("/")[2]);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(cal.HOUR_OF_DAY, hour);
                cal.set(cal.MINUTE, minute);
                cal.set(cal.YEAR, year);
                cal.set(cal.MONTH, (month-1));
                cal.set(cal.DAY_OF_MONTH, dayOfMonth);

                Intent intent = new Intent(context, AlarmBroadcast.class);
                intent.putExtra("titleOfAlarm", title);
//                int random = (int) (Math.random()*100);
//                int random2 = (int) ((int) random+(1+Math.random()*100));
                PendingIntent pi = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis()/1000, intent, 0);

                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                dialog.dismiss();
                Toast.makeText(context, "Remider Set Successfully", Toast.LENGTH_SHORT).show();
                Log.d("1234", "cal"+cal.getTime()
                );
            }
        });
    }
}
