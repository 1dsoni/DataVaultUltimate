package com.projects.doctor.datavaultultimate.pojos;

/**
 * Created by doctor on 07-07-2017.
 */

public class MyPojo_Sub_Item_note {
    private String note;
    private String date;
    private String time;
    private byte[] imageUri;
    private String subject_name;
    private String id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

}