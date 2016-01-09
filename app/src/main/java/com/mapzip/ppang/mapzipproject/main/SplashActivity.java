package com.mapzip.ppang.mapzipproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mapzip.ppang.mapzipproject.R;

public class SplashActivity extends Activity {

    private ImageView StartImage_ma;
    private ImageView StartImage_flag1;
    private ImageView StartImage_zi;
    private ImageView StartImage_flag2;
    private Animation start_text_ani;
    private Animation start_flag_ani_1;
    private Animation start_flag_ani_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        StartImage_ma = (ImageView)findViewById(R.id.start_ma_image);
        StartImage_flag1 = (ImageView)findViewById(R.id.start_flag1_image);
        StartImage_zi = (ImageView)findViewById(R.id.start_zi_image);
        StartImage_flag2 = (ImageView)findViewById(R.id.start_flag2_image);
        start_text_ani = AnimationUtils.loadAnimation(this,R.anim.start_alpha);
        start_flag_ani_1 = AnimationUtils.loadAnimation(this,R.anim.start_up2down_1);
        start_flag_ani_2 = AnimationUtils.loadAnimation(this,R.anim.start_up2down_2);
        StartImage_ma.startAnimation(start_text_ani);
        StartImage_flag1.startAnimation(start_flag_ani_1);
        StartImage_zi.startAnimation(start_text_ani);
        StartImage_flag2.startAnimation(start_flag_ani_2);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(),2500);
    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }


}
