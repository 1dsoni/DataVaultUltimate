package com.projects.doctor.datavaultultimate.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.doctor.datavaultultimate.pojos.MyPojo_Sub_Item_note;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabase;
import com.projects.doctor.datavaultultimate.databases.Subject_Notes_DatabaseTable;

import java.util.ArrayList;

/**
 * Created by doctor on 28-06-2017.
 */

public class C_Adapter_Sub_item_Note extends ArrayAdapter<MyPojo_Sub_Item_note> {

    Context context;
    ArrayList<MyPojo_Sub_Item_note> arrayList;
    int resource;
    MyPojo_Sub_Item_note myPojo;

    LinearLayout linearLayout;
    TextView textView_date,textView_time;
    TextView textView_notes;
    ImageView imageView_delete_btn;
    ListView l;

    AlertDialog alertDialog;

    public C_Adapter_Sub_item_Note(@NonNull Context context, @LayoutRes int resource, ArrayList<MyPojo_Sub_Item_note> arrayList, ListView l) {
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
        /*//allows to show all of the text entered
        textView_notes.setSingleLine(false);*/

/*
        checkTheme();
*/

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
                        SQLiteDatabase db = new SubjectDatabase(context).getWritableDatabase();

                        String id = arrayList.get(position).getId();
                        String tablename = arrayList.get(position).getSubject_name();
                        String where = Subject_Notes_DatabaseTable.ID_COL+" = "+"'"+id+"'";
                        if (Subject_Notes_DatabaseTable.delete(db,tablename,where,null)>0){
                            Log.d("1111","deleted - ok");
                            db = new SubjectDatabase(context).getReadableDatabase();
                            Log.d("1111","222");
                            Cursor cursor = Subject_Notes_DatabaseTable.select(false,tablename,db,null,null,null,null);
                            Log.d("1111","333");
                            cursor.moveToFirst();
                            if (cursor.getCount()<1){
                                arrayList.clear();
                                l.setAdapter(l.getAdapter());
                                alertDialog.dismiss();
                                return;
                            }
                            arrayList.clear();
                            do{
                                myPojo = new MyPojo_Sub_Item_note();
                                myPojo.setId(""+cursor.getInt(0));
                                myPojo.setNote(cursor.getString(1));
                                myPojo.setDate(cursor.getString(2));
                                myPojo.setTime(cursor.getString(3));
                                myPojo.setImageUri(cursor.getBlob(4));
                                myPojo.setColor(cursor.getString(7));
                                myPojo.setSubject_name(tablename);
                                arrayList.add(myPojo);
                            }while (cursor.moveToNext());
                            cursor.close();
                        }
                        else {
                            return;
                        }

                        db.close();
                        /*
                        l.setAdapter(l.getAdapter());*/
                        ((C_Adapter_Sub_item_Note) l.getAdapter()).notifyDataSetChanged();

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
                recTheme();
                break;
            case "green":
                greenTheme();
                break;
            case "blue":
                blueTheme();
                break;
        }
    }

    private void recTheme(){
        String red = "#94bf0f0f";
        linearLayout.setBackgroundColor(Color.parseColor(red));
    }
    private void greenTheme(){
        String green = "#000";
        linearLayout.setBackgroundColor(Color.parseColor(green));
    }
    private void blueTheme(){
        String blue = "#3F51B5";
        linearLayout.setBackgroundColor(Color.parseColor(blue));
    }*/
}
