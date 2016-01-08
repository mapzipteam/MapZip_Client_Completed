package com.mapzip.ppang.mapzipproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.mapzip.ppang.mapzipproject.R;

public class SplashActivity extends Activity {

    private ImageView StartImage;
    private Animation start_ani;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_splash);
        StartImage = (ImageView)findViewById(R.id.start_image);
        start_ani = new AlphaAnimation(0.0f,1.0f);
        start_ani.setDuration(2000);
        StartImage.startAnimation(start_ani);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(),2000);
    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }


}
