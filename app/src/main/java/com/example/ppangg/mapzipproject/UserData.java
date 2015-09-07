package com.example.ppangg.mapzipproject;

import android.graphics.Bitmap;

import com.example.ppangg.mapzipproject.model.MapData;

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
    private MapData mapData;

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
        mapData = new MapData();
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

    public void initMapData(){ mapData = new MapData(); }

    public void setMapData(String s_id, String m_id, String s_contact, String r_text, String r_emotion, String s_address, String s_name){
        mapData.setStore_id(s_id);
        mapData.setMapid(m_id);
        mapData.setStore_contact(s_contact);
        mapData.setReview_text(r_text);
        mapData.setReview_emotion(Integer.parseInt(r_emotion));
        mapData.setStore_address(s_address);
        mapData.setStore_name(s_name);
    }

    public MapData getMapData(){ return mapData; }

}
