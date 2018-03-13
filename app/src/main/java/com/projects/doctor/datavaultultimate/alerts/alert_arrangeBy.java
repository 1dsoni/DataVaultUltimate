package com.projects.doctor.datavaultultimate.alerts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.activities.activity_password;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabaseTable;

/**
 * Created by doctor on 27-07-2017.
 */

public class alert_arrangeBy extends AppCompatActivity {

    private Context context;
    private AlertDialog dialog;
    private String order = "ASC";

    private static final String PREF_ARRANGEBY_NAME = "arrangeBy_dataVault";          //name of pref file
    public static final String PREF_ARRANGEBY_STATUSORDERBY = "arrangeByStatus";     //key of date/name wise orderby
    public static final String PREF_ARRANGEBY_ORDERBY = "arrangeByOrderStatus";      //key of asc/dsc arrangement

    public void makeAlert(Context context){

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_arrange_by_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Grid Arrangement");

        RadioGroup rg_arrange_by = (RadioGroup) view.findViewById(R.id.rg_alert_arrange_by_id);
        RadioButton rb_date = (RadioButton) view.findViewById(R.id.date_created_rb__alert_arrange_id);
        RadioButton rb_name = (RadioButton) view.findViewById(R.id.name_created_rb__alert_arrange_id);
        RadioButton rb_color = (RadioButton) view.findViewById(R.id.color_used_rb__alert_arrange_id);

        RadioGroup rg_order = (RadioGroup) view.findViewById(R.id.rg_order_by_alert_arrange_by_id);
        RadioButton rb_asc, rb_desc;
        rb_asc = (RadioButton) view.findViewById(R.id.rb_ascending_alert_arrange_by_id);
        rb_desc = (RadioButton) view.findViewById(R.id.rb_descending_alert_arrange_by_id);

        SharedPreferences preferences = context.getSharedPreferences(PREF_ARRANGEBY_NAME, MODE_PRIVATE);

        //checking previous CATEGORY of arrangement
        if (preferences.getString(PREF_ARRANGEBY_STATUSORDERBY, SubjectDatabaseTable.ID_COL).equals(SubjectDatabaseTable.ID_COL)){
            rb_date.setChecked(true);
        }
        else if (preferences.getString(PREF_ARRANGEBY_STATUSORDERBY, SubjectDatabaseTable.ID_COL).equals(SubjectDatabaseTable.COLOR_COL)){
            rb_color.setChecked(true);
        }
        else {
            rb_name.setChecked(true);
        }


        //checking previous ORDER of arrangement
        if (preferences.getString(PREF_ARRANGEBY_ORDERBY, "ASC").equals("DESC")){
            rb_desc.setChecked(true);
        }
        else {
            rb_asc.setChecked(true);
        }

        dialog = builder.create();
        dialog.show();

        rb_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = "ASC";
            }
        });
        rb_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = "DESC";
            }
        });

        rb_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setArrangement(SubjectDatabaseTable.ID_COL);
            }
        });
        rb_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setArrangement(SubjectDatabaseTable.COLOR_COL);
            }
        });

        rb_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setArrangement(SubjectDatabaseTable.NAME_COL);
            }
        });

    }

    private void setArrangement(String str){
        SharedPreferences preferences = context.getSharedPreferences(PREF_ARRANGEBY_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ARRANGEBY_STATUSORDERBY, str);
        editor.putString(PREF_ARRANGEBY_ORDERBY, order);
        //Log.d("1234", "arr "+str+" or "+order);
        editor.commit();
        dialog.dismiss();
        this.finish();
    }
}
