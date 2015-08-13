package com.example.ppangg.mapzipproject.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ppangg.mapzipproject.R;


/**
 * Created by Song  Ji won on 2015-07-31.
 */
public class ReviewActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
    }

    public void onButton1Clicked(View v)
    {
        Toast.makeText(getApplicationContext(), "리뷰 끝났으면 돌아가라라", Toast.LENGTH_LONG).show();

        finish();
   }
}
