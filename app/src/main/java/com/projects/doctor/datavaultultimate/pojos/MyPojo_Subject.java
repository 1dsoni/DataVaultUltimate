package com.projects.doctor.datavaultultimate.pojos;

import java.sql.Blob;

/**
 * Created by doctor on 06-07-2017.
 */

public class MyPojo_Subject {
    private String name;
    private String numberOfItems;
    private String date;
    private String id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
