package com.projects.doctor.datavaultultimate.activities;

/*
* this shows the data of each note separately when clicked on show data in alert
*
* */

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabase;
import com.projects.doctor.datavaultultimate.databases.Subject_Notes_DatabaseTable;
import com.projects.doctor.datavaultultimate.helpers.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class notes_item_info_activity extends AppCompatActivity {

    private TextView textView_note;
    private ImageView image_icon;
    private Button btn_back;

    private String id, sub_name;
    private byte[] image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_item_info);
        linkItems();
        setListeners();

        setDataOnItems();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDataOnItems(){
        Intent i = getIntent();
        id = i.getStringExtra("id");
        sub_name = i.getStringExtra("subject");
        image = i.getByteArrayExtra("image_array");

        String where = Subject_Notes_DatabaseTable.ID_COL+" = "+"'"+id+"'";

        SQLiteDatabase db = new SubjectDatabase(this).getWritableDatabase();
        Cursor cursor = Subject_Notes_DatabaseTable.select(false,sub_name,db,null,where,null,null);

        if (cursor.getCount()>=1&&cursor.moveToFirst()){

            textView_note.setText(cursor.getString(1));

            try {
                image_icon.setImageBitmap(Utils.getImage(image));
                //Log.d("11111","reached!!");
                image_icon.setVisibility(View.VISIBLE);
            }
            catch (Exception e){
                image_icon.setVisibility(View.GONE);
            }
        }
    }

    private void linkItems(){
        textView_note = (TextView) findViewById(R.id.edit_notes_notes_info_item_id);
        image_icon = (ImageView) findViewById(R.id.image_for_notes_info_item_id);
        btn_back = (Button) findViewById(R.id.back_btn_note_info_item_id);
    }

    private void setListeners(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions(v);
            }
        });
    }

    private void showOptions(final View v) {
        final CharSequence[] sequences = {"save in gallery","cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog ;
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sequences[which]=="save in gallery"){
                    saveImage(v);
                }
                else if (sequences[which]=="cancel"){
                    dialog.dismiss();
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }


    String TAG ="1234";

    private void saveImage(View view) {
        File mainDir = new File(Environment.getExternalStorageDirectory(),"My_File_Store");
        createDir(mainDir);
        Log.d(TAG, "saveImage: "+mainDir);
        File subDir = new File(mainDir,sub_name.replace("'","").replace(" ","_"));
        createDir(subDir);
        Log.d(TAG, "saveImage: "+subDir);

        try {
            FileOutputStream fos = new FileOutputStream(subDir+"/"+sub_name.replace("'","").replace(" ","_")+"_"+System.currentTimeMillis()+"_image.jpg");
            fos.write(image);
            fos.close();
            //Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            Snackbar.make(view,"Image Added in Gallery",Snackbar.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean createDir(File f){
        boolean ret = true;
        if (!f.exists()){
            if (!f.mkdir()){
                ret = false;
            }
        }
        return ret;
    }
}
