package com.projects.doctor.datavaultultimate.activities;

/*
* activity containing the viewpager
*
* inflates the menu
* */

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.projects.doctor.datavaultultimate.adapters.C_Adapter_TabPager;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.alerts.alert_arrangeBy;
import com.projects.doctor.datavaultultimate.helpers.SetColorLatest;

public class Subject_Note extends AppCompatActivity {

    //debugg
    private String TAG = "123321";

    ViewPager viewPager;
    ActionBar actionBar;
    C_Adapter_TabPager cAdapterTabPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject__note);

        linkComponents();

        cAdapterTabPager = new C_Adapter_TabPager(getSupportFragmentManager());

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                actionBar = getSupportActionBar();
                actionBar.setSelectedNavigationItem(position);
            }
        });

        viewPager.setAdapter(cAdapterTabPager);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };
        viewPager.setAdapter(cAdapterTabPager);

        actionBar.addTab(actionBar.newTab().setText("Manage Entries").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Instant Notes").setTabListener(tabListener));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.setpassword_menu_item){
            setPass();
        }
        else if (id==R.id.arrange_menu_item){
            alert_arrangeBy arrangeBy = new alert_arrangeBy();
            arrangeBy.makeAlert(this);
        }
        else if (id==R.id.exit_menu_item){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog;
        View v = getLayoutInflater().inflate(R.layout.alert_set_password, null);
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
        final EditText editText_pass, edit_retype;
        final TextInputLayout layout_pass, layout_retype_pass;
        Button btn_ok, btn_cancel,btn_remove;

        layout_pass = (TextInputLayout) v.findViewById(R.id.new_password_alert_edit_text_layout_id);
        layout_retype_pass = (TextInputLayout) v.findViewById(R.id.get_retyped_pass_alert_edit_text_layout_id);
        editText_pass = (EditText) v.findViewById(R.id.new_password_edit_alert_set_pass_id);
        edit_retype = (EditText) v.findViewById(R.id.retype_password_edit_alert_set_pass_id);
        btn_cancel = (Button) v.findViewById(R.id.cancel_btn_alert_set_pass_id);
        btn_ok = (Button) v.findViewById(R.id.ok_btn_alert_set_pass_id);
        btn_remove = (Button) v.findViewById(R.id.remove_btn_alert_set_pass_id);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_pass.getText().toString().isEmpty()){
                    layout_pass.setError("Field Cannot Be Empty");
                }
                else if (edit_retype.getText().toString().equals(editText_pass.getText().toString())){
                    SharedPreferences sp = getSharedPreferences("myPass",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("passStatus",true);
                    editor.putString("passwordScreen",editText_pass.getText().toString());
                    editor.commit();
                    Toast.makeText(Subject_Note.this, "Password Changed !", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    layout_pass.setError("Both Fields Must Match!");
                    layout_retype_pass.setError("Both Fields Must Match!");
                }

            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("myPass",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("passStatus",false);
                editor.putString("passwordScreen","");
                editor.commit();
                Toast.makeText(Subject_Note.this, "Password Cleared !", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    private void linkComponents(){
        viewPager = (ViewPager) findViewById(R.id.subject_viewpager);
    }

}