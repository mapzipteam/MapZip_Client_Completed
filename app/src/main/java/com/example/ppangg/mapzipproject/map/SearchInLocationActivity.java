package com.example.ppangg.mapzipproject.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ppangg.mapzipproject.R;
import com.example.ppangg.mapzipproject.review_register;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapProjection;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;


import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;


public class SearchInLocationActivity extends NMapActivity implements OnMapStateChangeListener {


    //default Member variable declare
    public static final String API_KEY = MapActivity.API_KEY;
    NMapView mMapView = null;
    NMapController mMapController = null;
    LinearLayout MapContainer;
    //finish


    //current location
    NGeoPoint currentPoint;
    private double currentLNG; //경도, X
    private double currentLAT; //위도, Y

    private EditText storename;
    private EditText storeaddress;
    private EditText storecontact;

    int displayCenterX;
    int displayCenterY;

    private Button makeReviewButton;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_in_location2);

        storename = (EditText) findViewById(R.id.storename_txt_review_regi_self);
        storeaddress = (EditText) findViewById(R.id.address_txt_review_regi_self);
        storecontact = (EditText) findViewById(R.id.contact_txt_review_regi_self);

        mMapView = new NMapView(this);

        mMapController = mMapView.getMapController();

        MapContainer = (LinearLayout) findViewById(R.id.search_in_location_map2);

        MapContainer.addView(mMapView);

        mMapView.setApiKey(API_KEY);

        mMapView.setClickable(true);

        mMapView.setBuiltInZoomControls(true, null);

        mMapView.setOnMapStateChangeListener(this);

        makeReviewButton = (Button) findViewById(R.id.search_in_location_make_review_button);

        makeReviewButton.setOnClickListener(SearchInLocationActivityOnClickListener);

    }


    ////*ok*/class SearchInLocationOnMapStateChangeListener implements NMapView.OnMapStateChangeListener{


    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {

        if (nMapError == null) {

            mMapController.setMapCenter(Location.SEOUL, 11);

        } else {
            Log.e("SearchInLocation", "SearchInMocationOnMapStateChangeListener : error = " + nMapError.toString());
        }
    }


    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

        currentLNG = nGeoPoint.getLongitude();

        currentLAT = nGeoPoint.getLatitude();


        NMapProjection nMapProjection = mMapView.getMapProjection();

        //Log.d("SIL", "중심좌표의 위도 경도 : " + currentLNG + " ; " + currentLAT);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        displayCenterX = displayMetrics.widthPixels / 2;

        displayCenterY = displayMetrics.heightPixels / 2;


        currentPoint = nMapProjection.fromPixels(displayCenterX, displayCenterY);

        currentLNG = currentPoint.getLongitude();

        currentLAT = currentPoint.getLatitude();

        
    }


    public void onMapCenterChangeFine(NMapView nMapView) {


    }


    public void onZoomLevelChange(NMapView nMapView, int i) {

    }


    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }
    ///}


    //////버튼 리스너
    Button.OnClickListener SearchInLocationActivityOnClickListener = new View.OnClickListener() {

        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.search_in_location_make_review_button:
                    Intent intent = new Intent(SearchInLocationActivity.this, review_register.class);
                    intent.putExtra("store_name", storename.getText().toString());
                    intent.putExtra("store_address", storeaddress.getText().toString());
                    intent.putExtra("store_contact", storecontact.getText().toString());
                    intent.putExtra("store_x", currentLNG);
                    intent.putExtra("store_y", currentLAT);
                    startActivity(intent);
            }
        }
    };

}




