package com.mapzip.ppang.mapzipproject.adapter;

/**
 * Created by Minjeong on 2015-08-03.
 */
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.model.NavDrawerItem;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private int color_pressed;
    private TypedArray navMenuIcons_pressed;
    private int changenum=-1;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("네브드로어어댑터",String.valueOf(position));
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        if(changenum == position) {
            imgIcon.setImageResource(navMenuIcons_pressed.getResourceId(position, -1));
            txtTitle.setTextColor(color_pressed);
        }
        else {
            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setTextColor(Color.DKGRAY);
        }

        if(changenum == -1){
            if(position == 0){
                imgIcon.setImageResource(navMenuIcons_pressed.getResourceId(position, -1));
                txtTitle.setTextColor(color_pressed);
            }
        }

        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void getResource(Resources rs){
        Log.v("겟리소스","ㅇㅇ");

        navMenuIcons_pressed =  rs.obtainTypedArray(R.array.nav_drawer_icons_pressed);
        color_pressed = rs.getColor(R.color.hotpink);
    }

    public void changeIcon(int position){
        Log.v("체인지", String.valueOf(position));

        changenum = position;
    }

}