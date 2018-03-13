package com.projects.doctor.datavaultultimate.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.projects.doctor.datavaultultimate.InAppReminder.StopMedia;
import com.projects.doctor.datavaultultimate.R;

/**
 * Created by doctor on 21-07-2017.
 */

public class Alarm_Ringing_Screen extends AppCompatActivity{

    AlertDialog dialog;

    private String title;
    static MediaPlayer mp;
    NotificationManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("titleOfReminder");
        makeAlert();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("My Reminder");
        builder.setContentText(title);
        builder.setSmallIcon(R.drawable.ic_delete_forever_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.finall));

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, new Intent(this, StopMedia.class),0);

        builder.setContentIntent(pi);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(1,builder.build());

    }

    private void makeAlert() {
        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_alarm_ringing, null);

        final TextView textView_date, textView_content;
        Button btn_stop;
        btn_stop = (Button) view.findViewById(R.id.btn_stop_alert_alarm_id);
        textView_content = (TextView) view.findViewById(R.id.content_textview_alarm_ringing_id);
        textView_date = (TextView) view.findViewById(R.id.reminder_date_alert_alarm_id);

        textView_content.setText(textView_content.getText().toString()+title);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                manager.cancel(1);
                dialog.dismiss();
                finish();
            }
        });
    }

    public static void stopMedia(){
        mp.stop();
    }

}
