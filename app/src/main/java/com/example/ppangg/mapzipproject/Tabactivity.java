package com.example.ppangg.mapzipproject;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Resources;
import android.view.View;

public class Tabactivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_tabs);
        // mTabHost = new FragmentTabHost(this);
        // mTabHost.setup(this, getSupportFragmentManager(),
        // R.id.menu_settings);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        Resources res = getResources();
        Drawable draw1 = res.getDrawable(R.drawable.home);
        Drawable draw2 = res.getDrawable(R.drawable.posting);
        Drawable draw3 = res.getDrawable(R.drawable.search);
        Drawable draw4 = res.getDrawable(R.drawable.friend);
        Drawable draw5 = res.getDrawable(R.drawable.setting);

        Bundle b = new Bundle();
        b.putString("key", "Sample");
        mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("", draw1), Fragment1.class, b);
        mTabHost.addTab(mTabHost.newTabSpec("posting").setIndicator("",draw2), Fragment2.class, b);
        mTabHost.addTab(mTabHost.newTabSpec("search").setIndicator("",draw3),Fragment3.class, b);
        mTabHost.addTab(mTabHost.newTabSpec("friend").setIndicator("", draw4), Fragment4.class, b);
        mTabHost.addTab(mTabHost.newTabSpec("setting").setIndicator("",draw5),Fragment5.class, b);
        // setContentView(mTabHost);

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
