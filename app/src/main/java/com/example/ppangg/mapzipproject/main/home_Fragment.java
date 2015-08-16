package com.example.ppangg.mapzipproject.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ppangg.mapzipproject.map_setting;
import com.example.ppangg.mapzipproject.map.MapActivity;
import com.example.ppangg.mapzipproject.R;
import com.example.ppangg.mapzipproject.SystemMain;
import com.example.ppangg.mapzipproject.UserData;
import com.example.ppangg.mapzipproject.map.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class home_Fragment extends Fragment implements View.OnClickListener{

    private View v;
    private UserData user;

    private TextView topstate; // user info
    private View imageview; // map image
    private TextView hashstate; // hashtag
    private String mapcurname=""; // 현재 지도 이름
    private String mapkindnum; // 현재 지도 속성 번호

    private ArrayList<String> sppinerList; // map name
    Spinner spinner;
    private int mapnum; // map num

    private Button mapsetting;

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

    public home_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();

        mapnum = user.getMapmetaArray().length();
        sppinerList = new ArrayList<String>();
        try {
            for (int i = 0; i < mapnum; i++) {
                sppinerList.add(user.getMapmetaArray().getJSONObject(i).getString("title"));
            }
        }catch (JSONException ex){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);

        topstate = (TextView) v.findViewById(R.id.topstate);
        topstate.setText(user.getUserName());
        topstate.append(" (");
        topstate.append(user.getUserID());
        topstate.append(")");
        user.inputTestnum();
        topstate.append(String.valueOf(user.getTestnum()));

        imageview = (View) v.findViewById(R.id.mapimage);
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
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sppinerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // map select
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject mapmeta = null;
                    mapmeta = user.getMapmetaArray().getJSONObject(position);
                    mapcurname = sppinerList.get(position);
                    mapkindnum = mapmeta.get("category").toString();

                    // category select (SEOUL)
                    if (Integer.parseInt(mapmeta.get("category").toString()) == SystemMain.SEOUL_MAP_NUM) {
                        imageview.setBackgroundResource(R.drawable.seoul);
                        seoulBtnVisibility("visible");
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
                intent.putExtra("mapcurname",mapcurname);
                intent.putExtra("hashtag",hashstate.getText().toString());
                intent.putExtra("mapkindnum",mapkindnum);
                startActivity(intent);
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
    public void seoulBtnVisibility(String visible) {
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
        double loc_LNG = 0;
        double loc_LAT = 0;
        switch (v.getId()){
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
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("LNG", loc_LNG);
        intent.putExtra("LAT", loc_LAT);
        startActivity(intent);
    }

}
