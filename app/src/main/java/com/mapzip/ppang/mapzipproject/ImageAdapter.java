package com.mapzip.ppang.mapzipproject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ppangg on 2015-08-23.
 */

public class ImageAdapter extends PagerAdapter {
    Context context;
    private int nowwho;
    private UserData user;
    private FriendData fuser;

    ImageAdapter(Context context, int i){
        this.context=context;
        nowwho = i;

        if(i == SystemMain.justuser)
            user = UserData.getInstance();
        else if(i == SystemMain.justfuser)
            fuser = FriendData.getInstance();
    }
    @Override
    public int getCount() {
        if(nowwho == SystemMain.justuser)
            return user.getGalImages().length;
        else
            return fuser.getGalImages().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
//        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);

//        imageView.setPadding(padding, padding, padding, padding);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(nowwho == SystemMain.justuser)
            imageView.setImageBitmap(user.getGalImages()[position]);
        else
            imageView.setImageBitmap(fuser.getGalImages()[position]);

        ((ViewPager) container).addView(imageView, 0);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
