package com.mapzip.ppang.mapzipproject.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.mapzip.ppang.mapzipproject.R;

import org.json.JSONArray;

/**
 * Created by ljs93kr on 2015-07-27.
 */
public class UserData {
    private static UserData ourInstance;

    private boolean LoginPermission;
    private String UserID;
    private String UserName;
    private String UserPW;
    private JSONArray mapmetaArray; // 홈화면 정보
    private JSONArray[] mapforpinArray; // 지도 핀 데이터
    private int[] mapforpinNum = {0, 0, 0, 0, 0}; // 지도별 리뷰정보 로딩여부 0: loding yet, 1: loding ok, 2: no review
    private int mapmetaNum; // 지도정보 수정 시 홈화면 리프레시
    private int[][] pingCount; //25개 지역별 핑 갯수(색 지정에 쓰임)
    private Bitmap[] result; //map
    private Bitmap[] GalImages = new Bitmap[]{
    };
    private boolean friendlock;
    private boolean reviewListlock;
    private MapData mapData;
    private boolean mapRefreshLock = true;

    // noimage
    private Bitmap noimage;
    private boolean checkNoimage = false;

    // auto_login
    private int isAuto;

    public static UserData getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserData();
        }
        return ourInstance;
    }

    private UserData() {
        init();
    }

    public void init() {
        LoginPermission = false; // ó�� instanceȭ �Ҷ��� �α����㰡 false
        UserID = null;
        UserName = null;
        mapmetaArray = new JSONArray();
        mapforpinArray = new JSONArray[5];
        for(int i =0; i<5; i++)
            mapforpinNum[i] = 0;
        mapmetaNum = 0;
        mapData = new MapData();
        pingCount = new int[5][26];
        result = new Bitmap[5];
        GalImages = new Bitmap[5];
        friendlock = true;

        //auto_login
        isAuto = -1;
    }

    //서버에서 리뷰 갯슈 받아오기(지역별 index는 구글드라이브 지도번호 -1 하면 됨)
    public void setReviewCount(int mapnum, int index, int num) { pingCount[mapnum][index] = num; }

    public Bitmap getResult(int mapnum) {
        Log.v("userdata","이미지가져오기");
        return result[mapnum]; }

    public void setMapImage(int mapnum, Resources res) {
        Log.v("userdata","이미지셋");

        BitmapDrawable bd;
        BitmapDrawable yd;

        BitmapDrawable bd3 = (BitmapDrawable) res.getDrawable(R.drawable.seoul2);
        Bitmap bit3 = bd3.getBitmap();

        result[mapnum] = bit3;

        for (int index = 1; index < pingCount[mapnum].length; index++) {
            boolean bitlock = true;
            bd = null;
            if (pingCount[mapnum][index] >= SystemMain.MAP_RED_NUM) {
                bitlock = false;
                switch (index) {
                    case 1:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p6);
                        break;
                    case 2:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p7);
                        break;
                    case 3:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p5);
                        break;
                    case 4:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p4);
                        break;
                    case 5:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p8);
                        break;
                    case 6:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p1);
                        break;
                    case 7:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p3);
                        break;
                    case 8:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p9);
                        break;
                    case 9:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p2);
                        break;
                    case 10:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p19);
                        break;
                    case 11:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p20);
                        break;
                    case 12:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p23);
                        break;
                    case 13:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p25);
                        break;
                    case 14:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p14);
                        break;
                    case 15:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p18);
                        break;
                    case 16:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p10);
                        break;
                    case 17:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p11);
                        break;
                    case 18:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p12);
                        break;
                    case 19:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p15);
                        break;
                    case 20:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p17);
                        break;
                    case 21:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p13);
                        break;
                    case 22:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p16);
                        break;
                    case 23:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p21);
                        break;
                    case 24:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p22);
                        break;
                    case 25:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.p24);
                        break;
                }
            } else if (pingCount[mapnum][index] >= SystemMain.MAP_YELLOW_NUM) {
                bitlock = false;
                switch (index) {
                    case 1:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y6);
                        break;
                    case 2:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y7);
                        break;
                    case 3:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y5);
                        break;
                    case 4:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y4);
                        break;
                    case 5:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y8);
                        break;
                    case 6:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y1);
                        break;
                    case 7:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y3);
                        break;
                    case 8:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y9);
                        break;
                    case 9:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y2);
                        break;
                    case 10:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y19);
                        break;
                    case 11:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y20);
                        break;
                    case 12:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y23);
                        break;
                    case 13:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y25);
                        break;
                    case 14:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y14);
                        break;
                    case 15:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y18);
                        break;
                    case 16:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y10);
                        break;
                    case 17:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y11);
                        break;
                    case 18:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y12);
                        break;
                    case 19:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y15);
                        break;
                    case 20:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y17);
                        break;
                    case 21:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y13);
                        break;
                    case 22:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y16);
                        break;
                    case 23:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y21);
                        break;
                    case 24:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y22);
                        break;
                    case 25:
                        bd = (BitmapDrawable) res.getDrawable(R.drawable.y24);
                        break;
                }
            }

            Log.v("bitlock",String.valueOf(bitlock));

            if (bitlock == false) {
                Log.v("userdata","이미지셋에서 합치기");
                result[mapnum] = combineImage(result[mapnum], bd.getBitmap(), mapnum);
            }
        }
    }

    public Bitmap combineImage(Bitmap bmp1, Bitmap bmp2, int mapnum) {
        Log.v("userdata","이미지합치기");

        int x = result[mapnum].getWidth();
        int y = result[mapnum].getHeight();

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

    public void LoginOK() {
        LoginPermission = true;
    }

    public boolean getLoginPermission() {
        return LoginPermission;
    }

    public void inputID(String id) {
        UserID = id;
    }

    public void inputName(String name) {
        UserName = name;
    }

    public void inputPW(String pw){
        UserPW = pw;
    }

    public String getUserID() {
        return UserID;
    }

    public void setPingCount(int[][] ping) { pingCount = ping; }

    public int getPingCount(int mapnum, int gunum){ return pingCount[mapnum][gunum]; }

    public String getUserName() {
        return UserName;
    }

    public String getUserPW(){
        return UserPW;
    }

    public JSONArray getMapmetaArray() {
        return mapmetaArray;
    }

    public JSONArray getMapforpinArray(int mapid) {
        return mapforpinArray[mapid];
    }

    public boolean getReviewListlock() {
        return reviewListlock;
    }

    public boolean getfriendlock() {
        return friendlock;
    }

    public void setReviewListlock(boolean rlock) {
        reviewListlock = rlock;
    }

    public void setfriendlock(boolean rlock) {
        friendlock = rlock;
    }

    public void setMapmetaArray(JSONArray jarray) {
        mapmetaArray = jarray;
    }

    public void setMapforpinArray(JSONArray jarray, int mapid) {
        mapforpinArray[mapid] = jarray;
    }

    public void setMapmetaNum(int i) {
        mapmetaNum = i;
    }

    public int getMapmetaNum() {
        return mapmetaNum;
    }

    public void setMapforpinNum(int mapid, int i) {
        mapforpinNum[mapid] = i;
    }

    public int getMapforpinNum(int mapid) {
        return mapforpinNum[mapid];
    }

    public Bitmap[] getGalImages() {
        return GalImages;
    }

    public void inputGalImages(Bitmap[] Images) {
        GalImages = Images;
    }

    public void initMapData() {
        mapData = new MapData();
    }

    public void setMapData(String s_id, String m_id, String s_contact, String r_text, String r_emotion, String s_address, String s_name, String g_num) {
        mapData.setStore_id(s_id);
        mapData.setMapid(m_id);
        mapData.setStore_contact(s_contact);
        mapData.setReview_text(r_text);
        mapData.setReview_emotion(Integer.parseInt(r_emotion));
        mapData.setStore_address(s_address);
        mapData.setStore_name(s_name);
        mapData.setGu_num(Integer.parseInt(g_num));
    }

    public MapData getMapData() {
        return mapData;
    }

   public void setIsAuto(int isAuto){
       this.isAuto = isAuto;
   }

    public int getIsAuto(){
        return isAuto;
    }

    public Bitmap getNoimage() {
        return noimage;
    }

    public void setNoimage(Bitmap noimage) {
        this.noimage = noimage;
    }

    public boolean isCheckNoimage() {
        return checkNoimage;
    }

    public void setCheckNoimage(boolean checkNoimage) {
        this.checkNoimage = checkNoimage;
    }

    public boolean isMapRefreshLock() {
        return mapRefreshLock;
    }

    public void setMapRefreshLock(boolean mapRefreshLock) {
        this.mapRefreshLock = mapRefreshLock;
    }
}
