package com.example.ppangg.mapzipproject.model;

/**
 * Created by ppangg on 2015-08-29.
 */
public class MapData {
    private String mapid;
    private int store_cx;
    private int store_cy;
    private double store_x;
    private double store_y;
    private String store_name;
    private String store_address;
    private String store_contact;
    private int review_emotion;
    private String review_text;
    private int gu_num;

    public MapData() { this.review_text = ""; }

    public String getMapid() { return mapid; }

    public int getStore_cx() { return store_cx; }

    public int getStore_cy() { return store_cy; }

    public double getStore_x() {
        return store_x;
    }

    public double getStore_y() {
        return store_y;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public String getStore_contact() {
        return store_contact;
    }

    public int getReview_emotion() {
        return review_emotion;
    }

    public String getReview_text() {
        return review_text;
    }

    public int getGu_num() { return gu_num; }

    public void setMapid(String mapid) {
        this.mapid = mapid;
    }

    public void setStore_x(double store_x) { this.store_x = store_x; }

    public void setStore_y(double store_y) {
        this.store_y = store_y;
    }

    public void setStore_cx(int store_cx) { this.store_cx = store_cx; }

    public void setStore_cy(int store_cy) {
        this.store_cy = store_cy;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public void setStore_contact(String store_contact) {
        this.store_contact = store_contact;
    }

    public void setReview_emotion(int review_emotion) {
        this.review_emotion = review_emotion;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public void setGu_num(int gu_num) { this.gu_num = gu_num; }

}
