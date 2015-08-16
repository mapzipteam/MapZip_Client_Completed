package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by ppangg on 2015-08-13.
 */
public class map_setting extends Activity
{
    private UserData user;
    private TextView mapname;
    private TextView hashtag1;
    private TextView hashtag2;
    private TextView hashtag3;
    private TextView hashtag4;
    private TextView hashtag5;
    private String hashtag_send="";
    private int mapkindnum;

    private Button saveBtn;
    private Button cancelBtn;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsetting);
        user = UserData.getInstance();

        mapname = (TextView) findViewById(R.id.maptext);
        hashtag1 = (TextView) findViewById(R.id.hashtext1);
        hashtag2 = (TextView) findViewById(R.id.hashtext2);
        hashtag3 = (TextView) findViewById(R.id.hashtext3);
        hashtag4 = (TextView) findViewById(R.id.hashtext4);
        hashtag5 = (TextView) findViewById(R.id.hashtext5);

        String str_mapname = getIntent().getStringExtra("mapcurname");
        mapname.setText(str_mapname);

        String hashorgin = getIntent().getStringExtra("hashtag");
        String[] hasharr = hashorgin.split("#");

        for(int i=1; i<hasharr.length; i++)
        {
            switch (i){
                case 1: hashtag1.setText(hasharr[i]);
                    break;
                case 2: hashtag2.setText(hasharr[i]);
                    break;
                case 3: hashtag3.setText(hasharr[i]);
                    break;
                case 4: hashtag4.setText(hasharr[i]);
                    break;
                case 5: hashtag5.setText(hasharr[i]);
                    break;
            }
        }

        // map name
        Spinner spinner = (Spinner) findViewById(R.id.spinner_mapsetting);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_number));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mapkindnum = Integer.parseInt(getIntent().getStringExtra("mapkindnum"));
        spinner.setSelection(mapkindnum-1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mapkindnum = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void saveOnclick(View v)
    {
        if(!hashtag1.getText().toString().trim().isEmpty())
            hashtag_send += "#"+hashtag1.getText().toString();

        if(!hashtag2.getText().toString().trim().isEmpty())
            hashtag_send += "#"+hashtag2.getText().toString();

        if(!hashtag3.getText().toString().trim().isEmpty())
            hashtag_send += "#"+hashtag3.getText().toString();

        if(!hashtag4.getText().toString().trim().isEmpty())
            hashtag_send += "#"+hashtag4.getText().toString();

        if(!hashtag5.getText().toString().trim().isEmpty())
            hashtag_send += "#"+hashtag5.getText().toString();

        // hash_tag <= hashtag_send
        // category <= mapkindnum
        // title <= mapname.getText().toString();

    }

    public void cancelOnclick(View v)
    {
        this.finish();
    }

}
