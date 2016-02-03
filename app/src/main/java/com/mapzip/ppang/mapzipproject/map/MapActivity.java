package com.mapzip.ppang.mapzipproject.map;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.games.GamesMetadata;
import com.google.android.gms.location.internal.LocationRequestUpdateData;
import com.mapzip.ppang.mapzipproject.adapter.ImageAdapter;
import com.mapzip.ppang.mapzipproject.model.FriendData;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.activity.ReviewActivity;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
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
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mapzip.ppang.mapzipproject.map.Location.SEOUL;
import static com.mapzip.ppang.mapzipproject.R.id.map;


public class MapActivity extends NMapActivity {

    //이 코드를 작성한 개발자 송지원입니다.
    //우선 이 코드의 맨 처음 초기버전은 안드로이드를 1도 모를때 짠 코드입니다.
    //그래서 구글링과 내이버 개발자센터 네이버 지도 API 튜토리얼을 뒤적여가며 보고 이 코드가 무슨 의미인지도 잘 모른체 쓴 부분이 많았습니다.
    //그래서 나중에 이 코드를 보니 제 코드임에도 많이 상태가 심각히 안좋음을 느끼고 2016/01/26에 코드를 새로 정리했습니다.
    //우선 이 클래스의 필드 부분에 상세히 각 객체의 용도와 무얼 하는 객체인지 써 놓았습니다.
    //그래도 부족하시다면 '네이버 개발자 센터 네이버지도 API 안드로이드용 튜토리얼' http://developer.naver.com/wiki/pages/Tutorial_Andriod 이나
    //클래스 정보가 담긴 http://developer.naver.com/wiki/pages/Android API 페이지에 가보시길 추천해드립니다.
    //아마 튜토리얼을 찬찬히 읽어보시면 이해가 되실거라고 믿어 의심치 않습니다.

    private static final String LOG_TAG = "MapActivity";
    private static final boolean DEBUG = false;

    public static final String API_KEY = "1205a9af6f6c01148e2d24a2f39c03de";


    // toast
    private View layout_toast;
    private TextView text_toast;

    // image load
    private List<Bitmap> oPerlishArray;
    private Bitmap[] bitarr;
    private Bitmap[] bitarrfornone;
    private ImageLoader imageLoader;
    private int image_num=0;

    // user data
    private UserData user;
    private FriendData fuser;


    // Map
    private NMapView mMapView = null;
    /*지도 데이터를 표시하는 클래스*/
    private NMapController mMapController = null;
    /*지도의 상태를 변경하고 컨트롤하기 위한 클래스*/


    private NMapViewerResourceProvider mMapViewerResourceProvider = null;
    /*지도 위의 오버레이 객체 드로잉에 필요한 데이터를 제공하기 위한 추상 클래스로  NMapResourceProvider를 상속 받은 객체다 */
    private NMapOverlayManager mOverlayManager = null;
    /*지도 위에 표시되는 오버레이 객체들을 관리하는 클래스*/


    // POI
    /*POI(Point Of Interest)는 법률또는 관례로 정해져있는 특정위치를 정하는 말이 아니라 서비스 제공자가 임의로 정하는 주요시설물.
    지도 위에 표시되는 다양한 지점을 POI라 부를 수 있기 때문에 특정 조건들을 가지고 검색되어 화면에 표출되는 마커들을 POI 라고 칭하고 그 마커들 각각의 정보를 통틀어 POI데이터 라고 부른다.*/
    private NMapPOIdata poiData = null;
    /*지도 위에 표시되는 POI 아이템을 관리하는 클래스*/
    private NMapPOIdataOverlay poiDataOverlay = null;
    /*여러개의 오버레이 아이템을 포함할수있는 오버레이 클래스. 그룹 오버레이 아이템을 효과적으로 처리할수있는 기능 제공*/











    //Listener customizing
    /*NMapView 객체에서 발생하는 상태변화에 대한 콜백 인터페이스*/
    private final NMapView.OnMapStateChangeListener onMapStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
            if (nMapError == null) {
                mMapController.setMapCenter(current_point, 9);
                poiDataOverlay.showAllPOIdata(0);//위에 코드와 달리 처음 지도를 불렀을때 모든 poi 플래그들리 보이도록 자동으로 축적이랑 중심을 변경 시켜주는 코드
                //mMapController.set
            } else {
                if(DEBUG) {
                    Log.e("NMAP", "onMapInitHandler : error=" + nMapError.toString());
                }
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

            if(DEBUG) {
                Log.e(LOG_TAG, "zoom level : " + i);
            }
        }

        @Override
        public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

        }
    };

    /*NMapView 객체에서 발생하는 티치이벤트에 대한 콜백 인터페이스*/
    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {
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
    };

    /*POI아이템의 선택상태 변경시 호출되는 콜백 인터페이스*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

            if(DEBUG) {
                Log.e("dSJW :" + LOG_TAG, "NMapPOIdataOVerlay.OnstateChangListener onFocusChanged");
            }

            if (nMapPOIitem != null) {
                if(DEBUG){
                    Log.i("dSJW :" + LOG_TAG, "onFocusChanged: " + nMapPOIitem.toString());
                }
            } else {
                if(DEBUG){
                    Log.i("dSJW :" + LOG_TAG, "onFocusChanged: ");
                }
            }
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

            if(DEBUG){
                Log.e("dSJW :"+ LOG_TAG, "NMapPOIdataOVerlay.OnstateChangListener onCalloutClick");
                Log.e("dSJW :"+ LOG_TAG, "NMapPOIdataOVerlay.OnstateChangListener onCalloutClick"+"\t\t+ getIntent()..getStringExtra(\"fragment_id\")toString() == "+getIntent().getStringExtra("fragment_id").toString());
            }
            if ((getIntent().getStringExtra("fragment_id").equals("home")) || (getIntent().getStringExtra("fragment_id").equals("friend_home"))) {

                GetMapDetail(nMapPOIitem.getId());
            }
        }
    };

    /*말풍선 overlay 객체 생성시 호출되는 콜백 인터페이스*/
    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {
        @Override
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
            if(DEBUG){
                Log.e("dSJW :"+ LOG_TAG, "NMApOverlayManager.OnCalloutOverlayViewListener onCreateCalloutOverlayView");
            }

            if (overlayItem != null) {
                String title = overlayItem.getTitle();

                if (title != null && title.length() > 0) {
                    return new NMapCalloutCustomOverlayView(MapActivity.this, itemOverlay, overlayItem, itemBounds);
                }
            }

            return null;
        }
    };




    NGeoPoint current_point = SEOUL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        user = UserData.getInstance();
        fuser = FriendData.getInstance();

        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        setContentView(R.layout.activity_map);


        current_point = new NGeoPoint(getIntent().getExtras().getDouble("LNG"), getIntent().getExtras().getDouble("LAT"));



        mMapView = (com.nhn.android.maps.NMapView)findViewById(R.id.map);

        mMapView.setApiKey(API_KEY);

        mMapView.setClickable(true);

        mMapView.setOnMapStateChangeListener(onMapStateChangeListener);

        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        mMapView.setBuiltInZoomControls(true, null);



        mMapController = mMapView.getMapController();

        //mMapController.setZoomLevelConstraint();



        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);




        int markerId = NMapPOIflagType.PIN;

        poiData = new NMapPOIdata(5, mMapViewerResourceProvider);

        poiData = getRightPOIdata(poiData, markerId);




        poiDataOverlay = /**/mOverlayManager.createPOIdataOverlay(poiData, null);

        //poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.showAllItems();

        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);



        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);


    }

    public NMapPOIdata getRightPOIdata(NMapPOIdata poiData, int markerId){

        String fragment_id = getIntent().getStringExtra("fragment_id");
        String mapid = getIntent().getStringExtra("mapid");//review_Fragement에서 날라오는거는 mapid 값 안써서 이부분이 에러 날수도있을것같다 확인해보자

        if (fragment_id.equals("home")) {
            try {
                poiData.beginPOIdata(0);
                //String mapid = getIntent().getStringExtra("mapid");
                JSONArray jarr = new JSONArray();
                jarr = user.getMapforpinArray(Integer.parseInt(mapid));
                if(DEBUG) {
                    Log.v(LOG_TAG+"맵 어레이", String.valueOf(user.getMapforpinArray(Integer.parseInt(mapid))));
                }
                int arrnum = 0;

                for (arrnum = 0; arrnum < jarr.length(); arrnum++) {

                    double store_x = Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_x"));
                    double store_y = Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_y"));
                    String store_name = jarr.getJSONObject(arrnum).getString("store_name");
                    int store_id = Integer.parseInt(jarr.getJSONObject(arrnum).getString("store_id"));

                    //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)
                    /*POI 아이템을 추가한다. 아이템이 표시될 좌표와 마커 Id는 필수 인자이며, title을 null로 전달하면 마커 선택 시 말풍선이 표시되지 않는다.
                    tag와 id는 마커 및 말풍선 선택 시 호출되는 콜백 인터페이스에서 사용하기 위해 전달한다.
                    객체로 추가적인 정보를 설정할 수 있다.*/

                    //poiData.addPOIitem(Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_x")), Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_y")), jarr.getJSONObject(arrnum).getString("store_name"), markerId, 0, Integer.parseInt(jarr.getJSONObject(arrnum).getString("store_id")));
                    poiData.addPOIitem(store_x, store_y, store_name, markerId, 0, store_id);
                }
                poiData.endPOIdata();
            } catch (JSONException ex) {
                if(DEBUG) {
                    Log.v(LOG_TAG+"맵액티비티", "JSONEX");
                }

            }
        } else if (fragment_id.equals("friend_home")) {
            try {
                poiData.beginPOIdata(0);
                //String mapid = getIntent().getStringExtra("mapid");
                JSONArray jarr = new JSONArray();
                jarr = fuser.getMapforpinArray(Integer.parseInt(mapid));
                if(DEBUG) {
                    Log.v(LOG_TAG+"맵 어레이", String.valueOf(fuser.getMapforpinArray(Integer.parseInt(mapid))));
                }
                int arrnum = 0;
                for (arrnum = 0; arrnum < jarr.length(); arrnum++) {

                    double store_x = Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_x"));
                    double store_y = Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_y"));
                    String store_name = jarr.getJSONObject(arrnum).getString("store_name");
                    int store_id = Integer.parseInt(jarr.getJSONObject(arrnum).getString("store_id"));

                    //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)
                    /*POI 아이템을 추가한다. 아이템이 표시될 좌표와 마커 Id는 필수 인자이며, title을 null로 전달하면 마커 선택 시 말풍선이 표시되지 않는다.
                    tag와 id는 마커 및 말풍선 선택 시 호출되는 콜백 인터페이스에서 사용하기 위해 전달한다.
                    객체로 추가적인 정보를 설정할 수 있다.*/

                    //poiData.addPOIitem(Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_x")), Double.parseDouble(jarr.getJSONObject(arrnum).getString("store_y")), jarr.getJSONObject(arrnum).getString("store_name"), markerId, 0, Integer.parseInt(jarr.getJSONObject(arrnum).getString("store_id")));
                    poiData.addPOIitem(store_x, store_y, store_name, markerId, 0, store_id);
                }
                poiData.endPOIdata();
            } catch (JSONException ex) {
                if(DEBUG) {
                    Log.v(LOG_TAG+"맵액티비티", "JSONEX");
                }
            }
        } else if (fragment_id.equals("review")) {
            poiData.beginPOIdata(0);

            double store_x = getIntent().getDoubleExtra("store_x", 0.0);
            double store_y = getIntent().getDoubleExtra("store_y", 0.0);
            String store_name = getIntent().getStringExtra("store_name");


            //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)
            /*POI 아이템을 추가한다. 아이템이 표시될 좌표와 마커 Id는 필수 인자이며, title을 null로 전달하면 마커 선택 시 말풍선이 표시되지 않는다.
            tag와 id는 마커 및 말풍선 선택 시 호출되는 콜백 인터페이스에서 사용하기 위해 전달한다.
            객체로 추가적인 정보를 설정할 수 있다.*/

            //poiData.addPOIitem(getIntent().getDoubleExtra("store_x", 0.0), getIntent().getDoubleExtra("store_y", 0.0), getIntent().getStringExtra("store_name"), markerId, 0);
            poiData.addPOIitem(store_x, store_y, store_name, markerId, 0);

            poiData.endPOIdata();
        }

        return poiData;
    }


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
    protected void onResume() {
        super.onResume();

        if(DEBUG){
            Log.v("맵 온리쥼","스타트");
        }

        if(user.isMapRefreshLock() == true)
            return;



        mOverlayManager.removeOverlay(poiDataOverlay);
        //poiDataOverlay.setHidden(true);


        int markerId = NMapPOIflagType.PIN;

        poiData = new NMapPOIdata(5, mMapViewerResourceProvider);

        poiData = getRightPOIdata(poiData, markerId);



        poiDataOverlay = /**/mOverlayManager.createPOIdataOverlay(poiData, null);

        //poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.showAllItems();

        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        user.setMapRefreshLock(true);

        if(DEBUG) {
            Log.v("맵 온리쥼", "엔드");
        }
    }



    public void GetMapDetail(int poiid) {

        RequestQueue queue = MyVolley.getInstance(getApplicationContext()).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            if ((getIntent().getStringExtra("fragment_id").equals("home")))
                obj.put("userid", user.getUserID());
            else if ((getIntent().getStringExtra("fragment_id").equals("friend_home")))
                obj.put("userid", fuser.getUserID());
            obj.put("store_id", poiid);

            if(DEBUG) {
                Log.v(LOG_TAG+"MapActivity 제이손 보내기", obj.toString());
            }
        } catch (JSONException e) {
            if(DEBUG) {
                Log.v(LOG_TAG+"제이손", "에러");
            }
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

                try {

                    if (response.getString("state").equals("702")) {

                        JSONArray jarr = response.getJSONArray("map_detail");
                        JSONObject obj = jarr.getJSONObject(0);

                        if ((getIntent().getStringExtra("fragment_id").equals("home")))
                        {
                            user.initMapData();
                            user.setMapData(obj.getString("store_id"), obj.getString("map_id"), obj.getString("store_contact"), obj.getString("review_text"), obj.getString("review_emotion"), obj.getString("store_address"), obj.getString("store_name"), obj.getString("gu_num"),obj.getString("image_num"));
                        }
                        else if ((getIntent().getStringExtra("fragment_id").equals("friend_home")))
                        {
                            fuser.initMapData();
                            fuser.setMapData(obj.getString("store_id"), obj.getString("map_id"), obj.getString("store_contact"), obj.getString("review_text"), obj.getString("review_emotion"), obj.getString("store_address"), obj.getString("store_name"));
                        }

                        // get default image
                        oPerlishArray = new ArrayList<Bitmap>();

                        Bitmap noimage = drawableToBitmap(getResources().getDrawable(R.drawable.noimage));

                        oPerlishArray.add(noimage);
                        bitarrfornone = new Bitmap[oPerlishArray.size()];
                        oPerlishArray.toArray(bitarrfornone); // fill the array

                        // set default image
                        if (getIntent().getStringExtra("fragment_id").equals("friend_home")) {
                            fuser.inputGalImages(bitarrfornone);
                        } else {
                            user.inputGalImages(bitarrfornone);
                        }

                        image_num = Integer.parseInt(obj.getString("image_num"));

                        // if image exist, get & set Image
                        if (image_num != 0) {

                            imageLoader = MyVolley.getInstance(getApplicationContext()).getImageLoader();

                            for (int i = 0; i < image_num; i++) {
                                if(DEBUG) {
                                    Log.v(LOG_TAG+"imagenum", String.valueOf(i));
                                }

                                // if first, clear array. (remove noimage)
                                if (i == 0) {
                                    oPerlishArray.clear();
                                    bitarr = new Bitmap[image_num];
                                }

                                if (getIntent().getStringExtra("fragment_id").equals("friend_home")) {

                                    imageLoad(i, SystemMain.SERVER_ROOT_URL + "/client_data/client_" + fuser.getUserID() + "_" + fuser.getMapData().getMapid() + "_" + fuser.getMapData().getStore_id() + "/image" + String.valueOf(i) + ".jpg");

                                } else {

                                    String url = SystemMain.SERVER_ROOT_URL + "/client_data/client_" + user.getUserID() + "_" + user.getMapData().getMapid() + "_" + user.getMapData().getStore_id() + "/image" + String.valueOf(i) + ".jpg";

                                    if(user.isAfterModify()) {

                                        MyVolley.getInstance(getApplicationContext()).clearCache();
                                    }
                                    user.setAfterModify(false);

                                    imageLoad(i, url);
                                }
                            }
                        }else {
                            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                            intent.putExtra("fragment_id", getIntent().getStringExtra("fragment_id"));
                            startActivity(intent);
                            onResume();

                        }
                    }
                } catch (JSONException e) {
                    if(DEBUG){ Log.v(LOG_TAG+"에러", "제이손");}
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

                    if(DEBUG){ Log.e(LOG_TAG+"MapActivity", error.getMessage());}
                } catch (NullPointerException ex) {
                    // toast
                    if(DEBUG){ Log.e(LOG_TAG+"MapActivity", "nullpointexception");}
                }
            }
        };
    }

    // get review Image from server
    public void imageLoad(final int nownum, String imageURL) {
        if(DEBUG){ Log.v(LOG_TAG+"imageLoader", "함수진입");}

        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if (response != null && response.getBitmap() != null) {

                    if (DEBUG) {
                        Log.v(LOG_TAG+"이미지이미지", response.getRequestUrl().toString());
                    }

                    // add image to array
                    oPerlishArray.add(response.getBitmap());
                    if (DEBUG) {
                        Log.v(LOG_TAG+"리스폰스", response.getBitmap().toString());
                    }
                    // set image to user Data, if receive completed.
                    if (image_num == oPerlishArray.size()) {

                        //bitarr = new Bitmap[image_num];
                        oPerlishArray.toArray(bitarr); // fill the array

                        if (!getIntent().getStringExtra("fragment_id").equals("friend_home")) {

                            user.inputGalImages(bitarr);
                            for (int i = 0; i < image_num; i++) {
                                if (DEBUG) {
                                    Log.v(LOG_TAG+"갤러리", user.getGalImages()[i].toString());
                                }
                            }
                        } else {

                            fuser.inputGalImages(bitarr);
                            for (int i = 0; i < image_num; i++) {
                                if (DEBUG) {
                                    Log.v(LOG_TAG+"갤러리", fuser.getGalImages()[i].toString());
                                }
                            }
                        }

                        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                        intent.putExtra("fragment_id", getIntent().getStringExtra("fragment_id"));
                        startActivity(intent);
                    }

                    if (DEBUG) {
                        Log.v(LOG_TAG+"imageLoad completed", String.valueOf(nownum));
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (DEBUG) {
                    Log.v(LOG_TAG+"imageLoader", "에러");
                }
            }
        });

    }

    // drawable to bitmap
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
