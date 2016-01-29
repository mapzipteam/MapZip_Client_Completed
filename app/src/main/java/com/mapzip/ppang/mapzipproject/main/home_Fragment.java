package com.mapzip.ppang.mapzipproject.main;


import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.ScalableLayout.ScalableLayout;
import com.mapzip.ppang.mapzipproject.activity.map_setting;
import com.mapzip.ppang.mapzipproject.map.MapActivity;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
import com.mapzip.ppang.mapzipproject.map.Location;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class home_Fragment extends Fragment implements View.OnClickListener {

    private View v;
    private UserData user;

    // toast
    private View layout_toast;
    private TextView text_toast;

    private int fix_x = 0;
    private int fix_y = 0;

    private double loc_LNG = 0;
    private double loc_LAT = 0;

    private TextView topstate; // user info
    private ImageView imageview; // map image
    private TextView hashstate; // hashtag
    private String mapcurname = ""; // 현재 지도 이름
    private String mapkindnum; // 현재 지도 속성 번호
    private String mapid; // 현재 지도 pid값

    private ArrayList<String> sppinerList; // map name
    private Spinner spinner;
    private ArrayAdapter adapter;
    private int mapnum; // map num

    private Button mapsetting;

    public int realWidth; //ScreenSize width
    public int realHeight;//ScreenSize height

    // Seoul Btn
    private Button DoBong;
    private Button NoWon;
    private Button GangBuk;
    private Button SungBuk;
    private Button ZongRang;
    private Button EunPhung;
    private Button ZongRo;
    private Button DongDaeMon;
    private Button SuDaeMon;
    private Button Zhong;
    private Button SungDong;
    private Button GangZin;
    private Button GangDong;
    private Button MaPho;
    private Button YongSan;
    private Button GangSue;
    private Button YangChen;
    private Button GuRo;
    private Button YongDengPo;
    private Button DongJack;
    private Button GemChun;
    private Button GanAk;
    private Button SeoCho;
    private Button GangNam;
    private Button SongPa;

    private ScalableLayout scalableLayout;


    public home_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();
        getActivity().getActionBar().setTitle("     "+user.getUserName() + "의 지도");
        getActivity().invalidateOptionsMenu();
/*
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_title);
*/
        mapnum = user.getMapmetaArray().length();
        sppinerList = new ArrayList<String>();
        try {
            for (int i = 0; i < mapnum; i++) {
                sppinerList.add(user.getMapmetaArray().getJSONObject(i).getString("title"));
            }
        } catch (JSONException ex) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);

        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

      //  topstate = (TextView) v.findViewById(R.id.topstate);
      //  topstate.setText(user.getUserName());
       /*topstate.append(" (");
        topstate.append(user.getUserID());
        topstate.append(")");*/
      //  topstate.append("의 지도");


        scalableLayout = (ScalableLayout) v.findViewById(R.id.scalablelayout);


        imageview = (ImageView) v.findViewById(R.id.mapimage);
        hashstate = (TextView) v.findViewById(R.id.tagText);
        mapsetting = (Button) v.findViewById(R.id.mapsetting);

        // Seoul Btn init
        DoBong = (Button) v.findViewById(R.id.DoBong);
        NoWon = (Button) v.findViewById(R.id.NoWon);
        GangBuk = (Button) v.findViewById(R.id.GangBuk);
        SungBuk = (Button) v.findViewById(R.id.SungBuk);
        ZongRang = (Button) v.findViewById(R.id.ZongRang);
        EunPhung = (Button) v.findViewById(R.id.EunPhung);
        ZongRo = (Button) v.findViewById(R.id.ZongRo);
        DongDaeMon = (Button) v.findViewById(R.id.DongDaeMon);
        SuDaeMon = (Button) v.findViewById(R.id.SuDaeMon);
        Zhong = (Button) v.findViewById(R.id.Zhong);
        SungDong = (Button) v.findViewById(R.id.SungDong);
        GangZin = (Button) v.findViewById(R.id.GangZin);
        GangDong = (Button) v.findViewById(R.id.GangDong);
        MaPho = (Button) v.findViewById(R.id.MaPho);
        YongSan = (Button) v.findViewById(R.id.YongSan);
        GangSue = (Button) v.findViewById(R.id.GangSue);
        YangChen = (Button) v.findViewById(R.id.YangChen);
        GuRo = (Button) v.findViewById(R.id.GuRo);
        YongDengPo = (Button) v.findViewById(R.id.YongDengPo);
        DongJack = (Button) v.findViewById(R.id.DongJack);
        GemChun = (Button) v.findViewById(R.id.GemChun);
        GanAk = (Button) v.findViewById(R.id.GanAk);
        SeoCho = (Button) v.findViewById(R.id.SeoCho);
        GangNam = (Button) v.findViewById(R.id.GangNam);
        SongPa = (Button) v.findViewById(R.id.SongPa);

        // map name
        spinner = (Spinner) v.findViewById(R.id.spinner);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sppinerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // map select
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    RelativeLayout.LayoutParams imageLayout = new RelativeLayout.LayoutParams(realWidth, realHeight / 5 * 3); // width, height
                    imageLayout.setMargins(0, realHeight / 10, 0, 0);
                    imageview.setLayoutParams(imageLayout);
                    RelativeLayout.LayoutParams tagLayout = new RelativeLayout.LayoutParams(realWidth, realHeight / 14);// width, height
                    tagLayout.setMargins(0, (int) realHeight / 24 * 17, 0, 0);
                    hashstate.setLayoutParams(tagLayout);

                    JSONObject mapmeta = null;
                    mapmeta = user.getMapmetaArray().getJSONObject(position);
                    mapcurname = sppinerList.get(position);
                    mapkindnum = mapmeta.get("category").toString();
                    mapid = mapmeta.get("map_id").toString();

                    Bitmap result = user.getResult(Integer.parseInt(mapid));
//2016.01.08        imageview.setImageBitmap(result);
                    imageview.setEnabled(false);
                    Drawable drawable = new BitmapDrawable(result);
                    scalableLayout.setBackground(drawable);
                    //scalableLayout.setBackground(new BitmapDrawable(getActivity().getResources(), result));




                    // category select (SEOUL)
                    if (Integer.parseInt(mapmeta.get("category").toString()) == SystemMain.SEOUL_MAP_NUM) {

                        seoulBtnVisibility("visible",mapid);
                        hashstate.setText(mapmeta.get("hash_tag").toString());
                    }
                } catch (JSONException ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Seoul Btn onClick
        DoBong.setOnClickListener(this);
        NoWon.setOnClickListener(this);
        GangBuk.setOnClickListener(this);
        SungBuk.setOnClickListener(this);
        ZongRang.setOnClickListener(this);
        EunPhung.setOnClickListener(this);
        ZongRo.setOnClickListener(this);
        DongDaeMon.setOnClickListener(this);
        SuDaeMon.setOnClickListener(this);
        Zhong.setOnClickListener(this);
        SungDong.setOnClickListener(this);
        GangZin.setOnClickListener(this);
        GangDong.setOnClickListener(this);
        MaPho.setOnClickListener(this);
        YongSan.setOnClickListener(this);
        GangSue.setOnClickListener(this);
        YangChen.setOnClickListener(this);
        GuRo.setOnClickListener(this);
        YongDengPo.setOnClickListener(this);
        DongJack.setOnClickListener(this);
        GemChun.setOnClickListener(this);
        GanAk.setOnClickListener(this);
        SeoCho.setOnClickListener(this);
        GangNam.setOnClickListener(this);
        SongPa.setOnClickListener(this);

        mapsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), map_setting.class);
                intent.putExtra("mapcurname", mapcurname);
                intent.putExtra("hashtag", hashstate.getText().toString());
                intent.putExtra("mapkindnum", mapkindnum);
                intent.putExtra("mapid", mapid);
                startActivity(intent);
            }
        });

        imageview.post(new Runnable() {


            @Override
            public void run() {

                int[] location = new int[2];
                imageview.getLocationOnScreen(location);


                ScreenSize();

                Log.e("Display size : ", "" + realWidth);
                Log.e("Display size : ", "" + realHeight);
                Log.e("check brand", "BRAND = " + Build.BRAND);
                if (Build.BRAND.equals("lge")) {
                    Log.e("check", "ok");
                    fix_x = 47;
                    fix_y = -40-(realHeight/18);
                } else {
                    fix_y=-(realHeight/13)-(realHeight/400); //17~18
                    Log.e("check", "No");
                }
//김민정이 코드
//                RelativeLayout.LayoutParams layoutParms1 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms1.setMargins((realWidth / 100 * 33) + fix_x, realHeight / 100 * 36 + fix_y, 0, 0); // left, top, 0, 0
//                EunPhung.setLayoutParams(layoutParms1);
//                RelativeLayout.LayoutParams layoutParms2 = new RelativeLayout.LayoutParams(90 + fix_x, 90); // width, height
//                layoutParms2.setMargins(realWidth / 100 * 34 + fix_x, realHeight / 100 * 45 + fix_y, 0, 0); // left, top, 0, 0
//                SuDaeMon.setLayoutParams(layoutParms2);
//                RelativeLayout.LayoutParams layoutParms3 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms3.setMargins(realWidth / 100 * 45 + fix_x, realHeight / 100 * 43 + fix_y, 0, 0); // left, top, 0, 0
//                ZongRo.setLayoutParams(layoutParms3);
//                RelativeLayout.LayoutParams layoutParms4 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms4.setMargins(realWidth / 100 * 54 + fix_x, realHeight / 100 * 40 + fix_y, 0, 0); // left, top, 0, 0
//                SungBuk.setLayoutParams(layoutParms4);
//                RelativeLayout.LayoutParams layoutParms5 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms5.setMargins(realWidth / 100 * 52 + fix_x, realHeight / 100 * 32 + fix_y, 0, 0); // left, top, 0, 0
//                GangBuk.setLayoutParams(layoutParms5);
//
//                RelativeLayout.LayoutParams layoutParms6 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms6.setMargins(realWidth / 100 * 58 + fix_x, realHeight / 100 * 27 + fix_y, 0, 0); // left, top, 0, 0
//                DoBong.setLayoutParams(layoutParms6);
//                RelativeLayout.LayoutParams layoutParms7 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms7.setMargins(realWidth / 100 * 68 + fix_x, realHeight / 100 * 33 + fix_y, 0, 0); // left, top, 0, 0
//                NoWon.setLayoutParams(layoutParms7);
//                RelativeLayout.LayoutParams layoutParms8 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms8.setMargins(realWidth / 100 * 72 + fix_x, realHeight / 100 * 40 + fix_y, 0, 0); // left, top, 0, 0
//                ZongRang.setLayoutParams(layoutParms8);
//                RelativeLayout.LayoutParams layoutParms9 = new RelativeLayout.LayoutParams(90 + fix_x, 90); // width, height
//                layoutParms9.setMargins(realWidth / 100 * 62 + fix_x, realHeight / 100 * 44 + fix_y, 0, 0); // left, top, 0, 0
//                DongDaeMon.setLayoutParams(layoutParms9);
//                RelativeLayout.LayoutParams layoutParms10 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms10.setMargins(realWidth / 100 * 9 + fix_x, realHeight / 100 * 47 + fix_y, 0, 0); // left, top, 0, 0
//                GangSue.setLayoutParams(layoutParms10);
//
//                RelativeLayout.LayoutParams layoutParms11 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms11.setMargins(realWidth / 100 * 17 + fix_x, realHeight / 100 * 56 + fix_y, 0, 0); // left, top, 0, 0
//                YangChen.setLayoutParams(layoutParms11);
//                RelativeLayout.LayoutParams layoutParms12 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms12.setMargins(realWidth / 100 * 13 + fix_x, realHeight / 100 * 62 + fix_y, 0, 0); // left, top, 0, 0
//                GuRo.setLayoutParams(layoutParms12);
//                RelativeLayout.LayoutParams layoutParms13 = new RelativeLayout.LayoutParams(90, 90);// width, height
//                layoutParms13.setMargins(realWidth / 100 * 26 + fix_x, realHeight / 100 * 68 + fix_y, 0, 0); // left, top, 0, 0
//                GemChun.setLayoutParams(layoutParms13);
//                RelativeLayout.LayoutParams layoutParms14 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms14.setMargins(realWidth / 100 * 29 + fix_x, realHeight / 100 * 49 + fix_y, 0, 0); // left, top, 0, 0
//                MaPho.setLayoutParams(layoutParms14);
//                RelativeLayout.LayoutParams layoutParms15 = new RelativeLayout.LayoutParams(90 + fix_x, 90); // width, height
//                layoutParms15.setMargins(realWidth / 100 * 29 + fix_x, realHeight / 100 * 56 + fix_y, 0, 0); // left, top, 0, 0
//                YongDengPo.setLayoutParams(layoutParms15);
//
//                RelativeLayout.LayoutParams layoutParms16 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms16.setMargins(realWidth / 100 * 37 + fix_x, realHeight / 100 * 67 + fix_y, 0, 0); // left, top, 0, 0
//                GanAk.setLayoutParams(layoutParms16);
//                RelativeLayout.LayoutParams layoutParms17 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms17.setMargins((realWidth / 100 * 39) + fix_x, realHeight / 100 * 59 + fix_y, 0, 0); // left, top, 0, 0
//                DongJack.setLayoutParams(layoutParms17);
//                RelativeLayout.LayoutParams layoutParms18 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms18.setMargins(realWidth / 100 * 45 + fix_x, realHeight / 100 * 53 + fix_y, 0, 0); // left, top, 0, 0
//                YongSan.setLayoutParams(layoutParms18);
//                RelativeLayout.LayoutParams layoutParms19 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms19.setMargins(realWidth / 100 * 49 + fix_x, realHeight / 100 * 48 + fix_y, 0, 0); // left, top, 0, 0
//                Zhong.setLayoutParams(layoutParms19);
//                RelativeLayout.LayoutParams layoutParms20 = new RelativeLayout.LayoutParams(90, 90);// width, height
//                layoutParms20.setMargins(realWidth / 100 * 60 + fix_x, realHeight / 100 * 50 + fix_y, 0, 0); // left, top, 0, 0
//                SungDong.setLayoutParams(layoutParms20);
//
//                RelativeLayout.LayoutParams layoutParms21 = new RelativeLayout.LayoutParams(90, 90);// width, height
//                layoutParms21.setMargins(realWidth / 100 * 53 + fix_x, realHeight / 100 * 64 + fix_y, 0, 0); // left, top, 0, 0
//                SeoCho.setLayoutParams(layoutParms21);
//                RelativeLayout.LayoutParams layoutParms22 = new RelativeLayout.LayoutParams(90, 90);// width, height
//                layoutParms22.setMargins(realWidth / 100 * 63 + fix_x, realHeight / 100 * 61 + fix_y, 0, 0); // left, top, 0, 0
//                GangNam.setLayoutParams(layoutParms22);
//                RelativeLayout.LayoutParams layoutParms23 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms23.setMargins(realWidth / 100 * 70 + fix_x, realHeight / 100 * 51 + fix_y, 0, 0); // left, top, 0, 0
//                GangZin.setLayoutParams(layoutParms23);
//                RelativeLayout.LayoutParams layoutParms24 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms24.setMargins(realWidth / 100 * 75 + fix_x, realHeight / 100 * 58 + fix_y, 0, 0); // left, top, 0, 0
//                SongPa.setLayoutParams(layoutParms24);
//                RelativeLayout.LayoutParams layoutParms25 = new RelativeLayout.LayoutParams(90, 90); // width, height
//                layoutParms25.setMargins(realWidth / 100 * 83 + fix_x, realHeight / 100 * 49 + fix_y, 0, 0); // left, top, 0, 0
//                GangDong.setLayoutParams(layoutParms25);
//
//                //  SungBuk.setLayoutParams(layoutParms5);
//                Log.e("owl", "" + imageview.getHeight());

                //은평
                ScalableLayout.LayoutParams layoutParams1 = new ScalableLayout.LayoutParams(1500, 1300, 700f, 500f);
                EunPhung.setLayoutParams(layoutParams1);//scalableLayout.addView(EunPhung, layoutParams1);
                scalableLayout.setScale_TextSize(EunPhung, 150);

                //서대문
                ScalableLayout.LayoutParams layoutParams2 = new ScalableLayout.LayoutParams(1650, 1900, 700f, 500f);
                SuDaeMon.setLayoutParams(layoutParams2);//scalableLayout.addView(SuDaeMon, layoutParams2);
                scalableLayout.setScale_TextSize(SuDaeMon, 150);

                //종로
                ScalableLayout.LayoutParams layoutParams3 = new ScalableLayout.LayoutParams(2050, 1700, 700f, 500f);
                ZongRo.setLayoutParams(layoutParams3);//scalableLayout.addView(ZongRo, layoutParams3);
                scalableLayout.setScale_TextSize(ZongRo, 150);

                //성북
                ScalableLayout.LayoutParams layoutParams4 = new ScalableLayout.LayoutParams(2600, 1600, 700f, 500f);
                SungBuk.setLayoutParams(layoutParams4);//scalableLayout.addView(SungBuk, layoutParams4);
                scalableLayout.setScale_TextSize(SungBuk, 150);

                //강북
                ScalableLayout.LayoutParams layoutParams5 = new ScalableLayout.LayoutParams(2400, 900, 700f, 500f);
                GangBuk.setLayoutParams(layoutParams5);//scalableLayout.addView(GangBuk, layoutParams5);
                scalableLayout.setScale_TextSize(GangBuk, 150);




                //도봉
                ScalableLayout.LayoutParams layoutParams6 = new ScalableLayout.LayoutParams(2750, 500, 700f, 500f);
                DoBong.setLayoutParams(layoutParams6);//scalableLayout.addView(DoBong, layoutParams6);
                scalableLayout.setScale_TextSize(DoBong, 150);

                //노원
                ScalableLayout.LayoutParams layoutParams7 = new ScalableLayout.LayoutParams(3200, 1000, 700f, 500f);
                NoWon.setLayoutParams(layoutParams7);//scalableLayout.addView(NoWon, layoutParams7);
                scalableLayout.setScale_TextSize(NoWon, 150);

                //중랑
                ScalableLayout.LayoutParams layoutParams8 = new ScalableLayout.LayoutParams(3400, 1600, 700f, 500f);
                ZongRang.setLayoutParams(layoutParams8);//scalableLayout.addView(ZongRang, layoutParams8);
                scalableLayout.setScale_TextSize(ZongRang, 150);

                //동대문
                ScalableLayout.LayoutParams layoutParams9 = new ScalableLayout.LayoutParams(3000, 1900, 700f, 500f);
                DongDaeMon.setLayoutParams(layoutParams9);//scalableLayout.addView(DongDaeMon, layoutParams9);
                scalableLayout.setScale_TextSize(DongDaeMon, 150);

                //강서
                ScalableLayout.LayoutParams layoutParams10 = new ScalableLayout.LayoutParams(390, 2150, 700f, 500f);
                GangSue.setLayoutParams(layoutParams10);//scalableLayout.addView(GangSue, layoutParams10);
                scalableLayout.setScale_TextSize(GangSue, 150);




                ///////////////////////
                //양천
                ScalableLayout.LayoutParams layoutParams11 = new ScalableLayout.LayoutParams(700, 2850, 700f, 500f);
                YangChen.setLayoutParams(layoutParams11);//scalableLayout.addView(YangChen, layoutParams11);
                scalableLayout.setScale_TextSize(YangChen, 150);

                //구로
                ScalableLayout.LayoutParams layoutParams12 = new ScalableLayout.LayoutParams(600, 3350, 700f, 500f);
                GuRo.setLayoutParams(layoutParams12);//scalableLayout.addView(GuRo, layoutParams12);
                scalableLayout.setScale_TextSize(GuRo, 150);

                //금천
                ScalableLayout.LayoutParams layoutParams13 = new ScalableLayout.LayoutParams(1200, 3800, 700f, 500f);
                GemChun.setLayoutParams(layoutParams13);//scalableLayout.addView(GemChun, layoutParams13);
                scalableLayout.setScale_TextSize(GemChun, 150);

                //마포
                ScalableLayout.LayoutParams layoutParams14 = new ScalableLayout.LayoutParams(1400, 2350, 700f, 500f);
                MaPho.setLayoutParams(layoutParams14);//scalableLayout.addView(MaPho, layoutParams14);
                scalableLayout.setScale_TextSize(MaPho, 150);

                //영등포
                ScalableLayout.LayoutParams layoutParams15 = new ScalableLayout.LayoutParams(1350, 2900, 700f, 500f);
                YongDengPo.setLayoutParams(layoutParams15);//scalableLayout.addView(YongDengPo, layoutParams15);
                scalableLayout.setScale_TextSize(YongDengPo, 150);




                /////////////////////////
                //관악
                ScalableLayout.LayoutParams layoutParams16 = new ScalableLayout.LayoutParams(1750, 3800, 700f, 500f);
                GanAk.setLayoutParams(layoutParams16);//scalableLayout.addView(GanAk, layoutParams16);
                scalableLayout.setScale_TextSize(GanAk, 150);

                //동작
                ScalableLayout.LayoutParams layoutParams17 = new ScalableLayout.LayoutParams(1800, 3150, 700f, 500f);
                DongJack.setLayoutParams(layoutParams17);//scalableLayout.addView(DongJack, layoutParams17);
                scalableLayout.setScale_TextSize(DongJack, 150);

                //용산
                ScalableLayout.LayoutParams layoutParams18 = new ScalableLayout.LayoutParams(2150, 2650, 700f, 500f);
                YongSan.setLayoutParams(layoutParams18);//scalableLayout.addView(YongSan, layoutParams18);
                scalableLayout.setScale_TextSize(YongSan, 150);

                //중구
                ScalableLayout.LayoutParams layoutParams19 = new ScalableLayout.LayoutParams(2300, 2200, 700f, 500f);
                Zhong.setLayoutParams(layoutParams19);//scalableLayout.addView(Zhong, layoutParams19);
                scalableLayout.setScale_TextSize(Zhong, 150);

                //성동
                ScalableLayout.LayoutParams layoutParams20 = new ScalableLayout.LayoutParams(2800, 2400, 700f, 500f);
                SungDong.setLayoutParams(layoutParams20);//scalableLayout.addView(SungDong, layoutParams20);
                scalableLayout.setScale_TextSize(SungDong, 150);




                //////////////////////////////////
                //서초
                ScalableLayout.LayoutParams layoutParams21 = new ScalableLayout.LayoutParams(2450, 3500, 700f, 500f);
                SeoCho.setLayoutParams(layoutParams21);//scalableLayout.addView(SeoCho, layoutParams21);
                scalableLayout.setScale_TextSize(SeoCho, 150);

                //강남
                ScalableLayout.LayoutParams layoutParams22 = new ScalableLayout.LayoutParams(2950, 3250, 700f, 500f);
                GangNam.setLayoutParams(layoutParams22);//scalableLayout.addView(GangNam, layoutParams22);
                scalableLayout.setScale_TextSize(GangNam, 150);

                //광진
                ScalableLayout.LayoutParams layoutParams23 = new ScalableLayout.LayoutParams(3300, 2500, 700f, 500f);
                GangZin.setLayoutParams(layoutParams23);//scalableLayout.addView(GangZin, layoutParams23);
                scalableLayout.setScale_TextSize(GangZin, 150);

                //송파
                ScalableLayout.LayoutParams layoutParams24 = new ScalableLayout.LayoutParams(3500, 3000, 700f, 500f);
                SongPa.setLayoutParams(layoutParams24);//scalableLayout.addView(SongPa, layoutParams24);
                scalableLayout.setScale_TextSize(SongPa, 150);

                //강동
                ScalableLayout.LayoutParams layoutParams25 = new ScalableLayout.LayoutParams(4000, 2300, 700f, 500f);
                GangDong.setLayoutParams(layoutParams25);//scalableLayout.addView(GangDong, layoutParams25);
                scalableLayout.setScale_TextSize(GangDong, 150);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    // 서울 지도 버튼 Visibility
    public void seoulBtnVisibility(String visible, String mapid) {
        if (visible.equals("visible")) {
            // Seoul Btn can view
            DoBong.setVisibility(View.VISIBLE);
            NoWon.setVisibility(View.VISIBLE);
            GangBuk.setVisibility(View.VISIBLE);
            SungBuk.setVisibility(View.VISIBLE);
            ZongRang.setVisibility(View.VISIBLE);
            EunPhung.setVisibility(View.VISIBLE);
            ZongRo.setVisibility(View.VISIBLE);
            DongDaeMon.setVisibility(View.VISIBLE);
            SuDaeMon.setVisibility(View.VISIBLE);
            Zhong.setVisibility(View.VISIBLE);
            SungDong.setVisibility(View.VISIBLE);
            GangZin.setVisibility(View.VISIBLE);
            GangDong.setVisibility(View.VISIBLE);
            MaPho.setVisibility(View.VISIBLE);
            YongSan.setVisibility(View.VISIBLE);
            GangSue.setVisibility(View.VISIBLE);
            YangChen.setVisibility(View.VISIBLE);
            GuRo.setVisibility(View.VISIBLE);
            YongDengPo.setVisibility(View.VISIBLE);
            DongJack.setVisibility(View.VISIBLE);
            GemChun.setVisibility(View.VISIBLE);
            GanAk.setVisibility(View.VISIBLE);
            SeoCho.setVisibility(View.VISIBLE);
            GangNam.setVisibility(View.VISIBLE);
            SongPa.setVisibility(View.VISIBLE);

            DoBong.setText("도봉구\n(");
            DoBong.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.DoBong)));
            DoBong.append(")");
            NoWon.setText("노원구\n(");
            NoWon.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.NoWon)));
            NoWon.append(")");
            GangBuk.setText("강북구\n(");
            GangBuk.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GangBuk)));
            GangBuk.append(")");
            SungBuk.setText("성북구\n(");
            SungBuk.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.SungBuk)));
            SungBuk.append(")");
            ZongRang.setText("중랑구\n(");
            ZongRang.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.ZongRang)));
            ZongRang.append(")");
            EunPhung.setText("은평구\n(");
            EunPhung.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.EunPhung)));
            EunPhung.append(")");
            ZongRo.setText("종로구\n(");
            ZongRo.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.ZongRo)));
            ZongRo.append(")");
            DongDaeMon.setText("동대문구\n(");
            DongDaeMon.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.DongDaeMon)));
            DongDaeMon.append(")");
            SuDaeMon.setText("서대문구\n(");
            SuDaeMon.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.SuDaeMon)));
            SuDaeMon.append(")");
            Zhong.setText("중구\n(");
            Zhong.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.Zhong)));
            Zhong.append(")");
            SungDong.setText("성동구\n(");
            SungDong.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.SungDong)));
            SungDong.append(")");
            GangZin.setText("광진구\n(");
            GangZin.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GangZin)));
            GangZin.append(")");
            GangDong.setText("강동구\n(");
            GangDong.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GangDong)));
            GangDong.append(")");
            MaPho.setText("마포구\n(");
            MaPho.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.MaPho)));
            MaPho.append(")");
            YongSan.setText("용산구\n(");
            YongSan.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.YongSan)));
            YongSan.append(")");
            GangSue.setText("강서구\n(");
            GangSue.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GangSue)));
            GangSue.append(")");
            YangChen.setText("양천구\n(");
            YangChen.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.YangChen)));
            YangChen.append(")");
            GuRo.setText("구로구\n(");
            GuRo.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GuRo)));
            GuRo.append(")");
            YongDengPo.setText("영등포구\n(");
            YongDengPo.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.YongDengPo)));
            YongDengPo.append(")");
            DongJack.setText("동작구\n(");
            DongJack.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.DongJack)));
            DongJack.append(")");
            GemChun.setText("금천구\n(");
            GemChun.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GemChun)));
            GemChun.append(")");
            GanAk.setText("관악구\n(");
            GanAk.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GanAk)));
            GanAk.append(")");
            SeoCho.setText("서초구\n(");
            SeoCho.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.SeoCho)));
            SeoCho.append(")");
            GangNam.setText("강남구\n(");
            GangNam.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.GangNam)));
            GangNam.append(")");
            SongPa.setText("송파구\n(");
            SongPa.append(String.valueOf(user.getPingCount(Integer.parseInt(mapid), SystemMain.SongPa)));
            SongPa.append(")");

        } else if (visible.equals("gone")) {
            // Seoul Btn can not view
            DoBong.setVisibility(View.GONE);
            NoWon.setVisibility(View.GONE);
            GangBuk.setVisibility(View.GONE);
            SungBuk.setVisibility(View.GONE);
            ZongRang.setVisibility(View.GONE);
            EunPhung.setVisibility(View.GONE);
            ZongRo.setVisibility(View.GONE);
            DongDaeMon.setVisibility(View.GONE);
            SuDaeMon.setVisibility(View.GONE);
            Zhong.setVisibility(View.GONE);
            SungDong.setVisibility(View.GONE);
            GangZin.setVisibility(View.GONE);
            GangDong.setVisibility(View.GONE);
            MaPho.setVisibility(View.GONE);
            YongSan.setVisibility(View.GONE);
            GangSue.setVisibility(View.GONE);
            YangChen.setVisibility(View.GONE);
            GuRo.setVisibility(View.GONE);
            YongDengPo.setVisibility(View.GONE);
            DongJack.setVisibility(View.GONE);
            GemChun.setVisibility(View.GONE);
            GanAk.setVisibility(View.GONE);
            SeoCho.setVisibility(View.GONE);
            GangNam.setVisibility(View.GONE);
            SongPa.setVisibility(View.GONE);
        }
    }

    // 버튼 클릭 리스너
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // select location (SEOUL)
            case R.id.DoBong:
                loc_LNG = Location.DOBONGGU_LNG;
                loc_LAT = Location.DOBONGGU_LAT;
                break;
            case R.id.NoWon:
                loc_LNG = Location.NOWONGU_LNG;
                loc_LAT = Location.NOWONGU_LAT;
                break;
            case R.id.GangBuk:
                loc_LNG = Location.GANGBOOKGU_LNG;
                loc_LAT = Location.GANGBOOKGU_LAT;
                break;
            case R.id.SungBuk:
                loc_LNG = Location.SEONGBUKGU_LNG;
                loc_LAT = Location.SEONGBUKGU_LAT;
                break;
            case R.id.ZongRang:
                loc_LNG = Location.JUNGNAMGGU_LNG;
                loc_LAT = Location.JUNGNAMGGU_LAT;
                break;
            case R.id.EunPhung:
                loc_LNG = Location.EUNPYEONGGU_LNG;
                loc_LAT = Location.EUNPYEONGGU_LAT;
                break;
            case R.id.ZongRo:
                loc_LNG = Location.JONGNOGU_LNG;
                loc_LAT = Location.JONGNOGU_LAT;
                break;
            case R.id.DongDaeMon:
                loc_LNG = Location.DONGDAEMUNGU_LNG;
                loc_LAT = Location.DONGDAEMUNGU_LAT;
                break;
            case R.id.SuDaeMon:
                loc_LNG = Location.SEODAEMUNGU_LNG;
                loc_LAT = Location.SEODAEMUNGU_LAT;
                break;
            case R.id.Zhong:
                loc_LNG = Location.JUNGGU_LNG;
                loc_LAT = Location.JUNGGU_LAT;
                break;
            case R.id.SungDong:
                loc_LNG = Location.SEONGDONGGU_LNG;
                loc_LAT = Location.SEONGDONGGU_LAT;
                break;
            case R.id.GangZin:
                loc_LNG = Location.GWANGJINGU_LNG;
                loc_LAT = Location.GWANGJINGU_LAT;
                break;
            case R.id.GangDong:
                loc_LNG = Location.GANGDONGGU_LNG;
                loc_LAT = Location.GANGDONGGU_LAT;
                break;
            case R.id.MaPho:
                loc_LNG = Location.MAPOGU_LNG;
                loc_LAT = Location.MAPOGU_LAT;
                break;
            case R.id.YongSan:
                loc_LNG = Location.YONGSANGU_LNG;
                loc_LAT = Location.YONGSANGU_LAT;
                break;
            case R.id.GangSue:
                loc_LNG = Location.GANGSEOGU_LNG;
                loc_LAT = Location.GANGSEOGU_LAT;
                break;
            case R.id.YangChen:
                loc_LNG = Location.YANGCHEONGU_LNG;
                loc_LAT = Location.YANGCHEONGU_LAT;
                break;
            case R.id.GuRo:
                loc_LNG = Location.GUROGU_LNG;
                loc_LAT = Location.GUROGU_LAT;
                break;
            case R.id.YongDengPo:
                loc_LNG = Location.YEONGDEUNGPOGU_LNG;
                loc_LAT = Location.YEONGDEUNGPOGU_LAT;
                break;
            case R.id.DongJack:
                loc_LNG = Location.DONGJAKGU_LNG;
                loc_LAT = Location.DONGJAKGU_LAT;
                break;
            case R.id.GemChun:
                loc_LNG = Location.GEUMCHEONGU_LNG;
                loc_LAT = Location.GEUMCHEONGU_LAT;
                break;
            case R.id.GanAk:
                loc_LNG = Location.GWANAKGU_LNG;
                loc_LAT = Location.GWANAKGU_LAT;
                break;
            case R.id.SeoCho:
                loc_LNG = Location.SEOCHOGU_LNG;
                loc_LAT = Location.SEOCHOGU_LAT;
                break;
            case R.id.GangNam:
                loc_LNG = Location.GANGNAMGU_LNG;
                loc_LAT = Location.GANGNAMGU_LAT;
                break;
            case R.id.SongPa:
                loc_LNG = Location.SONGPAGU_LNG;
                loc_LAT = Location.SONGPAGU_LAT;
                break;
        }

        if (user.getMapforpinNum(Integer.parseInt(mapid)) == 0) {
            GetStorearrary(v);
        } else if(user.getMapforpinNum(Integer.parseInt(mapid)) == 2){
            // toast
            text_toast.setText("등록 된 리뷰가 없습니다.");
            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();
        }
        else {

            Log.v("홈", "맵인텐트");
            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra("fragment_id","home");
            intent.putExtra("LNG", loc_LNG);
            intent.putExtra("LAT", loc_LAT);
            intent.putExtra("mapid", mapid);
            startActivity(intent);

        }
    }

    public void refresh() {
        Log.v("리프레쉬", "확인");
        Bitmap result = user.getResult(Integer.parseInt(mapid));
        //2016.01.08        imageview.setImageBitmap(result);
        imageview.setEnabled(false);
        Drawable drawable = new BitmapDrawable(result);
        scalableLayout.setBackground(drawable);

        mapnum = user.getMapmetaArray().length();
        sppinerList = new ArrayList<String>();
        try {
            for (int i = 0; i < mapnum; i++) {
                sppinerList.add(user.getMapmetaArray().getJSONObject(i).getString("title"));
            }
        } catch (JSONException ex) {

        }

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sppinerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        user.setMapmetaNum(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        user = UserData.getInstance();
        if (user.getMapmetaNum() == 1)
            refresh();
    }


    public void ScreenSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();


        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;
            realHeight = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realWidth = display.getWidth();
                realHeight = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realWidth = display.getWidth();
            realHeight = display.getHeight();

        }
    }

    public void GetStorearrary(View v) {
        RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", user.getUserID());
            obj.put("map_id", mapid);
            Log.v("제이손 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_HOMETOMAP_URL,
                obj,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);

    }


    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("홈 가게", response.toString());
                try {
                    if (response.get("state").toString().equals("701")) {
                        user.setMapforpinNum(Integer.parseInt(mapid), 1);
                        user.setMapforpinArray(response.getJSONArray("map_meta"), Integer.parseInt(response.getJSONArray("map_meta").getJSONObject(0).get("map_id").toString()));
                        Log.v("홈에서 맵 어레이", user.getMapforpinArray(Integer.parseInt(mapid)).toString());

                        Log.v("홈", "맵인텐트");
                        Intent intent = new Intent(getActivity(), MapActivity.class);
                        intent.putExtra("fragment_id","home");
                        intent.putExtra("LNG", loc_LNG);
                        intent.putExtra("LAT", loc_LAT);
                        intent.putExtra("mapid", mapid);
                        startActivity(intent);
                    } else if (response.get("state").toString().equals("711")) {
                        user.setMapforpinNum(Integer.parseInt(mapid), 2);
                        // toast
                        text_toast.setText("등록 된 리뷰가 없습니다.");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();
                    }
                } catch (JSONException ex) {

                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("homeFragment", error.getMessage());
                } catch (NullPointerException ex) {
                    // toast
                    Log.e("homeFragment", "nullpointexception");
                }
            }
        };
    }


}
