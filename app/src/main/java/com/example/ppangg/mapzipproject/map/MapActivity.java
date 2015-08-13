package com.example.ppangg.mapzipproject.map;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ppangg.mapzipproject.R;
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

import static com.example.ppangg.mapzipproject.map.Location.SEOUL;
import static com.example.ppangg.mapzipproject.R.id.map;


public class MapActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener, /*이제부턴 오버레이 아이콘*/NMapOverlayManager.OnCalloutOverlayListener
{
    public static final String API_KEY = "4ae3e1917e6279159f77684848f41423";

    private NMapView mMapView = null;

    NMapController mMapController = null;

    LinearLayout MapContainer;

    NGeoPoint current_point = SEOUL;

    /////////////////////// private int GU_NUM = 0;
    //여기서부턴 오버레이 아이템

    NMapViewerResourceProvider mMapViewerResourceProvider = null;

    NMapOverlayManager mOverlayManager;

    NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener()
    {
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item)
        {
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            startActivity(intent);
        }

        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item)
        {
            if(item != null)
            {
                Log.i("NMAP", "onFocusChanged: "+ item.toString());
            }
            else
            {
                Log.i("NMAP", "onFocusChanged: ");
            }
        }

    };

    NMapPOIdataOverlay poiDataOverlay;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ////////////////////GU_NUM = getIntent().getExtras().getInt("location");
        //ㄴㄴ이건 안될것같다........으어current_point =(NGeoPoint)(getIntent().getExtras().getInt("location"));
        ///준수형 코드 int b = getIntent().getExtras().getInt("location");

        //이거 해보자!!!
        current_point = new NGeoPoint(getIntent().getExtras().getDouble("LNG"), getIntent().getExtras().getDouble("LAT"));

        //g확인용 토스트
        Toast.makeText(getApplicationContext(),"지도화면에서 받은 LNG : "+getIntent().getExtras().getDouble("LNG"),Toast.LENGTH_LONG).show();


        Toast.makeText(getApplicationContext(),"지도화면에서 받은 LAT : "+getIntent().getExtras().getDouble("LAT"),Toast.LENGTH_LONG).show();

        //////////////////////////////current_point.setCurrent_point(GU_NUM);

        super.onCreate(savedInstanceState);

        MapContainer = (LinearLayout) findViewById(map);


        mMapView = new NMapView(this);

        mMapView.setApiKey(API_KEY);

        setContentView(mMapView);

        mMapView.setClickable(true);

        /****/mMapView.setOnMapStateChangeListener(this);

        /****/mMapView.setOnMapViewTouchEventListener(this);

        mMapView.setBuiltInZoomControls(true, null);

        mMapController = mMapView.getMapController();
        //여기까지가 오버레이 아이콘 넣기전


        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        mOverlayManager = new NMapOverlayManager(this,  mMapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(5, mMapViewerResourceProvider);

        poiData.beginPOIdata(5);

        poiData.addPOIitem(127.0716985, 37.5430318, "우마이도", markerId, 0);
        poiData.addPOIitem(126.9206943, 37.5482579, "부엉이 돈까스", markerId, 0);
        poiData.addPOIitem(126.9191225, 37.550611, "친구네 집빈날", markerId, 0);
        poiData.addPOIitem(126.9436279, 37.5402453, "박달재", markerId, 0);
        poiData.addPOIitem(127.068504, 37.5384298, "매화반점", markerId, 0);
        poiData.addPOIitem(127.027144, 37.5023993, "리골레토 사카고 피자", markerId, 0);
        poiData.addPOIitem(127.027197, 37.5021116, "카니발 피자", markerId, 0);
        poiData.addPOIitem(126.953024, 37.495872, "파동추야", markerId, 0);
        poiData.addPOIitem(126.9572027, 37.4946909, "숯가마 숯불구이", markerId, 0);

        poiData.endPOIdata();

        poiDataOverlay = /**/mOverlayManager.createPOIdataOverlay(poiData, null);

        poiDataOverlay.showAllPOIdata(0);

        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        /**/mOverlayManager.setOnCalloutOverlayListener((NMapOverlayManager.OnCalloutOverlayListener)this);


        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);
    }


    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener(){
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds ) {

            if(overlayItem != null) {
                String title = overlayItem.getTitle();

                if(title != null && title.length() > 0) {
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
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0, NMapOverlayItem arg1, Rect arg2)
    {
        return new NMapCalloutBasicOverlay(arg0, arg1, arg2);
    }

//    public NGeoPoint setCurrent_point(int gu_num)
////    {
////
//////        if(gu_num==1)   {   current_point=GANGNAMGU;    }
////////        else if(gu_num==)   {   current_point=; }
//////////    }


}
