package com.projects.doctor.datavaultultimate.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.projects.doctor.datavaultultimate.alerts.alert_arrangeBy;
import com.projects.doctor.datavaultultimate.helpers.SetColorLatest;
import com.projects.doctor.datavaultultimate.pojos.MyPojo_Subject;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.activities.Select_Image;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabase;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabaseTable;
import com.projects.doctor.datavaultultimate.adapters.C_Adapter_Grid_Subjects;
import com.projects.doctor.datavaultultimate.activities.notes_subject_item;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by doctor on 06-07-2017.
 *
 * contains the gridview of the availible entries
 */

public class Frag_Subject_main extends Fragment {

    private ArrayList<MyPojo_Subject> arr = new ArrayList<>();
    private GridView gridView;
    private AlertDialog alertDialog;
    private EditText subject_name_edit;
    private TextInputLayout  subjeect_name_edit_parent;
    Button btn_ok,btn_cancel;

    private C_Adapter_Grid_Subjects myAdapter;

    //to grab the position of clicked or long clicked item
    //and perform the required operations
    int position1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_subject_main,null);

        linkComponents(view);
        //checks for prevdata and puts them on the gridview
        startNoteActivity();
        setListner();

        return view;
     }

    @Override
    public void onStart() {
        super.onStart();
        startNoteActivity();
    }

    private void setListner(){

         gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 position1=position;
                 if(position==0){
                     //Toast.makeText(getActivity(), "used to add more subjects", Toast.LENGTH_SHORT).show();
                     createAlertAddSubject();
                 }
                 else{
                     openSubjectOptions(position);
                 }
                 return true;
             }
         });

         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 position1=position;
                 if(position==0){
                     createAlertAddSubject();
                 }
                 else{
                     //openSubjectOptions(position);
                     openSubjectTable(position);
                 }
             }
         });
     }

     private void openSubjectOptions(final int pos){

         final CharSequence c[] = { "open", "set icon", "set color", "reset icon", "reset color", "delete"};
         final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

         builder.setItems(c, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 if (c[which]=="open"){
                     openSubjectTable(pos);
                 }
                 else if (c[which]=="set icon"){
                     setIconForSubject();
                     //Snackbar.make(view, "Icon Set Successfully",Snackbar.LENGTH_SHORT).show();
                 }
                 else if (c[which]=="set color"){
                     setColorOnItem(pos);
                 }
                 else if (c[which]=="reset color"){
                     resetColorOnItem(pos);
                 }
                 else if (c[which]=="delete"){
                     deleteTable();
                     //Snackbar.make(view, "Entry Deleted Successfully",Snackbar.LENGTH_SHORT).show();
                 }else if (c[which]=="reset icon"){
                     reset();
                    // Snackbar.make(view, "Icon Reset done",Snackbar.LENGTH_SHORT).show();
                 }
             }
         });

         builder.show();
     }

    private void resetColorOnItem(int pos) {
        addColorToDb(pos, -686659797);
        findDataBaseParameters();
        myAdapter.notifyDataSetChanged();
    }

    private void setColorOnItem(final int pos) {
        ColorPickerDialogBuilder.with(getActivity()).setTitle("Choose color")
                .initialColor(SetColorLatest.getColorVal(getActivity()))
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //changeBackgroundColor(selectedColor);
                        Log.d("123321", "elected color "+selectedColor);
                        SetColorLatest.setColorVal(getActivity(), selectedColor);
                        addColorToDb(pos, selectedColor);
                        findDataBaseParameters();
                        myAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void addColorToDb(int pos, int colorChoosen) {

        SQLiteDatabase db = new SubjectDatabase(getActivity()).getWritableDatabase();
        String where = SubjectDatabaseTable.ID_COL+" = "+"'"+arr.get(pos).getId()+"'";
        ContentValues cv = new ContentValues();
        cv.put(SubjectDatabaseTable.COLOR_COL, colorChoosen);
        SubjectDatabaseTable.update(db, cv, where, null);
        findDataBaseParameters();
    }

    private void reset(){
         SQLiteDatabase db = new SubjectDatabase(getActivity()).getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(SubjectDatabaseTable.IMAGE_COL, (byte[]) null);

         String w = SubjectDatabaseTable.ID_COL+" = "+"'"+arr.get(position1).getId()+"'";

         Long i = SubjectDatabaseTable.update(db,cv,w,null);
         if (i>0){
         }
         else {
             return;
         }
         onStart();
     }

     private void setIconForSubject() {
         Intent i = new Intent(getContext(),Select_Image.class);
         i.putExtra("id_delete_",arr.get(position1).getId());
         i.putExtra("id_delete_2",arr.get(position1).getName());
         startActivity(i);
     }

     private void deleteTable(){

         AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
         View v = getActivity().getLayoutInflater().inflate(R.layout.confirm_delete_table,null);
         b.setView(v);

         final AlertDialog alert = b.create();

         alert.show();

         final CheckBox check;
         Button btn_confim,btn_back;
         check = (CheckBox) v.findViewById(R.id.check_alert_delete_id);
         btn_confim = (Button) v.findViewById(R.id.confirm_btn_id_confirmation);
         btn_back = (Button) v.findViewById(R.id.back_btn_id_confirmation);

         btn_confim.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v) {
                 if (check.isChecked()){
                     SQLiteDatabase db = new SubjectDatabase(getActivity()).getWritableDatabase();
                     String id = arr.get(position1).getId();
                     String subject = arr.get(position1).getName();
                     String where = SubjectDatabaseTable.ID_COL+" = "+"'"+id+"'";
                     SubjectDatabaseTable.delete(db,where,null);
                     db.execSQL("DROP TABLE IF EXISTS "+"'"+subject+"'");
                     findDataBaseParameters();
                     myAdapter.notifyDataSetChanged();
                     alert.dismiss();
                 }
                 else {
                     check.setError("check to confirm");
                 }
             }
         });

         btn_back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 alert.dismiss();
             }
         });

     }

     private void openSubjectTable(int pos){
         MyPojo_Subject my = (MyPojo_Subject) gridView.getAdapter().getItem(pos);
         Intent intent = new Intent(getContext(),notes_subject_item.class);
         intent.putExtra("table_ka_naam",my.getName());
         startActivity(intent);
     }

     private void createSubjectTable(String sub_name){

         SubjectDatabase subjectDatabase2 = new SubjectDatabase(getActivity());
         SQLiteDatabase db = subjectDatabase2.getWritableDatabase();

         ///////////////////

         //this creates the table for subject to handle notes' data ..
         String drop = "DROP TABLE IF EXISTS "+"'"+sub_name+"'";
         db.execSQL(drop);

         String qq = "CREATE TABLE `"+sub_name+"` (\n" +
                 "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                 "\t`name`\tTEXT,\n" +
                 "\t`date`\tTEXT,\n" +
                 "\t`time`\tTEXT,\n" +
                 "\t`image`\tTEXT,\n" +
                 "\t`sub_name`\tTEXT,\n" +
                 "\t`password`\tTEXT,\n" +
                 "\t`color`\tTEXT\n" +
                 ");";

         db.execSQL(qq);
         //////////////////////

         ContentValues cv=new ContentValues();
         Calendar cal = Calendar.getInstance();
         cv.put(SubjectDatabaseTable.NAME_COL,sub_name);
         cv.put(SubjectDatabaseTable.DATE_COL,""+cal.get(cal.DATE)+"/"+cal.get(cal.MONTH)+"/"+cal.get(cal.YEAR));
         cv.put(SubjectDatabaseTable.TIME_COL,""+cal.get(cal.HOUR)+" : "+cal.get(cal.MINUTE));
         long i = SubjectDatabaseTable.insert(db,cv);
         if (i>0){
             Toast.makeText(getActivity(), sub_name+" Entry created ", Toast.LENGTH_SHORT).show();
         }
         else {
         }
         subjectDatabase2.close();
     }

    private void linkComponents(View view){
        gridView = (GridView) view.findViewById(R.id.subjects_grid_id);
    }

    private void createAlertAddSubject(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_subject_alert,null);

        builder.setView(view);
        subject_name_edit=(EditText) view.findViewById(R.id.subject_name_alert_edit_text_id);
        subjeect_name_edit_parent=(TextInputLayout) view.findViewById(R.id.subject_name_alert_edit_text_layout_id);
        btn_ok=(Button) view.findViewById(R.id.subject_name_alert_ok_button_id);
        btn_cancel=(Button) view.findViewById(R.id.subject_name_alert_cancel_button_id);

        alertDialog=builder.create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String subject_name=subject_name_edit.getText().toString();

                if (subject_name.isEmpty()){
                    subjeect_name_edit_parent.setError("Please Enter a Name");
                    return;
                }

                if (subject_name.charAt(0) >= 'a' && subject_name.charAt(0) <='z'){
                    char start = subject_name.charAt(0);
                    // Log.d("123321", "start"+start);
                    start =(char) ((int)start - 32) ;
                    // Log.d("123321", "start"+((int) 'a'));
                    String new_sub = "" + start + subject_name.substring(1);
                    subject_name = new_sub;

                }

                if(checkSubjectName(subject_name)){
                    subjeect_name_edit_parent.setError("Entry With same name exists!");
                   return;
                }
                else{
                    createSubjectTable(subject_name);
                    startNoteActivity();
                    alertDialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private Boolean checkSubjectName( String sub_name){

        SubjectDatabase sbdb = new SubjectDatabase(getActivity());
        SQLiteDatabase sqldb = sbdb.getReadableDatabase();
        String where = SubjectDatabaseTable.NAME_COL+" = "+"'"+sub_name+"'";
        Cursor c = SubjectDatabaseTable.select(false,sqldb,null,where,null,null);

        if (c.getCount()>=1){
            sbdb.close();
            c.close();
            sqldb.close();
            return true;
        }
        else {
            sbdb.close();
            c.close();
            sqldb.close();
            return false;
        }
    }


    private void startNoteActivity(){
        findDataBaseParameters();
        setDataOnAdapter();
    }

    private void findDataBaseParameters(){
        SQLiteDatabase sqldb = new SubjectDatabase(getActivity()).getReadableDatabase();

        SharedPreferences preferences = getActivity().getSharedPreferences("arrangeBy_dataVault", Context.MODE_PRIVATE);

        String orderBy = preferences.getString(alert_arrangeBy.PREF_ARRANGEBY_STATUSORDERBY, "NULL");
        String orderByOrder = preferences.getString(alert_arrangeBy.PREF_ARRANGEBY_ORDERBY, "NULL");

        Cursor c = SubjectDatabaseTable.select(false,sqldb,null,null,null,orderBy+" "+orderByOrder);
        long numberOfSubjects = c.getCount();

        arr.clear();
        Calendar calendar = Calendar.getInstance();
        MyPojo_Subject my = new MyPojo_Subject();
/*
        my.setName("");
*/
        my.setNumberOfItems("Entries Added: "+numberOfSubjects);
        my.setDate(""+calendar.get(calendar.DATE)+"/"+calendar.get(calendar.MONTH)+"/"+calendar.get(calendar.YEAR));
        arr.add(my);

        if (numberOfSubjects>=1&&c.moveToFirst()){
            do{
                my = new MyPojo_Subject();
                my.setId(""+c.getInt(0));
                my.setName(c.getString(1));
                my.setDate(c.getString(2));
                my.setColor(c.getString(5));
               try {
                   my.setImageUri(c.getBlob(4));
               }
               catch (NullPointerException e){
                   e.printStackTrace();
               }
                my.setNumberOfItems("");
                arr.add(my);
            }while (c.moveToNext());
        }
    }

    private void setDataOnAdapter() {
        myAdapter = new C_Adapter_Grid_Subjects(getActivity(), R.layout.subject_item_grid_layout,arr);
        gridView.setAdapter(myAdapter);
    }
}

