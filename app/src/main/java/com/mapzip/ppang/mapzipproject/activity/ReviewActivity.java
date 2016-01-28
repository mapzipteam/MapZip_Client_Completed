package com.mapzip.ppang.mapzipproject.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.adapter.ImageAdapter;
import com.mapzip.ppang.mapzipproject.map.SearchInLocationActivity;
import com.mapzip.ppang.mapzipproject.model.FriendData;
import com.mapzip.ppang.mapzipproject.model.MapData;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Song  Ji won on 2015-07-31.
 */
public class ReviewActivity extends Activity {
    // toast
    private View layout_toast;
    private TextView text_toast;

    // user Data
    private UserData user;
    private FriendData fuser;
    private boolean userlock = false; // true = friend's, false = mine

    // review Data
    private MapData mapData;
    private ImageView review_emotion;
    private TextView store_name;
    private TextView review_text;
    private TextView store_address;
    private TextView store_contact;

    // image
    private ImageAdapter imageadapter;
    private ViewPager viewPager;

    // Btn
    private Button modifyBtn;
    private Button deleteBtn;

    // Loading
   // private LoadingTask loading;
   // public ProgressDialog asyncDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // toast
        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        // user Data & friend check
        user = UserData.getInstance();
        fuser = FriendData.getInstance();

        if (getIntent().getStringExtra("fragment_id").equals("friend_home"))
            userlock = true;

        // hide Btn
        modifyBtn = (Button) findViewById(R.id.modifyBtn_review);
        deleteBtn = (Button) findViewById(R.id.deleteBtn_review);

        if(userlock == true){
            modifyBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }else{
            modifyBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        }

        // review Data
        review_emotion = (ImageView) findViewById(R.id.emotion_review);
        store_name = (TextView) findViewById(R.id.name_review);
        review_text = (TextView) findViewById(R.id.text_review);
        store_address = (TextView) findViewById(R.id.address_text_review);
        store_contact = (TextView) findViewById(R.id.contact_text_review);

        // get review Data from user Data
        if (userlock == false) { // mine
            mapData = user.getMapData();
        } else { // friend's
            mapData = fuser.getMapData();
        }

        store_name.setText(mapData.getStore_name());
        review_text.setText(mapData.getReview_text());
        store_address.setText(mapData.getStore_address());
        store_contact.setText(mapData.getStore_contact());

        // set Emotion Image
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

        /*
         *  get & set Image
         */
        viewPager = (ViewPager) findViewById(R.id.pager_review);

        // set Imageadapter
        if(userlock == false)
            imageadapter = new ImageAdapter(getApplicationContext(), SystemMain.justuser);
        else
            imageadapter = new ImageAdapter(getApplicationContext(), SystemMain.justfuser);

        viewPager.setAdapter(imageadapter);
        imageadapter.notifyDataSetChanged();
    }

    // modify Btn
    public void modifyOnclick(View v) {
        Intent intent = new Intent(this, review_register.class);
        intent.putExtra("store_name", mapData.getStore_name());
        intent.putExtra("store_address", mapData.getStore_address());
        intent.putExtra("store_contact", mapData.getStore_contact());
        intent.putExtra("store_x", mapData.getStore_x());
        intent.putExtra("store_y", mapData.getStore_y());
        intent.putExtra("store_cx", 0);
        intent.putExtra("store_cy", 0);
        intent.putExtra("store_id",mapData.getStore_id());
        intent.putExtra("state", "modify");
        startActivity(intent);

        finish();
    }

    // delete Btn
    public void deleteOnclick(View v){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ReviewActivity.this);
        alert_confirm.setMessage("리뷰를 정말 삭제하시겠습니까?\n").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = MyVolley.getInstance(ReviewActivity.this).getRequestQueue();

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("user_id", user.getUserID());
                            obj.put("map_id",mapData.getMapid());
                            obj.put("store_id",mapData.getStore_id());

                            Log.v("ReviewActivity 보내기", obj.toString());
                        } catch (JSONException e) {
                            Log.v("제이손", "에러");
                        }

                        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                                SystemMain.SERVER_REVIEWDELETE_URL,
                                obj,
                                createMyReqSuccessListener(),
                                createMyReqErrorListener()) {
                        };
                        queue.add(myReq);
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("ReviewActivity 받기", response.toString());

                try {
                    if(response.get("state").toString().equals("604")){
                        // toast
                        text_toast.setText("리뷰가 삭제되었습니다.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();

                        // for home fragment(map Image) refresh - @로딩 필요
                        Resources res;
                        res = getResources();
                        int mapid = Integer.parseInt(mapData.getMapid());
                        int pingcount = user.getPingCount(mapid,mapData.getGu_num());
                        user.setReviewCount(mapid, mapData.getGu_num(), pingcount - 1);

                        if((pingcount-1) == 0){ // no reivew check
                            int checknonzero = 0;
                            for(int c=1; c<=SystemMain.SeoulGuCount; c++){
                                if(user.getPingCount(mapid,c) != 0){
                                    checknonzero = 1;
                                    break;
                                }
                            }
                            if(checknonzero == 0)
                                user.setMapforpinNum(mapid,2);
                        }

                        // map Image reload
                        user.setMapImage(mapid, res);
                        user.setMapmetaNum(1);

                        // for map activity(pin) refresh
                        JSONArray narray = user.getMapforpinArray(mapid);
                        for(int i=0; i<narray.length(); i++){
                            if(narray.getJSONObject(i).getString("store_id").equals(mapData.getStore_id()) == true){
                                narray = removeJsonObjectAtJsonArrayIndex(narray,i);
                            }
                        }
                        user.setMapforpinArray(narray, mapid);
                        user.setMapRefreshLock(false);

                        finish();
                    }
                } catch (JSONException ex) {
                    Log.v("제이손", "에러");
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("ReviewActivity", error.getMessage());
                }catch (NullPointerException ex) {
                    Log.e("ReviewActivity", "nullpointexception");
                }
            }
        };
    }

    // for array item remove
    public static JSONArray removeJsonObjectAtJsonArrayIndex(JSONArray source, int index) throws JSONException {
        if (index < 0 || index > source.length() - 1) {
            throw new IndexOutOfBoundsException();
        }

        final JSONArray copy = new JSONArray();
        for (int i = 0, count = source.length(); i < count; i++) {
            if (i != index) copy.put(source.get(i));
        }
        return copy;
    }

    /*
     *  Loading

    protected class LoadingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            asyncDialog = new ProgressDialog(ReviewActivity.this);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.v("loading", "success");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (asyncDialog != null) {
                asyncDialog.dismiss();
            }
            Log.d("loading", "finish");

            // toast
            text_toast.setText("리뷰가 등록되었습니다.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout_toast);
            toast.show();
            finish();

            super.onPostExecute(result);
        }
    }
    */
}
