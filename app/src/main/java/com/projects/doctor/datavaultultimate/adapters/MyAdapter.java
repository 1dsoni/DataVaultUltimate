package com.projects.doctor.datavaultultimate.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.doctor.datavaultultimate.pojos.MyPojo;
import com.projects.doctor.datavaultultimate.databases.Mydb;
import com.projects.doctor.datavaultultimate.databases.Notes_Storage;
import com.projects.doctor.datavaultultimate.R;

import java.util.ArrayList;

/**
 * Created by doctor on 28-06-2017.
 * custom adapter for Independent notes
 */

public class MyAdapter extends ArrayAdapter<MyPojo> {

    Context context;
    ArrayList<MyPojo> arrayList;
    int resource;
    MyPojo myPojo;

    LinearLayout linearLayout;
    TextView textView_date,textView_time;
    TextView textView_notes;
    ImageView imageView_delete_btn;
    ListView l;

    AlertDialog alertDialog;

    public MyAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<MyPojo> arrayList, ListView l) {
        super(context, resource, arrayList);
        this.l=l;
        this.context = context;
        this.arrayList = arrayList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource,null,false);

        myPojo = arrayList.get(position);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_note_item_id);
        textView_date = (TextView) view.findViewById(R.id.date_textview_id);
        textView_notes = (TextView) view.findViewById(R.id.note_textview_id);
        textView_time = (TextView) view.findViewById(R.id.time_textview_id);
        imageView_delete_btn = (ImageView) view.findViewById(R.id.delete_btn_id);

/*
        checkTheme();
*/
/*
        //allows to show all of the text entered
        textView_notes.setSingleLine(false);*/
        imageView_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater1 = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                View view1 = layoutInflater1.inflate(R.layout.confirmation,null);
                builder.setView(view1);
                alertDialog = builder.create();
                alertDialog.show();

                Button btn_yes,btn_no;
                btn_yes = (Button) view1.findViewById(R.id.yes_btn_id_confirmation);
                btn_no = (Button) view1.findViewById(R.id.no_btn_id_delete_account);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Mydb mydb = new Mydb(context);
                        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
                        String whereClause = Notes_Storage.ID_COl+" = "+"'"+arrayList.get(position).getId()+"'";
                        if ((Notes_Storage.delete(sqLiteDatabase,whereClause)>0)){
                            Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show();
                            Mydb mydb1 = new Mydb(context);
                            SQLiteDatabase sqLiteDatabase2 = mydb1.getReadableDatabase();
                            Cursor cursor = Notes_Storage.select(sqLiteDatabase2,null);
                            cursor.moveToFirst();
                            if (cursor.getCount()<1){
                                arrayList.clear();
                                l.setAdapter(l.getAdapter());
                                alertDialog.dismiss();
                                return;
                            }
                            arrayList.clear();
                            do{
                                myPojo = new MyPojo();
                                myPojo.setId(""+cursor.getInt(0));
                                myPojo.setNote(cursor.getString(1));
                                myPojo.setDate(cursor.getString(2));
                                myPojo.setTime(cursor.getString(3));
                                myPojo.setColor(cursor.getString(5));
                                arrayList.add(myPojo);
                            }while (cursor.moveToNext());
                            cursor.close();
                        }
                        else {
                            Toast.makeText(context, "db_error", Toast.LENGTH_SHORT).show();
                        }
                        /*l.setAdapter(l.getAdapter());*/
                        ((MyAdapter) l.getAdapter()).notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        setListViewItems();

        return view;
    }
    private void setListViewItems(){
        linearLayout.setBackgroundColor(myPojo.getColor());
        textView_date.setText(myPojo.getDate());
        textView_time.setText(myPojo.getTime());
        textView_notes.setText(myPojo.getNote());
    }

    /*private void checkTheme(){
        SharedPreferences preferences = context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String color = preferences.getString("color","blue");
        switch (color){
            case "red":
                setColor("#94bf0f0f");
                break;
            case "green":
                setColor("#000");
                break;
            case "blue":
                setColor("#3F51B5");
                break;
        }
    }

    private void setColor(String color){
        linearLayout.setBackgroundColor(Color.parseColor(color));
    }*/
}
