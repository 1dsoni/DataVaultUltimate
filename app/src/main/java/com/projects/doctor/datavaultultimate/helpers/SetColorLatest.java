package com.projects.doctor.datavaultultimate.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by doctor on 08-08-2017.
 */

public class SetColorLatest {
    public static void setColorVal(Context context, int color){
        SharedPreferences preferences = context.getSharedPreferences("MyPrefColor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("latestColor", ""+color);
        editor.commit();
    }

    public static int getColorVal(Context context){
        return Integer.parseInt(context.getSharedPreferences("MyPrefColor", Context.MODE_PRIVATE).getString("latestColor", ""+-685812180));
    }
    public static int getDefaultColor(Context context){
        return Integer.parseInt(context.getSharedPreferences("MyPrefColor", Context.MODE_PRIVATE).getString("latestColor", ""+-685812180));
    }
}
