package com.example.ppangg.mapzipproject;

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

import java.util.ArrayList;

public class home_Fragment extends Fragment {

    private View v;
    private TextView topstate ;
    private UserData user;
    private ArrayList<String> sppinerList;
    private View imageview;

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

	public home_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();

        sppinerList = new ArrayList<String>();
        sppinerList.add("첫번째");
        sppinerList.add("second");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        v = inflater.inflate(R.layout.fragment_home, container, false);
        imageview = (View)v.findViewById(R.id.mapimage);

        topstate = (TextView)v.findViewById(R.id.topstate);
        topstate.setText(user.getUserName());
        topstate.append("(");
        topstate.append(user.getUserID());
        topstate.append(")");

        DoBong = (Button)v.findViewById(R.id.DoBong);
        NoWon = (Button)v.findViewById(R.id.NoWon);
        GangBuk = (Button)v.findViewById(R.id.GangBuk);
        SungBuk = (Button)v.findViewById(R.id.SungBuk);
        ZongRang = (Button)v.findViewById(R.id.ZongRang);
        EunPhung = (Button)v.findViewById(R.id.EunPhung);
        ZongRo = (Button)v.findViewById(R.id.ZongRo);
        DongDaeMon = (Button)v.findViewById(R.id.DongDaeMon);
        SuDaeMon = (Button)v.findViewById(R.id.SuDaeMon);
        Zhong = (Button)v.findViewById(R.id.Zhong);
        SungDong = (Button)v.findViewById(R.id.SungDong);
        GangZin = (Button)v.findViewById(R.id.GangZin);
        GangDong = (Button)v.findViewById(R.id.GangDong);
        MaPho = (Button)v.findViewById(R.id.MaPho);
        YongSan = (Button)v.findViewById(R.id.YongSan);
        GangSue = (Button)v.findViewById(R.id.GangSue);
        YangChen = (Button)v.findViewById(R.id.YangChen);
        GuRo = (Button)v.findViewById(R.id.GuRo);
        YongDengPo = (Button)v.findViewById(R.id.YongDengPo);
        DongJack = (Button)v.findViewById(R.id.DongJack);
        GemChun = (Button)v.findViewById(R.id.GemChun);
        GanAk = (Button)v.findViewById(R.id.GanAk);
        SeoCho = (Button)v.findViewById(R.id.SeoCho);
        GangNam = (Button)v.findViewById(R.id.GangNam);
        SongPa = (Button)v.findViewById(R.id.SongPa);

        //스피너
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,sppinerList);
        Spinner spinner = (Spinner)v.findViewById(R.id.spinner);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_number, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    imageview.setBackgroundResource(R.drawable.seoul);

                    seoulBtnVisibility("visible");


                }
                else
                {
                    imageview.setBackgroundResource(R.drawable.p1);

                    seoulBtnVisibility("gone");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    public void seoulBtnVisibility(String visible){

        if(visible.equals("visible")) {
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
        }
        else if(visible.equals("gone")){
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
}
