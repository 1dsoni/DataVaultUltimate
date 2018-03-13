package com.projects.doctor.datavaultultimate.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projects.doctor.datavaultultimate.databases.SubjectDatabase;
import com.projects.doctor.datavaultultimate.databases.Subject_Notes_DatabaseTable;
import com.projects.doctor.datavaultultimate.pojos.MyPojo_Subject;
import com.projects.doctor.datavaultultimate.R;
import com.projects.doctor.datavaultultimate.helpers.Utils;

import java.util.ArrayList;

/**
 * Created by doctor on 06-07-2017.
 */

public class C_Adapter_Grid_Subjects extends ArrayAdapter<MyPojo_Subject> {

    private Context context;
    private int resource;
    private ArrayList<MyPojo_Subject> arrayList;
    MyPojo_Subject my;

    private TextView sub_name,sub_count,sub_date;
    private ImageView icon, imageView_moreOptions;
    private LinearLayout linearLayout;
    private int position=0;

    public C_Adapter_Grid_Subjects(@NonNull Context context, @LayoutRes int resource, ArrayList<MyPojo_Subject> arrayList) {
        super(context, resource, arrayList);

        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        this.position = position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (position==0){
            view = inflater.inflate(R.layout.add_sub_grid_item,null);
        }
        else {
            view = inflater.inflate(resource,null);
        }
        linkComponents(view);

        if (position==0){
            my = arrayList.get(position);
            sub_name.setText("Add More Entries");
            sub_date.setText(my.getDate());
            sub_count.setText(arrayList.get(position).getNumberOfItems());
        }
        else {
            setDataOnComponents(position);
        }
        return view;
    }


    private void linkComponents(View view){
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_grid_view_item_sub_entry_id);
        imageView_moreOptions = (ImageView) view.findViewById(R.id.more_options_grid_item_menu_id);
        icon = (ImageView) view.findViewById(R.id.subject_icon_image_id);
        sub_name = (TextView) view.findViewById(R.id.sub_name_grid_item_id);
        sub_count = (TextView) view.findViewById(R.id.sub_count_grid_item_id);
        sub_date = (TextView) view.findViewById(R.id.sub_date_grid_item_id);
    }

    private void setDataOnComponents(int pos){
        my = arrayList.get(pos);

        try {
            icon.setImageBitmap(Utils.getImage(my.getImageUri()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        linearLayout.setBackgroundColor(my.getColor());
        sub_name.setText(my.getName());
        sub_date.setText(my.getDate());
        sub_count.setText(countEntryItems(pos));
    }

    private String countEntryItems(int position){
        SQLiteDatabase db = new SubjectDatabase(getContext()).getReadableDatabase();
        return "Notes added : "+ Subject_Notes_DatabaseTable.select(false,"'"+arrayList.get(position).getName()+"'",db,null,null,null,null).getCount();
    }
}
