package com.mapzip.ppang.mapzipproject;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.mapzip.ppang.mapzipproject.model.MapData;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Song  Ji won on 2015-07-31.
 */
public class ReviewActivity extends Activity {
    private UserData user;
    private FriendData fuser;
    private boolean userlock = false;

    // 리뷰 데이타
    private ImageView review_emotion;
    private TextView store_name;
    private TextView review_text;
    private TextView store_address;
    private TextView store_contact;

    // image
    private ImageAdapter imageadapter;
    private ViewPager viewPager;
    private Bitmap noimage;
    private List<Bitmap> oPerlishArray;
    private Bitmap[] bitarr;
    private Bitmap[] bitarrfornone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        user = UserData.getInstance();
        fuser = FriendData.getInstance();

        if (getIntent().getStringExtra("fragment_id").equals("friend_home"))
            userlock = true;

        ActionBar actionBar = getActionBar();
        actionBar.hide();
        review_emotion = (ImageView) findViewById(R.id.emotion_review);
        store_name = (TextView) findViewById(R.id.name_review);
        review_text = (TextView) findViewById(R.id.text_review);
        store_address = (TextView) findViewById(R.id.address_text_review);
        store_contact = (TextView) findViewById(R.id.contact_text_review);

        MapData mapData;

        if (userlock == false) {
            mapData = user.getMapData();
        } else {
            mapData = fuser.getMapData();
        }

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

        noimage = drawableToBitmap(getResources().getDrawable(R.drawable.noimage));
        oPerlishArray = new ArrayList<Bitmap>();
        oPerlishArray.add(noimage);

        bitarrfornone = new Bitmap[oPerlishArray.size()];
        oPerlishArray.toArray(bitarrfornone); // fill the array

        if (userlock == false) {
            user.inputGalImages(bitarrfornone);
        } else {
            fuser.inputGalImages(bitarrfornone);
        }

        viewPager = (ViewPager) findViewById(R.id.pager_review);

        int image_num = Integer.parseInt(getIntent().getStringExtra("image_num"));
        if (image_num != 0) {
            for (int i = 0; i < image_num; i++) {
                Log.v("imagenum", String.valueOf(i));

                if (userlock == false) {
                    imageLoad(i, SystemMain.SERVER_ROOT_URL + "/client_data/client_" + user.getUserID() + "_" + mapData.getMapid() + "_" + mapData.getStore_id() + "/image" + String.valueOf(i) + ".jpg");
                } else {
                    imageLoad(i, SystemMain.SERVER_ROOT_URL + "/client_data/client_" + fuser.getUserID() + "_" + mapData.getMapid() + "_" + mapData.getStore_id() + "/image" + String.valueOf(i) + ".jpg");
                }
            }

        } else {
            if (userlock == false) {
                user.inputGalImages(bitarrfornone);
            } else {
                fuser.inputGalImages(bitarrfornone);
            }
        }


        if(userlock == false)
            imageadapter = new ImageAdapter(getApplicationContext(), SystemMain.justuser);
        else
            imageadapter = new ImageAdapter(getApplicationContext(), SystemMain.justfuser);

        viewPager.setAdapter(imageadapter);
        imageadapter.notifyDataSetChanged();
    }

    public void imageLoad(final int nownum, String imageURL) {
        Log.v("imageLoader", "함수진입");
        if (nownum == 0)
            oPerlishArray.clear();

        ImageLoader imageLoader = MyVolley.getInstance(getApplicationContext()).getImageLoader();

        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if (response != null && response.getBitmap() != null) {
                    oPerlishArray.add(response.getBitmap());

                    bitarr = new Bitmap[oPerlishArray.size()];
                    oPerlishArray.toArray(bitarr); // fill the array
                    if (userlock == false) {
                        user.inputGalImages(bitarr);
                        imageadapter = new ImageAdapter(getApplicationContext(), SystemMain.justuser);
                    } else {
                        fuser.inputGalImages(bitarr);
                        imageadapter = new ImageAdapter(getApplicationContext(), SystemMain.justfuser);
                    }

                    viewPager.setAdapter(imageadapter);
                    imageadapter.notifyDataSetChanged();

                    Log.v("imageLoader", String.valueOf(nownum));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("imageLoader", "에러");

            }
        });

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}
