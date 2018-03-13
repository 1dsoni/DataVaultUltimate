package com.projects.doctor.datavaultultimate.InAppReminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.projects.doctor.datavaultultimate.activities.Alarm_Ringing_Screen;

/**
 * Created by doctor on 20-07-2017.
 */

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("titleOfAlarm");
        Log.d("1234", "onReceive: broadcast m hu");
        Toast.makeText(context, "alarm  "+title, Toast.LENGTH_LONG).show();
        Intent i = new Intent(context, Alarm_Ringing_Screen.class);
        /*
        Alarm_Ringing_Screen screen = new Alarm_Ringing_Screen(context, R.layout.alert_alarm_ringing, title);
*/
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("titleOfReminder", title);
        context.startActivity(i);
    }
}
