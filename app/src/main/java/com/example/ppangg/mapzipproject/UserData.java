package com.example.ppangg.mapzipproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;

/**
 * Created by ljs93kr on 2015-07-27.

 */
public class UserData {
    private static UserData ourInstance;

    private boolean LoginPermission; // �α����㰡
    private String UserID; // ����� ���̵�
    private String UserName; // ����� �̸�
    private JSONArray mapmetaArray;
    private int testnum=0;
    private Bitmap[] GalImages = new Bitmap[] {
    };
    private boolean reviewListlock;

    public static UserData getInstance() {
        if(ourInstance == null) {
            ourInstance = new UserData();
        }
        return ourInstance;
    }

    private UserData() {
        init();
    }

    private void init(){
        LoginPermission = false; // ó�� instanceȭ �Ҷ��� �α����㰡 false
        UserID = null;
        UserName = null;
    }

    public void LoginOK(){
        LoginPermission = true;
    }

    public boolean getLoginPermission(){
        return LoginPermission;
    }

    public void inputID(String id){
        UserID = id;
    }

    public void inputName(String name){
        UserName = name;
    }

    public String getUserID(){
        return UserID;
    }

    public String getUserName(){
        return UserName;
    }

    public JSONArray getMapmetaArray(){ return mapmetaArray; }

    public boolean getReviewListlock(){ return reviewListlock; }


    public void setReviewListlock(boolean rlock){ reviewListlock = rlock; }

    public void setMapmetaArray(JSONArray jarray){ mapmetaArray = jarray; }

    public void inputTestnum(int i){ testnum=i; }

    public int getTestnum(){ return testnum; }

    public Bitmap[] getGalImages(){ return GalImages; };

    public void inputGalImages(Bitmap[] Images){ GalImages = Images; };


}
