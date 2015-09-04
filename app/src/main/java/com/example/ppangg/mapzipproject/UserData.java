package com.example.ppangg.mapzipproject;

import android.graphics.Bitmap;

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
    private JSONArray[] mapforpinArray;
    private int[] mapforpinNum = {0,0,0,0,0};
    private int mapmetaNum;
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
        mapforpinArray = new JSONArray[5];
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

    public JSONArray getMapforpinArray(int mapid){ return mapforpinArray[mapid]; }

    public boolean getReviewListlock(){ return reviewListlock; }


    public void setReviewListlock(boolean rlock){ reviewListlock = rlock; }

    public void setMapmetaArray(JSONArray jarray){ mapmetaArray = jarray; }

    public void setMapforpinArray(JSONArray jarray, int mapid){ mapforpinArray[mapid] = jarray; }

    public void setMapmetaNum(int i){ mapmetaNum =i; }

    public int getMapmetaNum(){ return mapmetaNum; }

    public void setMapforpinNum(int mapid, int i){ mapforpinNum[mapid] =i; }

    public int getMapforpinNum(int mapid){ return mapforpinNum[mapid]; }

    public Bitmap[] getGalImages(){ return GalImages; };

    public void inputGalImages(Bitmap[] Images){ GalImages = Images; };


}
