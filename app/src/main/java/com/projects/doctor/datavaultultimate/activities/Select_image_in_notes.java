package com.projects.doctor.datavaultultimate.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.doctor.datavaultultimate.databases.Mydb;
import com.projects.doctor.datavaultultimate.databases.Notes_Storage;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.helpers.SelectImageHelper;
import com.projects.doctor.datavaultultimate.helpers.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Select_image_in_notes extends AppCompatActivity {

    SelectImageHelper helper;
    ImageView imageq;
    Button btn_ok,btn_back;
    TextView textView_notes;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_image_selection_layout);

        id = getIntent().getStringExtra("id");
        linkComponents();
        setListners();
        helper = new SelectImageHelper(this,imageq);
    }

    private void setListners(){
        imageq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.selectImageOption();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try to convert to blob

                try {
                    InputStream iStream = getContentResolver().openInputStream(helper.getURI_FOR_SELECTED_IMAGE());
                    byte[] inputData = Utils.getBytes(iStream);
                    ContentValues cv = new ContentValues();
                    cv.put(Notes_Storage.IMAGE_COL,inputData);

                    SQLiteDatabase db = new Mydb(Select_image_in_notes.this).getWritableDatabase();

                    String w = Notes_Storage.ID_COl+" = "+"'"+id+"'";

                    Log.d("11111", "onClick: ");
                    long i = Notes_Storage.update(db,cv,w,null);

                    if (i>0){
                        Toast.makeText(Select_image_in_notes.this, "image saved in db!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("11111", "onClick: elsse");

                        Toast.makeText(Select_image_in_notes.this, "image saved in db!", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    textView_notes.setError("!");
                    Toast.makeText(Select_image_in_notes.this, "Choose An Image!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
            }
        });
    }


    private void linkComponents(){
        imageq = (ImageView) findViewById(R.id.image_for_choose_image_id);
        btn_ok = (Button) findViewById(R.id.ok_btn_id_choose_image);
        btn_back = (Button) findViewById(R.id.back_btn_id_choose_image);
        textView_notes = (TextView) findViewById(R.id.textview_to_check_image_choosen_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        helper.handleResult(requestCode, resultCode, result);  // call this helper class method
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        helper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }
}
