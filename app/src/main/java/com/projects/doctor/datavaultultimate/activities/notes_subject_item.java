package com.projects.doctor.datavaultultimate.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabase;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabaseTable;
import com.projects.doctor.datavaultultimate.databases.Subject_Notes_DatabaseTable;
import com.projects.doctor.datavaultultimate.adapters.C_Adapter_Sub_item_Note;
import com.projects.doctor.datavaultultimate.alerts.Set_Reminder;
import com.projects.doctor.datavaultultimate.helpers.SetColorLatest;
import com.projects.doctor.datavaultultimate.pojos.MyPojo_Sub_Item_note;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by doctor on 06-07-2017.
 *
 * this is for showing the notes of subjects listed on subject gridview
 *
 */

public class notes_subject_item extends AppCompatActivity {

    private MyPojo_Sub_Item_note myPojo;
    private ArrayList<MyPojo_Sub_Item_note> arrayList = new ArrayList<>();
   private C_Adapter_Sub_item_Note myAdapter;

    //mainActivity components
    private ListView listView;
    private EditText editText_note;
    private TextInputLayout textInputLayout_notes;
    private ImageView back_add,image;
    private Button btn_date, btn_time;



    //enter note popup
    private AlertDialog dialog_enter_note_popup ;

    //new addition floating action button
    private FloatingActionButton fab ;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private AlertDialog alertDialog;

    private String table;
    private int position_;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_subject_item);
        table = getIntent().getStringExtra("table_ka_naam");
        linkComponents();
        setListeners();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };
        actionBar.addTab(actionBar.newTab().setText(table).setTabListener(tabListener));

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        //select that button which color is already choosen on previous use
        switch (sharedPreferences.getString("color","blue")) {
            case "red":
                redColor();
                break;
            case "blue":
                blueColor();
                break;
            case "green":
                greenColor();
                break;
            default:
                break;
        }

        //very imp this is to accept spaces and shit
        table = "'"+table+"'";

        readTheData();

        myAdapter = new C_Adapter_Sub_item_Note(notes_subject_item.this, R.layout.notes_ind_listview_item, arrayList,listView);
        listView.setAdapter(myAdapter);

        setTimeOnBtn();
        setDateOnBtn();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTimeOnBtn();
        setDateOnBtn();
        readTheData();
        showTheNotes();
    }

    private void showTheNotes() {
        listView.setAdapter(myAdapter);
    }

    public void clickOnAddNote(View view) {

        if (editText_note.getText().toString().isEmpty()) {
            editText_note.setError("Enter a Note!");
            return;
        }
        addTheData();
        readTheData();
        //
        //onStart();
        myAdapter.notifyDataSetChanged();
    }

    private void addTheData(){

        SubjectDatabase mydb = new SubjectDatabase(notes_subject_item.this);
        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SubjectDatabaseTable.NAME_COL,editText_note.getText().toString());
        contentValues.put(SubjectDatabaseTable.DATE_COL,btn_date.getText().toString());
        contentValues.put(SubjectDatabaseTable.TIME_COL,btn_time.getText().toString());


        long c = Subject_Notes_DatabaseTable.insert(sqLiteDatabase,table,contentValues);

        if (c>0){
            editText_note.setText(null);

            Toast.makeText(this, "note Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void readTheData(){

        SQLiteDatabase db = new SubjectDatabase(this).getReadableDatabase();

        Cursor cursor = Subject_Notes_DatabaseTable.select(false,table,db,null,null,null,null);

        if (!(cursor.getCount()>=1)){
            return;
        }
        cursor.moveToFirst();
        arrayList.clear();
        do{
            myPojo = new MyPojo_Sub_Item_note();
            myPojo.setId(""+cursor.getInt(0));
            myPojo.setNote(cursor.getString(1));
            myPojo.setDate(cursor.getString(2));
            myPojo.setTime(cursor.getString(3));
            myPojo.setColor(cursor.getString(7));
            try {
                myPojo.setImageUri(cursor.getBlob(4));
            }
            catch (Exception e){
            }
            myPojo.setSubject_name(table);
            arrayList.add(myPojo);
        }while (cursor.moveToNext());
        cursor.close();

       // listView.setAdapter(myAdapter);
    }



    private void linkComponents(){

        //added on 7-8-17
        fab = (FloatingActionButton) findViewById(R.id.fab_popup_add_note_subject_item_id);

        image = (ImageView) findViewById(R.id.justforhelper_id);
        listView = (ListView) findViewById(R.id.sub_item_note_listview_id);
        editText_note = (EditText) findViewById(R.id.sub_item_note_edit_get_text_id);
        textInputLayout_notes = (TextInputLayout) findViewById(R.id.sub_item_note_edit_get_text_layout_id);

        back_add = (ImageView) findViewById(R.id.sub_item_note_btn_save_id);
       // back_theme =(ImageView) findViewById(R.id.subite);
        btn_date = (Button) findViewById(R.id.sub_item_note_btn_date_chooser_id);
        btn_time = (Button) findViewById(R.id.sub_item_note_btn_time_chooser_id);

    }

    private void setListeners(){
/*
        back_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnThemeChooser(v);
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // gotoNote_itemOptions(position);
                gotoNotes_Info(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gotoNote_itemOptions(position);
                return true;
            }
        });

        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnAddNote(v);
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_chooser(v);
            }
        });
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_chooser(v);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotePopUp();
            }
        });
    }

    private void addNotePopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(notes_subject_item.this);
        View view = notes_subject_item.this.getLayoutInflater().inflate(R.layout.enter_note_popup_independent, null);
        builder.setView(view);

        //popup componenets linked here
        final Button btn_date_pop, btn_time_pop, btn_cancel_pop, btn_clear_pop ;
        ImageView btn_add_note;
        final EditText et_note_popup;
        final TextInputLayout et_layout_note_popup;

        et_layout_note_popup = (TextInputLayout) view.findViewById(R.id.edit_get_text_enter_note_popup_layout_id);
        et_note_popup = (EditText) view.findViewById(R.id.edit_get_text_enter_note_popup_id);
        btn_add_note = (ImageView) view.findViewById(R.id.btn_add_enter_note_popup_id);
        btn_date_pop = (Button) view.findViewById(R.id.btn_date_chooser_enter_note_popup_id);
        btn_clear_pop = (Button) view.findViewById(R.id.btn_clear_enter_note__popup_id);
        btn_time_pop = (Button) view.findViewById(R.id.btn_time_chooser_enter_note_popup_id);
        btn_cancel_pop = (Button) view.findViewById(R.id.btn_cance_enter_note_popup_id);

        btn_clear_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_note_popup.setText("");
                et_layout_note_popup.setError(null);
            }
        });

        btn_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notePopup = et_note_popup.getText().toString();
                if (notePopup.isEmpty()){
                    et_layout_note_popup.setError("Plz Enter A Note");
                    return;
                }

                SQLiteDatabase sqLiteDatabase = new SubjectDatabase(notes_subject_item.this).getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Subject_Notes_DatabaseTable.NAME_COL,notePopup);
                contentValues.put(Subject_Notes_DatabaseTable.DATE_COL,btn_date_pop.getText().toString());
                contentValues.put(Subject_Notes_DatabaseTable.TIME_COL,btn_time_pop.getText().toString());
                if ((Subject_Notes_DatabaseTable.insert(sqLiteDatabase, table, contentValues))<0){
                    Toast.makeText(notes_subject_item.this, "Error Storing Note", Toast.LENGTH_SHORT).show();
                    return;
                }
                Snackbar.make(v, "Added Note", Snackbar.LENGTH_SHORT).show();
                et_layout_note_popup.setError(null);
                et_note_popup.setText(null);
                String date_time[] = getCurrentDateTime().split("_");
                btn_date_pop.setText(date_time[0]);
                btn_time_pop.setText(date_time[1]);
                readTheData();
                myAdapter.notifyDataSetChanged();
            }
        });

        btn_cancel_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_enter_note_popup.dismiss();
            }
        });

        String date_time[] = getCurrentDateTime().split("_");
        btn_date_pop.setText(date_time[0]);
        btn_time_pop.setText(date_time[1]);

        dialog_enter_note_popup = builder.create();
        dialog_enter_note_popup.setCanceledOnTouchOutside(false);
        dialog_enter_note_popup.show();
    }


    private String getCurrentDateTime(){
        Calendar cal = Calendar.getInstance();
        return ""+ cal.get(cal.DAY_OF_MONTH) + "/" + (cal.get(cal.MONTH)+1) + "/" + cal.get(cal.YEAR)
                +"_"+cal.get(cal.HOUR_OF_DAY)+" : "+cal.get(cal.MINUTE);
    }

    private void gotoNote_itemOptions(final int position){
        position_ = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence sequence[] = {"open", "add image", "set Reminder","set color", "reset color", "cancel"};
        builder.setItems(sequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (sequence[which]=="open"){
                    gotoNotes_Info(position);
                }
                else if (sequence[which]=="add image"){
                    goTo_Choose_Image(position);
                }
                else if (sequence[which]=="cancel"){
                    alertDialog.dismiss();
                }
                else if (sequence[which]=="set Reminder"){
                    setReminder();
                }else if (sequence[which]=="set color"){
                    setColorOnItem(position_);
                }
                else if (sequence[which]=="reset color"){
                    resetColorOnItem(position_);
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void resetColorOnItem(int pos) {
        addColorToDb(pos, -686659797);
        //findDataBaseParameters();
        readTheData();
        myAdapter.notifyDataSetChanged();
    }

    private void setColorOnItem(final int pos) {
        ColorPickerDialogBuilder.with(this).setTitle("Choose color")
                .initialColor(SetColorLatest.getColorVal(notes_subject_item.this))
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
                        SetColorLatest.setColorVal(notes_subject_item.this, selectedColor);
                        addColorToDb(pos, selectedColor);
                        // readTheData();
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

        SQLiteDatabase db = new SubjectDatabase(this).getWritableDatabase();
        String where = Subject_Notes_DatabaseTable.ID_COL+" = "+"'"+arrayList.get(pos).getId()+"'";
        ContentValues cv = new ContentValues();
        cv.put(Subject_Notes_DatabaseTable.COLOR_COL, colorChoosen);
        Log.d("123321", "data "+arrayList.get(pos).getId()+" - "+table+" pos - "+pos);
        Subject_Notes_DatabaseTable.update(db, table, cv, where, null);
        readTheData();
        // myAdapter.notifyDataSetChanged();
        //findDataBaseParameters();
    }

    private void setReminder() {
        String title = arrayList.get(position_).getNote();

        Set_Reminder rem = new Set_Reminder(this, R.layout.activity_reminder__screen, title);

    }

    private void goTo_Choose_Image(int position){
        Intent intent = new Intent(this,Select_Image_Note_item.class);
        intent.putExtra("id",arrayList.get(position).getId());
        intent.putExtra("sub",arrayList.get(position).getSubject_name());
        startActivity(intent);
    }

    private void gotoNotes_Info(int position){
        String note_id = arrayList.get(position).getId();
        String note_sub = arrayList.get(position).getSubject_name();
        Intent i = new Intent(getApplicationContext(),notes_item_info_activity.class);
        i.putExtra("id",note_id);
        i.putExtra("subject",note_sub);
        i.putExtra("image_array",arrayList.get(position).getImageUri());
        startActivity(i);
    }


    public void time_chooser(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        hour = calendar.get(calendar.HOUR);
        minute = calendar.get(calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(notes_subject_item.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btn_time.setText(null);
                btn_time.setText("" + hourOfDay + " : " + minute);
            }
        }, hour, minute, true);
        timePickerDialog.updateTime(hour,minute);
        timePickerDialog.show();
    }

    private void setTimeOnBtn(){
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        hour = calendar.get(calendar.HOUR_OF_DAY);
        minute = calendar.get(calendar.MINUTE);
        btn_time.setText("" + hour + " : " + minute);
    }

    public void date_chooser(View view) {
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(notes_subject_item.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btn_date.setText(null);
                btn_date.setText("" + dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.updateDate(year,month,day);
        datePickerDialog.show();
    }

    private void setDateOnBtn(){
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);
        btn_date.setText("" + day + "/" + (month+1) + "/" + year);
    }

    public void redColor(){
        String red = "#94bf0f0f";
        btn_time.setBackgroundColor(Color.parseColor(red));
        btn_date.setBackgroundColor(Color.parseColor(red));
        listView.setBackgroundColor(Color.parseColor(red));
    //    back_theme.setBackgroundColor(Color.parseColor(red));
        back_add.setBackgroundColor(Color.parseColor(red));
    }

    public void blueColor(){
        //set as the primary color
        String blue = "#3F51B5";
        btn_time.setBackgroundColor(Color.parseColor(blue));
        btn_date.setBackgroundColor(Color.parseColor(blue));
       // listView.setBackgroundColor(Color.parseColor(blue));
      //  back_theme.setBackgroundColor(Color.parseColor(blue));
        back_add.setBackgroundColor(Color.parseColor(blue));
    }

    public void greenColor(){
        String green ="#000000";
        btn_time.setBackgroundColor(Color.parseColor(green));
        btn_date.setBackgroundColor(Color.parseColor(green));
        listView.setBackgroundColor(Color.parseColor(green));
       // back_theme.setBackgroundColor(Color.parseColor(green));
        back_add.setBackgroundColor(Color.parseColor(green));
    }
}
