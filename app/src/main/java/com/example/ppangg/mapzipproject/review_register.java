package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by ppangg on 2015-08-22.
 */
public class review_register extends Activity {

    private SeekBar seekbar;
    private ImageView emotion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_regi);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_review_regi);
        ImageAdapter imageadapter = new ImageAdapter(this);
        viewPager.setAdapter(imageadapter);

        seekbar = (SeekBar) findViewById(R.id.emotionBar_review_regi);
        emotion = (ImageView) findViewById(R.id.emotion_review_regi);
        emotion.setImageResource(R.drawable.sample_emotion0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 20)
                    emotion.setImageResource(R.drawable.sample_emotion);
                else if((20 <= progress) && (progress <40))
                    emotion.setImageResource(R.drawable.sample_emotion2);
                else if((40 <= progress) && (progress <60))
                    emotion.setImageResource(R.drawable.sample_emotion3);
                else if((60 <= progress) && (progress <80))
                    emotion.setImageResource(R.drawable.sample_emotion4);
                else
                    emotion.setImageResource(R.drawable.sample_emotion5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final EditText directEdit = (EditText) findViewById(R.id.editeval_review_regi);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_review_regi);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_review_regi));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 3) // 직접입력
                    directEdit.setVisibility(View.VISIBLE);
                else
                    directEdit.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
