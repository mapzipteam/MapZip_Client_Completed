package com.example.ppangg.mapzipproject.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SearchInLocationActivity extends NMapActivity implements OnMapStateChangeListener {


    //default Member variable declare
    private boolean lockbtn;
    public static final String API_KEY = MapActivity.API_KEY;
    NMapView mMapView = null;
    NMapController mMapController = null;
    LinearLayout MapContainer;
    //finish


    //current location
    NGeoPoint currentPoint;
    private double currentLNG; //경도, X
    private double currentLAT; //위도, Y

    private TextView text_toast;
    private View layout_toast;

    private EditText storename;
    private TextView storeaddress;
    private EditText storecontact;

    int displayCenterX;
    int displayCenterY;

    private Button makeReviewButton;


    private List<Address> addresses;
    private Address address;
    private Geocoder geocoder;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        lockbtn = false;
        getActionBar().hide();
        setContentView(R.layout.activity_search_in_location2);

        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);


        storename = (EditText) findViewById(R.id.storename_txt_review_regi_self);
        storeaddress = (TextView) findViewById(R.id.address_txt_review_regi_self);
        storecontact = (EditText) findViewById(R.id.contact_txt_review_regi_self);

        storename.setOnFocusChangeListener(ofcl);
        storecontact.setOnFocusChangeListener(ofcl);

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



    View.OnFocusChangeListener ofcl = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus)
                v.setBackgroundResource(R.drawable.editback2);
            else
                v.setBackgroundResource(R.drawable.editback);
        }
    };


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

        displayCenterY = displayMetrics.heightPixels * 25 / 100;


        currentPoint = nMapProjection.fromPixels(displayCenterX, displayCenterY);

        currentLNG = currentPoint.getLongitude();

        currentLAT = currentPoint.getLatitude();



        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentLAT, currentLNG, 1);
            address = addresses.get(0);

            String cuttentAddress = address.getAddressLine(0);
            storeaddress.setText(cuttentAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }

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

            if (storename.getText().toString().trim().isEmpty()) {
                text_toast.setText("가게 이름을 입력하세요.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_toast);
                toast.show();

            } else {
                switch (v.getId()) {
                    case R.id.search_in_location_make_review_button:
                        Intent intent = new Intent(SearchInLocationActivity.this, review_register.class);
                        intent.putExtra("store_name", storename.getText().toString());
                        intent.putExtra("store_address", storeaddress.getText().toString());
                        intent.putExtra("store_contact", storecontact.getText().toString());
                        intent.putExtra("store_x", currentLNG);
                        intent.putExtra("store_y", currentLAT);

                        startActivity(intent);
                        finish();
                }

            }
        }
    };

}




