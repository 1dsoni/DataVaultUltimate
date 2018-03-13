package com.projects.doctor.datavaultultimate.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabase;
import com.projects.doctor.datavaultultimate.databases.SubjectDatabaseTable;
import com.projects.doctor.datavaultultimate.helpers.SetColorLatest;
import com.projects.doctor.datavaultultimate.pojos.MyPojo;
import com.projects.doctor.datavaultultimate.databases.Mydb;
import com.projects.doctor.datavaultultimate.databases.Notes_Storage;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.helpers.SelectImageHelper;
import com.projects.doctor.datavaultultimate.activities.Select_image_in_notes;
import com.projects.doctor.datavaultultimate.databases.Subject_Notes_DatabaseTable;
import com.projects.doctor.datavaultultimate.activities.ind_notes_item_info_activity;
import com.projects.doctor.datavaultultimate.adapters.MyAdapter;
import com.projects.doctor.datavaultultimate.alerts.Set_Reminder;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by doctor on 06-07-2017.
 *
 * it is for instant notes
 */

public class Frag_Notes_subject extends Fragment {

    MyPojo myPojo;
    ArrayList<MyPojo> arrayList = new ArrayList<>();
    MyAdapter myAdapter;

    //mainActivity components
    ListView listView;
    EditText editText_note;
    TextInputLayout textInputLayout_notes;
    ImageView back_theme,back_add,default_image;
    Button btn_date, btn_time;

    //new addition floating action button
    private FloatingActionButton fab ;

    //enter note popup
    private AlertDialog dialog_enter_note_popup ;


    //theme-chooser(alert dialog) components
    Boolean timeStatus=true,dateStatus=true;
    AlertDialog alertDialog,alertDialog_options,alertDialog_choose_image;
    RadioGroup radioGroup_color;
    RadioButton radioButton_red, radioButton_green, radioButton_blue;
    ImageView button_ok;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SelectImageHelper helper;
    private int position_item;

    AppCompatActivity ac;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notes_independent,null);
        ac = (AppCompatActivity) getActivity();
        linkComponents(view);
        setListeners();

/*
        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
*/

        sharedPreferences = getActivity().getSharedPreferences("MyPref",getActivity().MODE_PRIVATE);
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

        Mydb mydb = new Mydb(getActivity());
        SQLiteDatabase db = mydb.getReadableDatabase();
        Cursor c = Notes_Storage.select(db,null);
        if (c.getCount()>=1){
            readTheData();
            showThenotes();
        }
        setTimeOnBtn();
        setDateOnBtn();

        myAdapter = new MyAdapter(getActivity(), R.layout.notes_ind_listview_item, arrayList,listView);
        listView.setAdapter(myAdapter);

        return view;
    }


    private void linkComponents(View view){
        //added on 1-08-17
        fab = (FloatingActionButton) view.findViewById(R.id.fab_popup_add_note_frag_notes_ind_id);

        listView = (ListView) view.findViewById(R.id.listview_id);

        default_image = (ImageView) view.findViewById(R.id.default_image);
        editText_note = (EditText) view.findViewById(R.id.edit_get_text_id);
        textInputLayout_notes = (TextInputLayout) view.findViewById(R.id.edit_get_text_layout_id);

        back_add = (ImageView) view.findViewById(R.id.btn_add_back_id);
        back_theme =(ImageView) view.findViewById(R.id.btn_theme_back_id);
        btn_date = (Button) view.findViewById(R.id.btn_date_chooser_id);
        btn_time = (Button) view.findViewById(R.id.btn_time_chooser_id);

    }

    private void setListeners(){
        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnAddNote();
            }
        });

        back_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnThemeChooser(v);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position_item = position;
                //openOptions();
                Intent i = new Intent(getContext(),ind_notes_item_info_activity.class);
                i.putExtra("id",arrayList.get(position_item).getId());
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                position_item = position;
                openOptions();
                return true;
            }
        });

        //added on 1-8-17
        //to show popup to add note
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.enter_note_popup_independent, null);
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

                        Mydb mydb = new Mydb(getActivity());
                        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Subject_Notes_DatabaseTable.NAME_COL,notePopup);
                        contentValues.put(Subject_Notes_DatabaseTable.DATE_COL,btn_date_pop.getText().toString());
                        contentValues.put(Subject_Notes_DatabaseTable.TIME_COL,btn_time_pop.getText().toString());
                        if (!check(Notes_Storage.insertIntoDb(sqLiteDatabase,contentValues))){
                            Toast.makeText(getActivity(), "Error Storing Note", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Snackbar.make(v, "Added Note", Snackbar.LENGTH_SHORT).show();
                        et_note_popup.setText(null);
                        et_layout_note_popup.setError(null);
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
        });
    }

    private String getCurrentDateTime(){
        Calendar cal = Calendar.getInstance();
        return ""+ cal.get(cal.DAY_OF_MONTH) + "/" + (cal.get(cal.MONTH)+1) + "/" + cal.get(cal.YEAR)
                +"_"+cal.get(cal.HOUR_OF_DAY)+" : "+cal.get(cal.MINUTE);
    }

    private void openOptions(){
        final CharSequence[] menu = {"open", "add image", "set Reminder","set color", "reset color", "cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (menu[which]=="open"){
                    Intent i = new Intent(getContext(),ind_notes_item_info_activity.class);
                    i.putExtra("id",arrayList.get(position_item).getId());
                    startActivity(i);
                }
                else if (menu[which]=="add image"){
                    Intent i = new Intent(getContext(),Select_image_in_notes.class);
                    i.putExtra("id",arrayList.get(position_item).getId());
                    startActivity(i);
                }
                else if (menu[which]=="set Reminder"){
                    setReminder();
                }
                else if (menu[which]=="set color"){
                    setColorOnItem(position_item);
                }
                else if (menu[which]=="reset color"){
                    resetColorOnItem(position_item);
                }
                else if (menu[which]=="cancel"){
                    alertDialog_options.dismiss();
                }
            }
        });

        alertDialog_options = builder.create();
        alertDialog_options.show();
    }


    private void resetColorOnItem(int pos) {
        addColorToDb(pos, -686659797);
        //findDataBaseParameters();
        readTheData();
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

        SQLiteDatabase db = new Mydb(getActivity()).getWritableDatabase();
        String where = Notes_Storage.ID_COl+" = "+"'"+arrayList.get(pos).getId()+"'";
        ContentValues cv = new ContentValues();
        cv.put(Notes_Storage.COLOR_COL, colorChoosen);
        Notes_Storage.update(db, cv, where, null);
        readTheData();
       // myAdapter.notifyDataSetChanged();
        //findDataBaseParameters();
    }

    private void setReminder() {
        String title = arrayList.get(position_item).getNote();

        Set_Reminder rem = new Set_Reminder(getActivity(), R.layout.activity_reminder__screen, title);
    }

    public void time_chooser(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        hour = calendar.get(calendar.HOUR_OF_DAY);
        minute = calendar.get(calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btn_time.setText(null);
                btn_time.setText("" + hourOfDay + " : " + minute);
                timeStatus=true;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btn_date.setText(null);
                btn_date.setText("" + dayOfMonth + "/" + (month+1) + "/" + year);
                dateStatus=true;
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

    public void clickOnAddNote() {
        if (editText_note.getText().toString().isEmpty()) {
            textInputLayout_notes.setError("Enter a Note!");
            Toast.makeText(getActivity(),"Enter a Note!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!dateStatus){
            Toast.makeText(getActivity(),"plz choose date!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!timeStatus){
            Toast.makeText(getActivity(),"plz choose time!",Toast.LENGTH_SHORT).show();
            return;
        }

        addTheData();
        readTheData();
        showThenotes();
    }

    private void showThenotes(){
        editText_note.setText(null);
        textInputLayout_notes.setHint("Enter note here");
        setDateOnBtn();
        setTimeOnBtn();
        listView.setAdapter(myAdapter);
    }

    private void addTheData(){
        Mydb mydb = new Mydb(getActivity());
        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subject_Notes_DatabaseTable.NAME_COL,editText_note.getText().toString());
        contentValues.put(Subject_Notes_DatabaseTable.DATE_COL,btn_date.getText().toString());
        contentValues.put(Subject_Notes_DatabaseTable.TIME_COL,btn_time.getText().toString());
        if (!check(Notes_Storage.insertIntoDb(sqLiteDatabase,contentValues))){
        }
    }

    public void readTheData(){
        Mydb mydb1 = new Mydb(getActivity());
        SQLiteDatabase sqLiteDatabase = mydb1.getReadableDatabase();
        Cursor cursor = Notes_Storage.select(sqLiteDatabase,null);
        if (!(cursor.getCount()>=1)){
            return;
        }
        arrayList.clear();
        while (cursor.moveToNext()){
            myPojo = new MyPojo();
            myPojo.setId(""+cursor.getInt(0));
            myPojo.setNote(cursor.getString(1));
            myPojo.setDate(cursor.getString(2));
            myPojo.setTime(cursor.getString(3));
            myPojo.setColor(cursor.getString(5));
            arrayList.add(myPojo);
        }
        cursor.close();
    }

    public Boolean check(long i){
        if (i>0){
            return true;
        }
        else {
            return false;
        }
    }

    public void clickOnThemeChooser(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.activity_theme, null, false);

        radioGroup_color = (RadioGroup) view1.findViewById(R.id.color_picker_id);
        radioButton_blue = (RadioButton) view1.findViewById(R.id.blue_id);
        radioButton_green = (RadioButton) view1.findViewById(R.id.green_id);
        radioButton_red = (RadioButton) view1.findViewById(R.id.red_id);
        button_ok = (ImageView) view1.findViewById(R.id.btn_ok_color_id);

        sharedPreferences = getActivity().getSharedPreferences("MyPref",getActivity().MODE_PRIVATE);        //select that button which color is already choosen on previous use
        if (sharedPreferences.getBoolean("colorStatus", false)) {
            switch (sharedPreferences.getString("color",null)) {
                case "red":
                    radioButton_red.setChecked(true);
                    break;
                case "blue":
                    radioButton_blue.setChecked(true);
                    break;
                case "green":
                    radioButton_green.setChecked(true);
                    break;
                default:
                    break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view1);
        alertDialog = builder.create();
        alertDialog.show();

        editor = sharedPreferences.edit();

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (radioGroup_color.getCheckedRadioButtonId()) {
                    case R.id.red_id:
                        editor.putString("color", "red");
                        redColor();
                        break;
                    case R.id.blue_id:
                        editor.putString("color", "blue");
                        blueColor();
                        break;
                    case R.id.green_id:
                        editor.putString("color", "green");
                        greenColor();
                        break;
                }
                editor.putBoolean("colorStatus", true);
                editor.apply();
                alertDialog.dismiss();
            }
        });
    }

    public void redColor(){
        String red = "#94bf0f0f";
        btn_time.setBackgroundColor(Color.parseColor(red));
        btn_date.setBackgroundColor(Color.parseColor(red));
        listView.setBackgroundColor(Color.parseColor(red));
        back_theme.setBackgroundColor(Color.parseColor(red));
        back_add.setBackgroundColor(Color.parseColor(red));
    }

    public void blueColor(){
        String blue = "#3F51B5";
        btn_time.setBackgroundColor(Color.parseColor(blue));
        btn_date.setBackgroundColor(Color.parseColor(blue));
       // listView.setBackgroundColor(Color.parseColor(blue));
        back_theme.setBackgroundColor(Color.parseColor(blue));
        back_add.setBackgroundColor(Color.parseColor(blue));
    }

    public void greenColor(){
        String green ="#000000";
        btn_time.setBackgroundColor(Color.parseColor(green));
        btn_date.setBackgroundColor(Color.parseColor(green));
        listView.setBackgroundColor(Color.parseColor(green));
        back_theme.setBackgroundColor(Color.parseColor(green));
        back_add.setBackgroundColor(Color.parseColor(green));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        helper.handleResult(requestCode, resultCode, result);  // call this helper class method
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        helper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }

}
