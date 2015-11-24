package com.mapzip.ppang.mapzipproject;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    // Login fragment, Join fragment
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    // auto_login
    private SharedPreferences pref;
    private int isAuto;
    private String auto_id;
    private String auto_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        pref = getSharedPreferences("auto_login", MODE_PRIVATE);
        isAuto = pref.getInt("isAuto", -1);

        UserData userData = UserData.getInstance();
        userData.init();

        userData.setIsAuto(isAuto);


        if(isAuto == -1){
            // no auto login
            Log.d("pref","Auto Login off : "+isAuto);
        }
        else if(isAuto == 1){
            Log.d("pref","Auto Login on");
            auto_id = pref.getString("auto_id","");
            auto_pw = pref.getString("auto_pw","");
            Log.d("pref","auto_id : "+auto_id);
            Log.d("pref","auto_pw : "+auto_pw);
        }


    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // �ش��ϴ� page�� Fragment�� �����մϴ�.
            if(position == 0)
                return loginFragment.create(position,isAuto,auto_id,auto_pw);
            else
                return joinFragment.create(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
