package com.projects.doctor.datavaultultimate.pojos;

import java.sql.Blob;

/**
 * Created by doctor on 28-06-2017.
 */

public class MyPojo {
    private String date,time,note,id;
    private byte[] imageUri;
    private String color;

    public int getColor() {
        try{
            return Integer.parseInt(color);
        }
        catch (Exception e){
            return -686659797;
        }
    }

    public void setColor(String color) {
        this.color = color;
    }


    public byte[] getImageUri() {
        return imageUri;
    }

    public void setImageUri(byte[] imageUri) {
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
