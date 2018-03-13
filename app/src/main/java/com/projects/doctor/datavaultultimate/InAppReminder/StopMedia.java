package com.projects.doctor.datavaultultimate.InAppReminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.projects.doctor.datavaultultimate.activities.Alarm_Ringing_Screen;

/**
 * Created by doctor on 24-07-2017.
 */

public class StopMedia extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Alarm_Ringing_Screen.stopMedia();
    }
}
