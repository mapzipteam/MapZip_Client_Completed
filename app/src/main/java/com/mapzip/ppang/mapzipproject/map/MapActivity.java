package com.mapzip.ppang.mapzipproject.map;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.FriendData;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.ReviewActivity;
import com.mapzip.ppang.mapzipproject.SystemMain;
import com.mapzip.ppang.mapzipproject.UserData;
import com.mapzip.ppang.mapzipproject.network.MyVolley;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mapzip.ppang.mapzipproject.map.Location.SEOUL;
import static com.mapzip.ppang.mapzipproject.R.id.map;


public class MapActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener, /*�������� �������� ������*/NMapOverlayManager.OnCalloutOverlayListener {
    public static final String API_KEY = "1205a9af6f6c01148e2d24a2f39c03de";

    // toast
    private View layout_toast;
    private TextView text_toast;

    private NMapView mMapView = null;

    private UserData user;
    private FriendData fuser;

    NMapController mMapController = null;

    LinearLayout MapContainer;

    NGeoPoint current_point = SEOUL;

    /////////////////////// private int GU_NUM = 0;
    //���⼭���� �������� ������

    NMapViewerResourceProvider mMapViewerResourceProvider = null;

    NMapOverlayManager mOverlayManager;

    NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if ((getIntent().getStringExtra("fragment_id").equals("home")) || (getIntent().getStringExtra("fragment_id").equals("friend_home")))
                GetMapDetail(item.getId());
        }

        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (item != null) {
                Log.i("NMAP", "onFocusChanged: " + item.toString());
            } else {
                Log.i("NMAP", "onFocusChanged: ");
            }
        }

    };

    NMapPOIdataOverlay poiDataOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        user = UserData.getInstance();
        fuser = FriendData.getInstance();

        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        setContentView(R.layout.activity_map);

        ////////////////////GU_NUM = getIntent().getExtras().getInt("location");
        //�����̰� �ȵɰͰ���........����current_point =(NGeoPoint)(getIntent().getExtras().getInt("location"));
        ///�ؼ��� �ڵ� int b = getIntent().getExtras().getInt("location");

        //�̰� �غ���!!!
        current_point = new NGeoPoint(getIntent().getExtras().getDouble("LNG"), getIntent().getExtras().getDouble("LAT"));

        //gȮ�ο� �佺Ʈ
        // Toast.makeText(getApplicationContext(),"����ȭ�鿡�� ���� LNG : "+getIntent().getExtras().getDouble("LNG"),Toast.LENGTH_LONG).show();

        // Toast.makeText(getApplicationContext(),"����ȭ�鿡�� ���� LAT : "+getIntent().getExtras().getDouble("LAT"),Toast.LENGTH_LONG).show();

        //////////////////////////////current_point.setCurrent_point(GU_NUM);

        super.onCreate(savedInstanceState);

        MapContainer = (LinearLayout) findViewById(map);


        mMapView = new NMapView(this);

        mMapView.setApiKey(API_KEY);

        MapContainer.addView(mMapView);


        mMapView.setClickable(true);

        /****/mMapView.setOnMapStateChangeListener(this);

        /****/mMapView.setOnMapViewTouchEventListener(this);

        mMapView.setBuiltInZoomControls(true, null);

        mMapController = mMapView.getMapController();
        //��������� �������� ������ �ֱ���


        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(5, mMapViewerResourceProvider);

        if (getIntent().getStringExtra("fragment_id").equals("home")) {
            try {
                poiData.beginPOIdata(0);
                String mapid = getIntent().getStringExtra("mapid");
                JSONArray jarr = new JSONArray();
                jarr = user.getMapforpinArray(Integer.parseInt(mapid));
                Log.v("맵 어레이", String.valueOf(user.getMapforpinArray(Integer.parseInt(mapid))));
                int arrnum = 0;
                for (arrnum = 0; arrnum < jarr.length(); arrnum++) {
                    poiData.addPOIitem(Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_x")), Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_y")), jarr.getJSONObject(arrnum).getString("store_name"), markerId, 0, Integer.parseInt(jarr.getJSONObject(arrnum).getString("store_id")));
                }
                poiData.endPOIdata();
            } catch (JSONException ex) {
                Log.v("맵액티비티", "JSONEX");

            }
        } else if (getIntent().getStringExtra("fragment_id").equals("friend_home")) {
            try {
                poiData.beginPOIdata(0);
                String mapid = getIntent().getStringExtra("mapid");
                JSONArray jarr = new JSONArray();
                jarr = fuser.getMapforpinArray(Integer.parseInt(mapid));
                Log.v("맵 어레이", String.valueOf(fuser.getMapforpinArray(Integer.parseInt(mapid))));
                int arrnum = 0;
                for (arrnum = 0; arrnum < jarr.length(); arrnum++) {
                    poiData.addPOIitem(Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_x")), Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_y")), jarr.getJSONObject(arrnum).getString("store_name"), markerId, 0, Integer.parseInt(jarr.getJSONObject(arrnum).getString("store_id")));
                }
                poiData.endPOIdata();
            } catch (JSONException ex) {
                Log.v("맵액티비티", "JSONEX");
            }
        } else if (getIntent().getStringExtra("fragment_id").equals("review")) {
            poiData.beginPOIdata(0);
            poiData.addPOIitem(getIntent().getDoubleExtra("store_x", 0.0), getIntent().getDoubleExtra("store_y", 0.0), getIntent().getStringExtra("store_name"), markerId, 0);
            poiData.endPOIdata();
        }

        poiDataOverlay = /**/mOverlayManager.createPOIdataOverlay(poiData, null);

        //poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.showAllItems();

        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        /**/
        mOverlayManager.setOnCalloutOverlayListener((NMapOverlayManager.OnCalloutOverlayListener) this);


        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);
    }


    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

            if (overlayItem != null) {
                String title = overlayItem.getTitle();

                if (title != null && title.length() > 0) {
                    return new NMapCalloutCustomOverlayView(MapActivity.this, itemOverlay, overlayItem, itemBounds);
                }
            }

            return null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
        if (errorInfo == null) {
            mMapController.setMapCenter(current_point, 9);
        } else {
            android.util.Log.e("NMAP", "onMapInitHandler : error=" + errorInfo.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onLongPressCanceled(NMapView nMapView) {

    }

    @Override
    public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

    }

    @Override
    public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0, NMapOverlayItem
            arg1, Rect arg2) {
        return new NMapCalloutBasicOverlay(arg0, arg1, arg2);
    }

//    public NGeoPoint setCurrent_point(int gu_num)
////    {
////
//////        if(gu_num==1)   {   current_point=GANGNAMGU;    }
////////        else if(gu_num==)   {   current_point=; }
//////////    }


    public void GetMapDetail(int poiid) {
        RequestQueue queue = MyVolley.getInstance(getApplicationContext()).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            if ((getIntent().getStringExtra("fragment_id").equals("home")))
                obj.put("userid", user.getUserID());
            else if ((getIntent().getStringExtra("fragment_id").equals("friend_home")))
                obj.put("userid", fuser.getUserID());
            obj.put("store_id", poiid);

            Log.v("MapActivity 제이손 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_MAPTOREVIEW_URL,
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

                Log.v("MapActivity", response.toString());

                try {
                    if (response.getString("state").equals("702")) {

                        JSONArray jarr = response.getJSONArray("map_detail");
                        JSONObject obj = jarr.getJSONObject(0);

                        if ((getIntent().getStringExtra("fragment_id").equals("home")))
                        {
                            user.initMapData();
                            user.setMapData(obj.getString("store_id"), obj.getString("map_id"), obj.getString("store_contact"), obj.getString("review_text"), obj.getString("review_emotion"), obj.getString("store_address"), obj.getString("store_name"));
                        }
                        else if ((getIntent().getStringExtra("fragment_id").equals("friend_home")))
                        {
                            fuser.initMapData();
                            fuser.setMapData(obj.getString("store_id"), obj.getString("map_id"), obj.getString("store_contact"), obj.getString("review_text"), obj.getString("review_emotion"), obj.getString("store_address"), obj.getString("store_name"));
                        }

                        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                        intent.putExtra("fragment_id",getIntent().getStringExtra("fragment_id"));
                        intent.putExtra("image_num", obj.getString("image_num"));
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    Log.v("에러", "제이손");
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

                    Log.e("MapActivity", error.getMessage());
                } catch (NullPointerException ex) {
                    // toast
                    Log.e("MapActivity", "nullpointexception");
                }
            }
        };
    }

}
