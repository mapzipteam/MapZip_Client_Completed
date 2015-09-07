package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppangg.mapzipproject.ImageAdapter;
import com.example.ppangg.mapzipproject.R;
import com.example.ppangg.mapzipproject.UserData;
import com.example.ppangg.mapzipproject.model.MapData;


/**
 * Created by Song  Ji won on 2015-07-31.
 */
public class ReviewActivity extends Activity
{
    private UserData user;

    // 리뷰 데이타
    private ImageView review_emotion;
    private TextView store_name;
    private TextView review_text;
    private TextView store_address;
    private TextView store_contact;

    // image
    private ImageAdapter imageadapter;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        user = UserData.getInstance();

        review_emotion = (ImageView) findViewById(R.id.emotion_review);
        store_name = (TextView) findViewById(R.id.name_review);
        review_text = (TextView) findViewById(R.id.text_review);
        store_address = (TextView) findViewById(R.id.address_text_review);
        store_contact = (TextView) findViewById(R.id.contact_text_review);

        MapData mapData = user.getMapData();
        store_name.setText(mapData.getStore_name());
        review_text.setText(mapData.getReview_text());
        store_address.setText(mapData.getStore_address());
        store_contact.setText(mapData.getStore_contact());

        if (mapData.getReview_emotion() < 20)
            review_emotion.setImageResource(R.drawable.emotion1);
        else if ((20 <= mapData.getReview_emotion()) && (mapData.getReview_emotion() < 40))
            review_emotion.setImageResource(R.drawable.emotion2);
        else if ((40 <= mapData.getReview_emotion()) && (mapData.getReview_emotion() < 60))
            review_emotion.setImageResource(R.drawable.emotion3);
        else if ((60 <= mapData.getReview_emotion()) && (mapData.getReview_emotion() < 80))
            review_emotion.setImageResource(R.drawable.emotion4);
        else
            review_emotion.setImageResource(R.drawable.emotion5);


        viewPager = (ViewPager) findViewById(R.id.pager_review);
        imageadapter = new ImageAdapter(this);
        viewPager.setAdapter(imageadapter);

    }



}
