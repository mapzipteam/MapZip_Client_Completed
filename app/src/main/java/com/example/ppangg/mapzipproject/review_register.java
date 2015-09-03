package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ppangg.mapzipproject.model.MapData;
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
    final int REQ_CODE_SELECT_IMAGE = 100;

    private UserData user;

    private Button findImage;
    private Button enrollBtn;
    private Button cancelBtn;

    private TextView titleText;
    private TextView addressText;
    private TextView contactText;

    private EditText directEdit;
    private SeekBar seekbar;
    private ImageView emotion;
    private TextView oneText;
    private List<Uri> Uriarr;
    private Uri uriarray[];
    private Uri image_uri;
    private List<Bitmap> oPerlishArray;
    private int arrnum = 0;

    // map spinner
    private ArrayList<String> mapsppinerList; // map name
    private Spinner mapspinner;
    private ArrayAdapter mapadapter;

    // 보낼 정보
    private MapData mapData = new MapData();

    private int imagenum = 0;

    private File mfile;

    // toast
    private View layout_toast;
    private TextView text_toast;

    private View thisview;

    private ImageAdapter imageadapter;
    private Bitmap noimage;
    private Bitmap[] bitarr;
    private ViewPager viewPager;

    private boolean oncreatelock = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_regi);
        user = UserData.getInstance();

        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        mapData.setStore_x(getIntent().getDoubleExtra("store_x",0));
        mapData.setStore_y(getIntent().getDoubleExtra("store_y", 0));
        mapData.setStore_cx(getIntent().getIntExtra("store_cx", 0));
        mapData.setStore_cy(getIntent().getIntExtra("store_cy", 0));
        mapData.setStore_name(getIntent().getStringExtra("store_name"));
        mapData.setStore_address(getIntent().getStringExtra("store_address"));
        mapData.setStore_contact(getIntent().getStringExtra("store_contact"));
        mapData.setGu_num(getGunum());

        titleText = (TextView) findViewById(R.id.name_review_regi);
        addressText = (TextView) findViewById(R.id.address_txt_review_regi);
        contactText = (TextView) findViewById(R.id.contact_txt_review_regi);

        titleText.setText(mapData.getStore_name());
        addressText.setText(mapData.getStore_address());
        contactText.setText(mapData.getStore_contact());

        imagenum = 0;

        arrnum = 0;

        Uriarr = new ArrayList<Uri>();

        noimage = drawableToBitmap(getResources().getDrawable(R.drawable.noimage));
        oPerlishArray = new ArrayList<Bitmap>();
        oPerlishArray.add(noimage);

        viewPager = (ViewPager) findViewById(R.id.pager_review_regi);

        bitarr = new Bitmap[oPerlishArray.size()];
        oPerlishArray.toArray(bitarr); // fill the array
        user.inputGalImages(bitarr);
        imageadapter = new ImageAdapter(this);
        viewPager.setAdapter(imageadapter);

        /*
        user.inputGalImages(noimagearr);
        imageadapter.notifyDataSetChanged();
*/
        mapsppinerList = new ArrayList<String>();
        try {
            for (int i = 0; i < user.getMapmetaArray().length(); i++) {
                mapsppinerList.add(user.getMapmetaArray().getJSONObject(i).getString("title"));
            }
        }catch (JSONException ex){

        }

        // map name
        mapspinner = (Spinner) findViewById(R.id.spinner_review);
        mapadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mapsppinerList);
        mapadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapspinner.setAdapter(mapadapter);

        // map select
        mapspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject mapmeta = null;
                    mapmeta = user.getMapmetaArray().getJSONObject(position);
                    mapData.setMapid(mapmeta.get("map_id").toString());
                    Log.v("mappid", mapData.getMapid());

                } catch (JSONException ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        oneText = (TextView) findViewById(R.id.spinner_text_review_regi);
        seekbar = (SeekBar) findViewById(R.id.emotionBar_review_regi);
        emotion = (ImageView) findViewById(R.id.emotion_review_regi);
        emotion.setImageResource(R.drawable.sample_emotion0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mapData.setReview_emotion(progress);

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
                    mapData.setReview_text(getResources().getStringArray(R.array.spinner_review_regi)[position]);
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
/*
                user.inputGalImages(noimagearr);
                imageadapter.notifyDataSetChanged();
*/

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


        Log.v("resultCode",String.valueOf(resultCode));
        //Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Log.d("image", "data.getData() : " + data.getData());
                    image_uri = data.getData();
                    Uriarr.add(image_uri);

                    if (oncreatelock == false) {
                        oPerlishArray.clear();
                        oncreatelock = true;
                    }
                    oPerlishArray.add(image_bitmap);
                    bitarr = new Bitmap[oPerlishArray.size()];
                    oPerlishArray.toArray(bitarr); // fill the array
                    user.inputGalImages(bitarr);

                    imageadapter = new ImageAdapter(this);
                    viewPager.setAdapter(imageadapter);

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
            if (mapData.getReview_text().isEmpty())
                mapData.setReview_text(directEdit.getText().toString());
            Log.v("직접입력", directEdit.getText().toString());

            obj.put("userid", user.getUserID());
            obj.put("map_id", mapData.getMapid());
            obj.put("store_x", mapData.getStore_x());
            obj.put("store_y", mapData.getStore_y());
            obj.put("store_cx", mapData.getStore_cx());
            obj.put("store_cy", mapData.getStore_cy());
            obj.put("store_name", mapData.getStore_name());
            obj.put("store_address", mapData.getStore_address());
            obj.put("store_contact", mapData.getStore_contact());
            obj.put("review_emotion", mapData.getReview_emotion());
            obj.put("review_text", mapData.getReview_text());
            obj.put("image_num",Uriarr.size());
            obj.put("gu_num",mapData.getGu_num());

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
            obj.put("userid", user.getUserID());
            obj.put("map_id", mapData.getMapid());
            obj.put("store_name", mapData.getStore_name());
            obj.put("store_cx", mapData.getStore_cx());
            obj.put("store_cy", mapData.getStore_cy());

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
                    } else if (response.get("state").toString().equals("602") || response.get("state").toString().equals("621")) {
                        Log.v("리뷰저장2", "OK");

                        uriarray = new Uri[Uriarr.size()];
                        Uriarr.toArray(uriarray);

                        for (int i = 0; i < Uriarr.size(); i++)
                            DoUpload(thisview, i);
                    }
                    // toast
                    text_toast.setText("리뷰가 등록되었습니다.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout_toast);
                    toast.show();
                    finish();

                } catch (JSONException ex) {

                }
            }
        };
    }

    public void DoUpload(View v, int i) {
        mfile = getImageFile(uriarray[i]);
        if (mfile == null) {
            Toast.makeText(getApplicationContext(), "이미지가 선택되지 않았습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("mfile", mfile.toString());

//        String sdString = Environment.getExternalStorageDirectory().getPath();
//        sdString += "/DCIM/Camera/20150818_130908.jpg";
//
//        mfile = new File(sdString);

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", user.getUserID());
        params.put("map_id", mapData.getMapid());
        params.put("store_name", mapData.getStore_name());

        params.put("store_cx", String.valueOf(mapData.getStore_cx()));
        params.put("store_cy", String.valueOf(mapData.getStore_cy()));
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
                        //Log.d("volley",error.getMessage());

                    }
                }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("volley", response);
/*
                user.inputGalImages(noimagearr);
                imageadapter.notifyDataSetChanged();
*/

            }
        }, mfile, params);

        Log.v("사진 보내기", mRequest.toString());

        queue.add(mRequest);

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
}
