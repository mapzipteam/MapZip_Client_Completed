package com.mapzip.ppang.mapzipproject.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.adapter.ImageAdapter;
import com.mapzip.ppang.mapzipproject.model.MapData;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
import com.mapzip.ppang.mapzipproject.network.MultipartRequest;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by ppangg on 2015-08-22.
 */
public class review_register extends Activity {
    // state
    private int state = 0; // 0: default review enroll, 1: modify review
    private String primap_id;

    // toast
    private View layout_toast;
    private TextView text_toast;

    // user Data
    private UserData user;

    // UI
    private TextView titleText;
    private TextView addressText;
    private TextView contactText;
    private Button enrollBtn;
    private Button modifyBtn;

    // Image View
    private ViewPager viewPager;
    private List<Bitmap> oPerlishArray; // Image List
    private Bitmap[] bitarr; // Image array, oPerlishArray.toArray(bitarr)
    private ImageAdapter imageadapter;
    private Bitmap noimage;
    private Bitmap[] backupbitarr;

    // Image Select
    final int REQ_CODE_SELECT_IMAGE = 100;
    private boolean oncreatelock = false; // image array clear -> false: oPerlishArray.clear() , true: x

    // Image Send
    private int serverchoice = 0; // Image send check -> 0: default, 1: only text, 2: image send in Loding Background
    private int imagenum = 0; // to identify Image -> increase in doUpload

    // modify image
    private boolean modifyedcheck = false; // image modify check -> false: no modify, true: modified
    private int afterimagenum = 0;

    // Loading
    private LoadingTask loading;
    public ProgressDialog asyncDialog;

    // review text
    private int reviewposition1; // first spinner select num
    private int reviewposition2; // second spinner select num
    private EditText directEdit;
    private TextView oneText; // '한줄평' textview

    // review emotion
    private SeekBar seekbar;
    private ImageView emotion;

    // map spinner
    private ArrayList<String> mapsppinerList; // map name
    private Spinner mapspinner;
    private ArrayAdapter mapadapter;

    // to send Review Data
    private MapData mapData = new MapData();

    // set Map Image
    private Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_review_regi);

        // toast
        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        // user Data
        user = UserData.getInstance();

        /*
         *  Init var & Setting view
         */
        res = getResources();
        loading = new LoadingTask();
        serverchoice = 0;
        imagenum = 0;

        enrollBtn = (Button) findViewById(R.id.enrollBtn_review_regi);
        modifyBtn = (Button) findViewById(R.id.modifyBtn_review_regi);

        // state
        if(getIntent().getStringExtra("state").equals("modify") == true) {
            state = 1;
            try{
            mapData = user.getMapData().clone();
            }catch (Exception ex){
                Log.v("mapData","clone ex");
            }

            backupbitarr = user.getGalImages().clone();

            primap_id = mapData.getMapid();
            Log.v("mapid",mapData.getMapid());
        }
        else
            state=0;

        if(state == 0){
            enrollBtn.setVisibility(View.VISIBLE);
            modifyBtn.setVisibility(View.GONE);
        }
        else{
            enrollBtn.setVisibility(View.GONE);
            modifyBtn.setVisibility(View.VISIBLE);
        }
        Log.v("state", String.valueOf(state));

        // setting mapData to send
        mapData.setStore_x(getIntent().getDoubleExtra("store_x", 0));
        mapData.setStore_y(getIntent().getDoubleExtra("store_y", 0));
        mapData.setStore_name(getIntent().getStringExtra("store_name"));
        mapData.setStore_address(getIntent().getStringExtra("store_address"));
        mapData.setStore_contact(getIntent().getStringExtra("store_contact"));
        mapData.setGu_num(getGunum());

        // setting View
        titleText = (TextView) findViewById(R.id.name_review_regi);
        addressText = (TextView) findViewById(R.id.address_txt_review_regi);
        contactText = (TextView) findViewById(R.id.contact_txt_review_regi);
        titleText.setText(mapData.getStore_name());
        addressText.setText(mapData.getStore_address());
        contactText.setText(mapData.getStore_contact());

        /*
         *  setting for Image
         */
        viewPager = (ViewPager) findViewById(R.id.pager_review_regi);

        oPerlishArray = new ArrayList<Bitmap>();

        if(state == 0){ // in enroll
            // no Image
            noimage = drawableToBitmap(getResources().getDrawable(R.drawable.noimage));
            oPerlishArray.add(noimage);

            bitarr = new Bitmap[oPerlishArray.size()];
            oPerlishArray.toArray(bitarr); // fill the array
            user.inputGalImages(bitarr);
        }
        imageadapter = new ImageAdapter(this, SystemMain.justuser);
        viewPager.setAdapter(imageadapter);

        /*
         *  map spinner
         */
        // get map name
        mapsppinerList = new ArrayList<String>();
        try {
            for (int i = 0; i < user.getMapmetaArray().length(); i++) {
                mapsppinerList.add(user.getMapmetaArray().getJSONObject(i).getString("title"));
            }
        } catch (JSONException ex) {
            Log.v("제이손 에러","review_regi_mapspinner");
        }

        // set map spinner
        mapspinner = (Spinner) findViewById(R.id.spinner_review);
        mapadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mapsppinerList);
        mapadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapspinner.setAdapter(mapadapter);
        if(state == 1) // in modify
                mapspinner.setSelection(Integer.parseInt(mapData.getMapid())-1);

        // map select
        mapspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // get map id to send
                    JSONObject mapmeta = null;
                    mapmeta = user.getMapmetaArray().getJSONObject(position);
                    mapData.setMapid(mapmeta.get("map_id").toString());
                    Log.v("mappid", mapData.getMapid());
                } catch (JSONException ex) {
                    Log.v("제이손 에러", "review_regi_mapspinner2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // emotion
        emotion = (ImageView) findViewById(R.id.emotion_review_regi);
        emotion.setImageResource(R.drawable.sample_emotion0); // default emotion image
        seekbar = (SeekBar) findViewById(R.id.emotionBar_review_regi);
        if(state == 1)  // in modify
        {
            int pro = mapData.getReview_emotion();
            seekbar.setProgress(pro);
            if (pro < 20)
                emotion.setImageResource(R.drawable.emotion1);
            else if ((20 <= pro) && (pro < 40))
                emotion.setImageResource(R.drawable.emotion2);
            else if ((40 <= pro) && (pro < 60))
                emotion.setImageResource(R.drawable.emotion3);
            else if ((60 <= pro) && (pro < 80))
                emotion.setImageResource(R.drawable.emotion4);
            else
                emotion.setImageResource(R.drawable.emotion5);
        }
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mapData.setReview_emotion(progress); // to send emotion

                if (progress < 20)
                    emotion.setImageResource(R.drawable.emotion1);
                else if ((20 <= progress) && (progress < 40))
                    emotion.setImageResource(R.drawable.emotion2);
                else if ((40 <= progress) && (progress < 60))
                    emotion.setImageResource(R.drawable.emotion3);
                else if ((60 <= progress) && (progress < 80))
                    emotion.setImageResource(R.drawable.emotion4);
                else
                    emotion.setImageResource(R.drawable.emotion5);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /*
         *  review text
         */
        oneText = (TextView) findViewById(R.id.spinner_text_review_regi);
        directEdit = (EditText) findViewById(R.id.editeval_review_regi);

        // first spinner
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_review_regi);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_review_regi));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // second spinner
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_review_regi2);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_review_regi2));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 15) // 직접입력
                {
                    directEdit.setVisibility(View.VISIBLE);
                    oneText.setVisibility(View.GONE);
                    spinner2.setVisibility(View.GONE);
                    reviewposition1 = position; // save selected position
                } else {
                    directEdit.setVisibility(View.GONE);
                    oneText.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.VISIBLE);
                    reviewposition1 = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 14) // 직접입력
                {
                    directEdit.setVisibility(View.VISIBLE);
                    oneText.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    reviewposition2 = position; // save selected position
                } else {
                    directEdit.setVisibility(View.GONE);
                    oneText.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    reviewposition2 = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // in modify
        if(state == 1){
            spinner.setSelection(15);
            directEdit.setText(mapData.getReview_text());

            directEdit.setVisibility(View.VISIBLE);
            oneText.setVisibility(View.GONE);
            spinner2.setVisibility(View.GONE);
            reviewposition1 = 15; // save selected position
        }
    }

    //  onResult - findImageonClick
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("resultCode", String.valueOf(resultCode));

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Uri image_uri = data.getData();
                        
                    if (oncreatelock == false || state == 1) { // 사진 여러장 일 때 or modify
                        oPerlishArray.clear();
                        oncreatelock = true;
                    }

                    //2016.01.11송지원이 바꿈
                    int maxWidth = viewPager.getWidth();
                    int maxHeight = viewPager.getHeight();

                    Bitmap resized_image_bitmap = resizeBitmapImage(image_uri, image_bitmap, maxWidth, maxHeight);


                    //2016.01.26 송지원이 추가
                    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                    Cursor cursor = managedQuery(image_uri, orientationColumn, null, null, null);
                    int orientationDegree = -1;

                    if(cursor != null && cursor.moveToFirst()){
                        orientationDegree = cursor.getInt(cursor.getColumnIndex(orientationColumn[0]));
                    }

                    Bitmap rotated_resized_image_bitmap = rotateBitmapImage(resized_image_bitmap,  orientationDegree);



                    //oPerlishArray.add(image_bitmap);
                    oPerlishArray.add(rotated_resized_image_bitmap);
                    bitarr = new Bitmap[oPerlishArray.size()];
                    oPerlishArray.toArray(bitarr);

                    // save Image in user Data
                    if (state == 1) // in modify
                    {
                        Log.v("image modify", "ok");
                        Log.v("image_length1",String.valueOf(user.getGalImages().length));
                        if((mapData.getImage_num()==0) && (afterimagenum==0))
                            user.inputGalImages(bitarr);
                        else
                            user.addGalImages(bitarr);

                        modifyedcheck = true;
                        afterimagenum++;
                        Log.v("image_length2",String.valueOf(user.getGalImages().length));
                    }
                    else
                        user.inputGalImages(bitarr);

                    serverchoice = 2;
                    imageadapter = new ImageAdapter(this,SystemMain.justuser);
                    viewPager.setAdapter(imageadapter);
                    imageadapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // in enroll Btn
    public void DoReviewset(View v) {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            if (mapData.getReview_text().isEmpty())
                mapData.setReview_text(directEdit.getText().toString());

            if(serverchoice == 2)
                mapData.setImage_num(user.getGalImages().length);

            obj.put("userid", user.getUserID());
            obj.put("map_id", mapData.getMapid());
            obj.put("store_x", mapData.getStore_x());
            obj.put("store_y", mapData.getStore_y());
            obj.put("store_name", mapData.getStore_name());
            obj.put("store_address", mapData.getStore_address());
            obj.put("store_contact", mapData.getStore_contact());
            obj.put("review_emotion", mapData.getReview_emotion());
            obj.put("review_text", mapData.getReview_text());
            obj.put("image_num", mapData.getImage_num());
            obj.put("gu_num", mapData.getGu_num());

            Log.v("review 등록 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_REVIEWENROLL_URL,
                obj,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    // in enroll Btn -> response, 이미지 있을때
    public void DoReviewset2() {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", user.getUserID());
            obj.put("map_id", mapData.getMapid());
            obj.put("store_id", mapData.getStore_id());

            Log.v("review 등록2 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_REVIEWENROLL2_URL,
                obj,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    // in modify Btn
    public void DoModifyset(View v) {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            if (mapData.getReview_text().isEmpty())
                mapData.setReview_text(directEdit.getText().toString());

            mapData.setImage_num(mapData.getImage_num()+afterimagenum);
            obj.put("user_id", user.getUserID());
            obj.put("map_id", mapData.getMapid());
            obj.put("review_emotion", mapData.getReview_emotion());
            obj.put("review_text", mapData.getReview_text());
            obj.put("store_id",getIntent().getStringExtra("store_id"));
            obj.put("image_num", mapData.getImage_num());
            Log.v("image_num", String.valueOf(mapData.getImage_num()));

            Log.v("review 수정 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_REVIEWMODIFY_URL,
                obj,
                createMyReqSuccessListener_modify(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    // in modify Btn -> response, 이미지 있을때 (in noimage)
    public void DoModifyset2() {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", user.getUserID());
            obj.put("map_id", mapData.getMapid());
            obj.put("store_id", mapData.getStore_id());

            Log.v("review 등록2 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_REVIEWENROLL2_URL,
                obj,
                createMyReqSuccessListener_modify(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    // for modify response
    private Response.Listener<JSONObject> createMyReqSuccessListener_modify() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("review_modify 받기", response.toString());
                try {
                    if(response.getInt("state") == SystemMain.CLIENT_REVIEW_DATA_UPDATE_SUCCESS) { // 607
                        user.setModifystate(true);

                        // if Map Id modified (지도 변경시)
                        if(primap_id.equals(mapData.getMapid()) == false){
                            int pmap_id = Integer.parseInt(primap_id); // 수정 전
                            int nmap_id = Integer.parseInt(mapData.getMapid()); // 수정 후 map_id
                            int gu_num = mapData.getGu_num();
                            int nmapnocheck = 0; // 0: 리뷰있음 1: no review

                            if(user.getPingCount(nmap_id,gu_num) == 0){ // no review check in now map
                                int checknonzero = 0;
                                for(int c=1; c<=SystemMain.SeoulGuCount; c++){
                                    if(user.getPingCount(nmap_id,c) != 0){
                                        checknonzero = 1;
                                        break;
                                    }
                                }
                                if(checknonzero == 0)
                                    nmapnocheck = 1;
                            }

                            // mapforpinNum, PingCount modify, if review count is 0
                            user.setReviewCount(pmap_id, gu_num,user.getPingCount(pmap_id,gu_num)-1);
                            user.setReviewCount(nmap_id, gu_num, user.getPingCount(nmap_id,gu_num)+1);

                            if(user.getPingCount(pmap_id,gu_num) == 0){ // no review check in primap
                                int checknonzero = 0;
                                for(int c=1; c<=SystemMain.SeoulGuCount; c++){
                                    if(user.getPingCount(pmap_id,c) != 0){
                                        checknonzero = 1;
                                        break;
                                    }
                                }
                                if(checknonzero == 0)
                                    user.setMapforpinNum(pmap_id,2);
                            }

                            // map Image reload
                            user.setMapImage(pmap_id, res);
                            user.setMapImage(nmap_id, res);
                            user.setMapmetaNum(1);

                            // mapforpinArray modify
                            JSONArray farray = user.getMapforpinArray(pmap_id); // 이전 지도에서 리뷰디테일 삭제
                            JSONObject moveobj = new JSONObject();
                            for(int i=0; i<farray.length(); i++){
                                if(farray.getJSONObject(i).getString("store_id").equals(mapData.getStore_id()) == true){
                                    moveobj = farray.getJSONObject(i);
                                    farray = removeJsonObjectAtJsonArrayIndex(farray,i);
                                }
                            }
                            user.setMapforpinArray(farray, pmap_id);
                            Log.v("moveobj", moveobj.toString());

                            if(nmapnocheck == 1) { // 옮길 지도에 리뷰 정보가 없었을때
                                JSONArray sarray = new JSONArray();
                                sarray.put(sarray.length(),moveobj);
                                user.setMapforpinArray(sarray, nmap_id);
                                user.setMapforpinNum(nmap_id, 1);
                                Log.v("1 sarray", sarray.toString());
                            }
                            else{ // 리뷰 정보가 있었을때
                                if(user.getMapforpinNum(nmap_id) != 0) {
                                    JSONArray sarray = user.getMapforpinArray(nmap_id);
                                    Log.v("0_1 sarray", sarray.toString());
                                    sarray.put(sarray.length(), moveobj);
                                    user.setMapforpinArray(sarray, nmap_id);
                                    Log.v("0 sarray", sarray.toString());
                                }
                            }
                            user.setMapRefreshLock(false);
                        }

                        if(modifyedcheck == true){
                            Log.v("리뷰수정", "OK");
                            if((mapData.getImage_num()-afterimagenum) == 0)
                                DoModifyset2();
                            else{
                                serverchoice = 2;
                                loading.execute();
                                // 2번째통신 이미지갯수만큼 반복
                            }
                        }else{
                            // toast
                            text_toast.setText("리뷰가 수정되었습니다.");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout_toast);
                            toast.show();
                            
                            finish();
                        }
                    }
                    else if ((response.getInt("state") == SystemMain.CLIENT_REVIEW_IMAGE_MKDIR_SUCCESS) || (response.getInt("state") == SystemMain.CLIENT_REVIEW_IMAGE_MKDIR_EXIST)){ // 602 || 621
                        serverchoice = 2;
                        loading.execute();
                        // 2번째통신 이미지갯수만큼 반복
                    }
                }catch (JSONException e){
                    Log.e("제이손","에러");
                }
            }
        };
    }

    // for enroll response
    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("review_regi 받기", response.toString());

                try {
                    if (response.get("state").toString().equals("601")) {
                        // 1번째 통신 성공
                        Log.v("리뷰저장", "OK");
                        mapData.setStore_id(response.getString("store_id"));
                        if (mapData.getImage_num() != 0)
                            DoReviewset2(); // 이미지 있으면 2번째 통신 시작
                        else {
                            serverchoice = 1; // no image
                            loading.execute();
                        }
                    } else if (response.get("state").toString().equals("602") || response.get("state").toString().equals("621")) {
                        // 2번째통신 성공
                        Log.v("리뷰저장2", "OK");
                        serverchoice = 2;
                        loading.execute();
                        // 3번째통신 이미지갯수만큼 반복
                    }
                    if (response.get("state").toString().equals("612")) {
                        //1번째 통신에서 중복가게 걸러내기
                        // toast
                        text_toast.setText("이미 등록 된 가게입니다.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout_toast);
                        toast.show();
                    }
                } catch (JSONException ex) {
                    Log.e("제이손","에러");
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

                    Log.e("review_register", error.getMessage());
                } catch (NullPointerException ex) {
                    // toast
                    Log.e("review_register", "nullpointexception");
                }
            }
        };
    }

    /*
     * for Image
     */
    // get image uri, use in DoUpload
    private File getImageFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (uri == null) {
            return null;
        }
        Cursor mCursor = getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor == null || mCursor.getCount() < 1) {
            return null;
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return new File(path);
    }

    // image upload
    public void DoUpload(final int i) {
        Log.v("에러치크","2");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SystemMain.SERVER_REVIEWENROLL3_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Showing toast message of the response
                        Log.v("이미지 업로드",s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Showing toast
                        Log.v("이미지 업로드 에러", String.valueOf(volleyError));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                Log.v("길이길이",String.valueOf(user.getGalImages().length));
                String image = getStringImage(user.getGalImages()[i]);
                Log.v("image string",image);
                Log.v("image 길이", String.valueOf(image.length()));

                Map<String, String> params = new Hashtable<String, String>();
                params.put("image_string",image);
                params.put("userid", user.getUserID());
                params.put("map_id", mapData.getMapid());
                params.put("store_id", mapData.getStore_id());
                params.put("image_name", "image" + String.valueOf(imagenum));
                imagenum++;

                Log.v("param",params.toString());

                Log.v("에러치크","1");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

        /*
        mfile = getImageFile(uriarray[i]);
        if (mfile == null) {
            Toast.makeText(getApplicationContext(), "이미지가 선택되지 않았습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", user.getUserID());
        params.put("map_id", mapData.getMapid());
        params.put("store_id", mapData.getStore_id());
        params.put("image_name", "image" + String.valueOf(imagenum));
        imagenum++;

        RequestQueue queue = MyVolley.getInstance(getApplicationContext()).getRequestQueue();
        MultipartRequest mRequest = new MultipartRequest(SystemMain.SERVER_REVIEWENROLL3_URL,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // toast
                        text_toast.setText("인터넷 연결이 필요합니다.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();
                    }
                }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("volley", response);
            }
        }, mfile, params);
        Log.v("사진 보내기", mRequest.toString());
        queue.add(mRequest);
        */
    }

    /*
     * util
     */
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

    // resized bitmap
    public Bitmap resizeBitmapImage(Uri image_uri, Bitmap bmpSource, int maxWidth, int maxHeight){
        int iWidth = bmpSource.getWidth();
        int iHeight = bmpSource.getHeight();
        int newWidth = iWidth;
        int newHeight = iHeight;
        double rate = 0.0f;

        Log.d("dSJW", "iWidth :" + iWidth + "\tiHeight : " + iHeight + "\tmaxWidth : " + maxWidth + "\tmaxHeight : " + maxHeight);
        rate = Math.max((double)iWidth/maxWidth, (double)iHeight/maxHeight);
        Log.d("dSJW", (double) iWidth / maxWidth + "\t\t" + (double) iHeight / maxHeight + "\t\t" + rate);
        if(rate <= 1){
            Log.v("dSJW", "그대로 가로 : " + bmpSource.getWidth() + "\t\t그대로 세로 : " + bmpSource.getHeight());
            return bmpSource;
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int)rate+1;

            Log.d("dSJW","int)rate : "+((int)rate+1) );

            Bitmap bitmap_src = BitmapFactory.decodeFile(getPathFromUri(image_uri), options);

            Bitmap bitmap_resized = Bitmap.createScaledBitmap(bitmap_src, /*maxWidth, maxHeight*/(int)(iWidth/rate), (int)(iHeight/rate), true);

            Log.e("dSJW", "바뀌어서 가로 : " + bitmap_resized.getWidth() + "\t\t바뀌어서 세로 : " + bitmap_resized.getHeight());
            return bitmap_resized;
        }
    }

    public String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();

        return path;
    }

    //이미지 degree만큼 회전 해서 return하는 함수
    public Bitmap rotateBitmapImage(Bitmap srcBmp, int degree){

        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();

        Log.d("dSJW", "아여기보시게" + degree);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap rotatedBmp = Bitmap.createBitmap(srcBmp, 0, 0, width, height, matrix, true);

        return rotatedBmp;
    }

    // get Image encoding
    public String getStringImage(Bitmap bmp){/*
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        */

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedImage = Base64.encodeToString(byte_arr, 0);

        /*
        String encodedImage="";
        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedImage = new String(byte_arr,"UTF-8");

        }catch (Exception ex){
            Log.v("에러","utf-8");
        }
        */
        return encodedImage;
    }

    // for no Image
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

    // to send Gunum
    public int getGunum() {
        int gunum = -1;
        if (mapData.getStore_address().contains("서울특별시 도봉구"))
            gunum = SystemMain.DoBong;
        else if (mapData.getStore_address().contains("서울특별시 노원구"))
            gunum = SystemMain.NoWon;
        else if (mapData.getStore_address().contains("서울특별시 강북구"))
            gunum = SystemMain.GangBuk;
        else if (mapData.getStore_address().contains("서울특별시 성북구"))
            gunum = SystemMain.SungBuk;
        else if (mapData.getStore_address().contains("서울특별시 중랑구"))
            gunum = SystemMain.ZongRang;
        else if (mapData.getStore_address().contains("서울특별시 은평구"))
            gunum = SystemMain.EunPhung;
        else if (mapData.getStore_address().contains("서울특별시 종로구"))
            gunum = SystemMain.ZongRo;
        else if (mapData.getStore_address().contains("서울특별시 동대문구"))
            gunum = SystemMain.DongDaeMon;
        else if (mapData.getStore_address().contains("서울특별시 서대문구"))
            gunum = SystemMain.SuDaeMon;
        else if (mapData.getStore_address().contains("서울특별시 중구"))
            gunum = SystemMain.Zhong;
        else if (mapData.getStore_address().contains("서울특별시 성동구"))
            gunum = SystemMain.SungDong;
        else if (mapData.getStore_address().contains("서울특별시 광진구"))
            gunum = SystemMain.GangZin;
        else if (mapData.getStore_address().contains("서울특별시 강동구"))
            gunum = SystemMain.GangDong;
        else if (mapData.getStore_address().contains("서울특별시 마포구"))
            gunum = SystemMain.MaPho;
        else if (mapData.getStore_address().contains("서울특별시 용산구"))
            gunum = SystemMain.YongSan;
        else if (mapData.getStore_address().contains("서울특별시 강서구"))
            gunum = SystemMain.GangSue;
        else if (mapData.getStore_address().contains("서울특별시 양천구"))
            gunum = SystemMain.YangChen;
        else if (mapData.getStore_address().contains("서울특별시 구로구"))
            gunum = SystemMain.GuRo;
        else if (mapData.getStore_address().contains("서울특별시 영등포구"))
            gunum = SystemMain.YongDengPo;
        else if (mapData.getStore_address().contains("서울특별시 동작구"))
            gunum = SystemMain.DongJack;
        else if (mapData.getStore_address().contains("서울특별시 금천구"))
            gunum = SystemMain.GemChun;
        else if (mapData.getStore_address().contains("서울특별시 관악구"))
            gunum = SystemMain.GanAk;
        else if (mapData.getStore_address().contains("서울특별시 서초구"))
            gunum = SystemMain.SeoCho;
        else if (mapData.getStore_address().contains("서울특별시 강남구"))
            gunum = SystemMain.GangNam;
        else if (mapData.getStore_address().contains("서울특별시 송파구"))
            gunum = SystemMain.SongPa;

        return gunum;
    }

    // setting review text & emotion
    public int reviewtextset() {
        if (mapData.getReview_emotion() == 0) { // emotion not selected
            // toast
            text_toast.setText("이모티콘을 선택해주세요.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout_toast);
            toast.show();

            return -1;
        } else if ((reviewposition1 == 0) && (reviewposition2 == 0)) { // review text not selected
            // toast
            text_toast.setText("리뷰를 작성해주세요.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout_toast);
            toast.show();

            return -1;
        } else {
            if ((reviewposition1 == 15) || (reviewposition2 == 14)) // direct review text
                mapData.setReview_text(directEdit.getText().toString());
            else { // selected review text
                String tmp = "";
                if (reviewposition1 != 0) // first spinner
                    tmp = getResources().getStringArray(R.array.spinner_review_regi)[reviewposition1];
                if (reviewposition2 != 0) { // second spinner
                    if (reviewposition1 != 0)
                        tmp += " 하지만 " + getResources().getStringArray(R.array.spinner_review_regi2)[reviewposition2];
                    else
                        tmp += " " + getResources().getStringArray(R.array.spinner_review_regi2)[reviewposition2];
                }

                mapData.setReview_text(tmp); // set final review to send
            }
            return 1;
        }
    }

    /*
     *  Loading
     */
    protected class LoadingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            asyncDialog = new ProgressDialog(review_register.this);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (serverchoice == 1) {
            } else if (serverchoice == 2) {
/*
                if(state==1) // in modify
                    imagenum=(mapData.getImage_num()-afterimagenum);
*/
                for (int i = 0; i <mapData.getImage_num(); i++)
                    DoUpload(i);

                user.setAfterModify(true);
            }

            if(state == 0) {
                int tmp = user.getPingCount(Integer.parseInt(mapData.getMapid()), mapData.getGu_num());
                user.setReviewCount(Integer.parseInt(mapData.getMapid()), mapData.getGu_num(), tmp + 1);
                user.setMapImage(Integer.parseInt(mapData.getMapid()), res);
            }

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

            if(state == 0) {
                // toast
                text_toast.setText("리뷰가 등록되었습니다.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout_toast);
                toast.show();
            }
            else{
                // toast
                text_toast.setText("리뷰가 수정되었습니다.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout_toast);
                toast.show();
            }
            finish();
            
            super.onPostExecute(result);
        }
    }

    /*
     *  onClick Btn
     */
    // 사진찾기 버튼
    public void findImageonClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    // 사진제거 버튼
    public void deleteImageonClick(View v){
        Log.v("이미지카운트", String.valueOf(viewPager.getCurrentItem()));
        int delposition = viewPager.getCurrentItem();
        Bitmap[] fordelbitarr = user.getGalImages();

        //add userGal. removed image arr
        oPerlishArray.clear();

        if(fordelbitarr.length == 1) {
            // no Image
            noimage = drawableToBitmap(getResources().getDrawable(R.drawable.noimage));
            oPerlishArray.add(noimage);
        }
        else{
            for (int i = 0; i < fordelbitarr.length; i++)
                oPerlishArray.add(fordelbitarr[i]);
            oPerlishArray.remove(delposition);
        }
        bitarr = new Bitmap[oPerlishArray.size()];
        oPerlishArray.toArray(bitarr);
        user.inputGalImages(bitarr);

        //set imageadapter
        imageadapter = new ImageAdapter(this,SystemMain.justuser);
        viewPager.setAdapter(imageadapter);
        imageadapter.notifyDataSetChanged();

        afterimagenum--;
    }

    // 리뷰등록 버튼
    public void enrollonClick_review_regi(View v) {
            if(reviewtextset() == 1) {
                DoReviewset(v); // 서버 통신
                user.setMapforpinNum(Integer.parseInt(mapData.getMapid()), 0); // to review loading
            }
    }

    // 취소 버튼
    public void cancelonClick_review_regi(View v) {
        finish();
    }

    // 리뷰수정 버튼
    public void modifyonClick_review_regi(View v){
        reviewtextset();
        DoModifyset(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* if(modifyedcheck == true)
            user.inputGalImages(backupbitarr);*/
    }
}
