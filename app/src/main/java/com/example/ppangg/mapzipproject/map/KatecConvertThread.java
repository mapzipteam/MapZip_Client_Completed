package com.example.ppangg.mapzipproject.map;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class KatecConvertThread extends Thread {

    private RestaurantResult restaurants;
    private int restaurantNum;
    private Context context;

    private double lngX;
    private double latY;

    private String url;

    private static final String TAG = "DaumSearch";
    private static final String BASE_URL = "https://apis.daum.net/local/geo/transcoord?";
    private static final String API_KEY = "f1c9190bf38205e7915bb350c8f0f90e";
    private static final String FROM_COORD = "KTM";
    private static final String TO_COORD = "WGS84";
    private static final String OUTPUT = "xml";

    private static final class PARAM {
        private static final String API_KEY = "apikey=";
        private static final String FROM_COORD = "&fromCoord=";
        private static final String Y = "&y=";
        private static final String X = "&x=";
        private static final String TO_COORD = "&toCoord=";
        private static final String OUTPUT = "&output=";
    }

    
    public KatecConvertThread(RestaurantResult restaurants, int restaurantNum, Context context) {

        this.restaurants = restaurants;
        this.restaurantNum = restaurantNum;
        this.context = context;
    }

    public void run(){
        RequestQueue queue = MapVolley.getInstance(this.context).getRequestQueue();

        try {
            url = BASE_URL
                    + PARAM.API_KEY + API_KEY
                    + PARAM.X + restaurants.get(restaurantNum).getKatecX()
                    + PARAM.Y + restaurants.get(restaurantNum).getKatecY()
                    + PARAM.FROM_COORD + FROM_COORD
                    + PARAM.TO_COORD + TO_COORD
                    + PARAM.OUTPUT + OUTPUT;

        } catch (Exception e) {
            Log.e("volley", "KatecConvert startConvert 파트 Error!" + e.getMessage());
        }finally {

            StringRequest sr = new StringRequest(Request.Method.GET, url, successListener(), errorListener());

            queue.add(sr);
        }
    }



    private Response.Listener<String> successListener(){
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                startParsing(response);

            }
        };
    }
    private Response.ErrorListener errorListener(){
        return new Response.ErrorListener(){
            public void onErrorResponse(VolleyError e){

                if (e instanceof TimeoutError)
                {
                    Log.d("volley","error : "+"TimeOutError");
                }
                // 네트워크 연결이 모두 끊어진 경우
                else if (e instanceof NoConnectionError)
                {
                    Log.d("volley", "error : " + "NoConnectionError");
                }
                else if (e instanceof AuthFailureError)
                {
                    Log.d("volley", "error : " + "AuthFailureError");
                }
                // 서버에러, URL에 해당 자료가 없어도 이곳이 불린다.
                else if (e instanceof ServerError)
                {
                    Log.d("volley", "error : " + "ServerError");
                }
                else if (e instanceof NetworkError)
                {
                    Log.d("volley", "error : " + "NetworkError");
                }
                else if (e instanceof ParseError)
                {
                    Log.d("volley", "error : " + "ParseError");
                }
            }
        };
    }

    public void startParsing(String response) {

        try {
            final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            final XmlPullParser parser = factory.newPullParser();

            StringReader stringReader = new StringReader(response);

            parser.setInput(stringReader);

            int parserEvent = parser.getEventType();

            String tag;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {

                switch (parserEvent) {

                    case XmlPullParser.START_DOCUMENT:
                        break;


                    case XmlPullParser.END_DOCUMENT:
                        break;


                    case XmlPullParser.START_TAG:

                        tag = parser.getName();

                        if (tag.equals("result")) {

                            lngX = Double.parseDouble(parser.getAttributeValue(null, "x"));
                            restaurants.get(restaurantNum).setLngX(lngX);


                            latY = Double.parseDouble(parser.getAttributeValue(null, "y"));
                            restaurants.get(restaurantNum).setLatY(latY);

                        }
                        break;
                }

                parserEvent = parser.next();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }

    public RestaurantResult getRestaurants(){
        return restaurants;
    }
}

