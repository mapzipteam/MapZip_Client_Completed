package com.example.ppangg.mapzipproject.model;

/**
 * Created by ppangg on 2015-08-29.
 */
public class MapData {
    private String mapid;
    private int store_x;
    private int store_y;
    private String store_name;
    private String store_address;
    private String store_contact;
    private int review_emotion;
    private String review_text;

    public MapData(){
        this.review_text = "";
    }

    public String getMapid(){
        return mapid;
    }

    public int getStore_x(){
        return store_x;
    }

    public int getStore_y(){
        return store_y;
    }

    public String getStore_name(){
        return store_name;
    }

    public String getStore_address(){
        return store_address;
    }

    public String getStore_contact(){
        return store_contact;
    }

    public int getReview_emotion(){
        return review_emotion;
    }

    public String getReview_text(){
        return review_text;
    }

    public void setMapid(String mapid){
        this.mapid = mapid;
    }

    public void setStore_x(int store_x){
        this.store_x = store_x;
    }

    public void setStore_y(int store_y){
        this.store_y = store_y;
    }

    public void setStore_name(String store_name){
        this.store_name = store_name;
    }

    public  void setStore_address(String store_address){
        this.store_address = store_address;
    }

    public void setStore_contact(String store_contact){
        this.store_contact = store_contact;
    }

    public void setReview_emotion(int review_emotion){
        this.review_emotion = review_emotion;
    }

    public void setReview_text(String review_text){
        this.review_text = review_text;
    }

}
