package com.mapzip.ppang.mapzipproject.model;

import java.util.Map;

/**
 * Created by ppangg on 2015-08-29.
 */
public class MapData implements Cloneable {
    private String mapid;
    private String store_id;
    private double store_x;
    private double store_y;
    private String store_name;
    private String store_address;
    private String store_contact;
    private int review_emotion;
    private String review_text;
    private int gu_num;
    private int image_num;

    public MapData() { this.review_text = ""; }

    public String getMapid() { return mapid; }

    public String getStore_id() { return store_id; }

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

    public void setStore_id(String store_id) { this.store_id = store_id; }

    public void setMapid(String mapid) {
        this.mapid = mapid;
    }

    public void setStore_x(double store_x) { this.store_x = store_x; }

    public void setStore_y(double store_y) { this.store_y = store_y; }

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

    public int getImage_num() {
        return image_num;
    }

    public void setImage_num(int image_num) {
        this.image_num = image_num;
    }

    @Override
    public MapData clone() throws CloneNotSupportedException {
        MapData mapData = (MapData) super.clone();

        return mapData;
    }
}
