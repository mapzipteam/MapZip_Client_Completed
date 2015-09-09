package com.example.ppangg.mapzipproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

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
    private int[] pingCount = new int[25];; //25개 지역별 핑 갯수(색 지정에 쓰임)
    private Bitmap result; //map
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

    //서버에서 리뷰 갯슈 받아오기(지역별 index는 구글드라이브 지도번호 -1 하면 됨)
    public void setReviewCount(){
        for(int i = 0; i<25;i++) {
            pingCount[i] = 1;
        }
    }

    public Bitmap getResult() {return result;}

    public void setMapImage(Resources res) {

        BitmapDrawable bd;
        BitmapDrawable yd;

        BitmapDrawable bd3 = (BitmapDrawable) res.getDrawable(R.drawable.seoul2);
        Bitmap bit3 = bd3.getBitmap();

        result = bit3;

        for (int index = 0; index < pingCount.length; index++) {
            if (index == 0) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p1);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y1);
            } else if (index == 1) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p2);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y2);
            } else if (index == 2) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p3);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y3);
            } else if (index == 3) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p4);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y4);
            } else if (index == 4) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p5);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y5);
            } else if (index == 5) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p6);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y6);
            } else if (index == 6) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p7);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y7);
            } else if (index == 7) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p8);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y8);
            } else if (index == 8) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p9);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y9);
            } else if (index == 9) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p10);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y10);
            } else if (index == 10) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p11);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y11);
            } else if (index == 11) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p12);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y12);
            } else if (index == 12) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p13);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y13);
            } else if (index == 13) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p14);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y14);
            } else if (index == 14) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p15);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y15);
            } else if (index == 15) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p16);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y16);
            } else if (index == 16) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p17);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y17);
            } else if (index == 17) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p18);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y18);
            } else if (index == 18) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p19);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y19);
            } else if (index == 19) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p20);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y20);
            } else if (index == 20) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p21);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y21);
            } else if (index == 21) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p22);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y22);
            } else if (index == 22) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p23);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y23);
            } else if (index == 23) {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p24);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y24);
            } else {
                bd = (BitmapDrawable) res.getDrawable(R.drawable.p25);
                yd = (BitmapDrawable) res.getDrawable(R.drawable.y25);
            }

            if (pingCount[index] > 0 && pingCount[index] < 5) {
                result = combineImage(result, bd.getBitmap());
            } else if (pingCount[index] >= 5) {
                result = combineImage(result, yd.getBitmap());
            }
        }
    }

    public Bitmap combineImage(Bitmap bmp1, Bitmap bmp2)
    {
        int x = result.getWidth();
        int y = result.getHeight();

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        int w = bmp1.getWidth();
        int h = bmp1.getHeight();

        Rect src = new Rect(0, 0, w, h); //전체사이즈나오게 해줌
        Rect dst = new Rect(0, 0, x, y);//이 크기로 변경됨
        canvas.drawBitmap(bmp1, src, dst, null);
        canvas.drawBitmap(bmp2, src, dst, null);//bmp2, distanceLeft, distanceTop, null
        return bmOverlay;
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

    public void setPingCount(int[] ping) { pingCount = ping;}

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
