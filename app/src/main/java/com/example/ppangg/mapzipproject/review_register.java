package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ppangg.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ppangg on 2015-08-22.
 */
public class review_register extends Activity {

    private UserData user;

    final int REQ_CODE_SELECT_IMAGE = 100;
    private Button findImage;
    private Button enrollBtn;
    private Button cancelBtn;

    private EditText directEdit;
    private SeekBar seekbar;
    private ImageView emotion;
    private TextView oneText;
    private Uri image_uri;
    private List<Bitmap> oPerlishArray;
    private int arrnum = 0;

    // 보낼 정보
    private String mapid;
    private int store_x;
    private int store_y;
    private String store_name;
    private String store_address;
    private String store_contact;
    private int review_emotion;
    private String review_text;

    private File mfile;

    // toast
    private View layout;
    private TextView text;

    private View thisview;

    private ImageAdapter imageadapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_regi);
        user = UserData.getInstance();

        mapid = getIntent().getStringExtra("mapid");
        store_x = 123456;
        store_y = 234567;
        store_name = "storename";
        store_address = "주소";
        store_contact = "010-3061-0134";
        review_text = "";

        arrnum = 0;

        oPerlishArray = new ArrayList<Bitmap>();

        findImage = (Button) findViewById(R.id.findImage_review_regi);
        findImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_review_regi);
        imageadapter = new ImageAdapter(this);
        viewPager.setAdapter(imageadapter);

        oneText = (TextView) findViewById(R.id.spinner_text_review_regi);
        seekbar = (SeekBar) findViewById(R.id.emotionBar_review_regi);
        emotion = (ImageView) findViewById(R.id.emotion_review_regi);
        emotion.setImageResource(R.drawable.sample_emotion0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                review_emotion = progress;

                if (progress < 20)
                    emotion.setImageResource(R.drawable.sample_emotion);
                else if ((20 <= progress) && (progress < 40))
                    emotion.setImageResource(R.drawable.sample_emotion2);
                else if ((40 <= progress) && (progress < 60))
                    emotion.setImageResource(R.drawable.sample_emotion3);
                else if ((60 <= progress) && (progress < 80))
                    emotion.setImageResource(R.drawable.sample_emotion4);
                else
                    emotion.setImageResource(R.drawable.sample_emotion5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        directEdit = (EditText) findViewById(R.id.editeval_review_regi);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_review_regi);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_review_regi));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) // 직접입력
                {
                    directEdit.setVisibility(View.VISIBLE);
                    oneText.setVisibility(View.GONE);
                } else if (position == 0) {
                    directEdit.setVisibility(View.GONE);
                    oneText.setVisibility(View.VISIBLE);
                } else {
                    directEdit.setVisibility(View.GONE);
                    oneText.setVisibility(View.VISIBLE);
                    review_text = getResources().getStringArray(R.array.spinner_review_regi)[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enrollBtn = (Button) findViewById(R.id.enrollBtn_review_regi);
        enrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisview = v;
                DoReviewset(v);
            }
        });

        cancelBtn = (Button) findViewById(R.id.cancelBtn_review_regi);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Log.d("image", "data.getData() : " + data.getData());
                    image_uri = data.getData();

                    oPerlishArray.add(image_bitmap);
                    Bitmap[] bitarr = new Bitmap[oPerlishArray.size()];
                    oPerlishArray.toArray(bitarr); // fill the array
                    user.inputGalImages(bitarr);

                    imageadapter.notifyDataSetChanged();

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();

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

    public void DoReviewset(View v) {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            if(review_text.isEmpty())
                review_text = directEdit.getText().toString();
            Log.v("직접입력",directEdit.getText().toString());

            obj.put("userid",user.getUserID());
            obj.put("map_id", mapid);
            obj.put("store_x",store_x);
            obj.put("store_y",store_y);
            obj.put("store_name",store_name);
            obj.put("store_address",store_address);
            obj.put("store_contact",store_contact);
            obj.put("review_emotion",review_emotion);
            obj.put("review_text",review_text);

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

    public void DoReviewset2(View v) {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid",user.getUserID());
            obj.put("map_id", mapid);
            obj.put("store_name",store_name);
            obj.put("store_x",store_x);
            obj.put("store_y",store_y);

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



    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("review_regi 받기", response.toString());

                //home_Fragment hf = (home_Fragment) getFragmentManager().findFragmentByTag("home_fragment");
                //hf.refresh();

                try {
                    if (response.get("state").toString().equals("601")) {
                        Log.v("리뷰저장", "OK");
                        DoReviewset2(thisview);
                    } else if (response.get("state").toString().equals("602")) {
                        Log.v("리뷰저장2", "OK");
                        DoUpload(thisview);
                    }

                } catch (JSONException ex) {

                }
            }
        };
    }

    public void DoUpload(View v){
        mfile = getImageFile(image_uri);
        if(mfile==null){
            Toast.makeText(getApplicationContext(),"이미지가 선택되지 않았습니다",Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("mfile",mfile.toString());

//        String sdString = Environment.getExternalStorageDirectory().getPath();
//        sdString += "/DCIM/Camera/20150818_130908.jpg";
//
//        mfile = new File(sdString);

        Map<String,String> params = new HashMap<String,String>();
        params.put("userid", user.getUserID());
        params.put("map_id", mapid);
        params.put("store_name", store_name);

        //double x = store_x * 10000000;
        //double y = store_y * 10000000;

        params.put("store_x", String.valueOf(store_x));
        params.put("store_y", String.valueOf(store_y));

        RequestQueue queue = MyVolley.getInstance(getApplicationContext()).getRequestQueue();
        MultipartRequest mRequest = new MultipartRequest(SystemMain.SERVER_REVIEWENROLL3_URL,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"네트워크에 문제가 있습니다",Toast.LENGTH_SHORT).show();
                        Log.d("volley",error.getMessage());

                    }
                }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"이미지가 서버에 전송되었습니다",Toast.LENGTH_SHORT).show();
                Log.d("volley", response);

                finish();

            }
        },mfile,params);

        Log.v("사진 보내기",mRequest.toString());

        queue.add(mRequest);

    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.v("리뷰저장", "에러");
            }
        };
    }
}
