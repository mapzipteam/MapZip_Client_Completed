package com.example.ppangg.mapzipproject.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.ppangg.mapzipproject.R;
import com.example.ppangg.mapzipproject.SystemMain;
import com.example.ppangg.mapzipproject.UserData;
import com.example.ppangg.mapzipproject.review_register;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class review_Fragment extends Fragment {

    private UserData user;
    private ArrayList<String> sppinerList; // map name
    private Spinner spinner;
    private ArrayAdapter adapter;
    private int mapnum; // map num
    private String mapid; // 현재 지도 pid값

    private Button review_regi;

	public review_Fragment(){}
	


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
 
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        // map name
        spinner = (Spinner) v.findViewById(R.id.spinner_review);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sppinerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // map select
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject mapmeta = null;
                    mapmeta = user.getMapmetaArray().getJSONObject(position);
                    mapid = mapmeta.get("map_id").toString();
                    Log.v("mappid",mapid);

                } catch (JSONException ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        review_regi = (Button) v.findViewById(R.id.registerBtn_review);
        review_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), review_register.class);
                intent.putExtra("mapid", mapid);
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
    //
    // ㅠㅠ

}


