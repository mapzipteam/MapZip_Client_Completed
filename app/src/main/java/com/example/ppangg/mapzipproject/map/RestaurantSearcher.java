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
import com.example.ppangg.mapzipproject.UserData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Song  Ji won on 2015-08-30.
 */
public class RestaurantSearcher {
    private UserData user;

    private RestaurantResult restaurants;
    private String query;
    private Context context;


    private String url;

    private static final String TAG = "NaverSearch";
    private static final String BASE_URL = "http://openapi.naver.com/search?";
    private static final String API_KEY = "9d62aa9c56b4ed922537ad43a5d29004";
    private static final String TARGET = "local";
    private static final int START = 1;
    private static final int DISPLAY = 30;

    private static final class PARAM {
        private static final String API_KEY = "key=";
        private static final String QUERY = "&query=";
        private static final String TARGET = "&target=";
        private static final String START = "&start=";
        private static final String DISPLAY = "&display=";
    }

    private KatecConvertThread katecConvertThread;


    public RestaurantSearcher(RestaurantResult restaurants, String query, Context context) {

        user = UserData.getInstance();

        this.restaurants = restaurants;
        this.query = query;
        this.context = context;
    }

    public void UrlRequest() {

        try {
            url = BASE_URL +
                    PARAM.API_KEY + API_KEY +
                    PARAM.QUERY + URLEncoder.encode(query, "utf-8") +
                    PARAM.TARGET + TARGET +
                    PARAM.START + START +
                    PARAM.DISPLAY + DISPLAY;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("volley", url);
        RequestQueue queue = MapVolley.getInstance(context).getRequestQueue();

        StringRequest sr = new StringRequest(Request.Method.GET, url, successListener(), errorListener());

        queue.add(sr);

    }


    private Response.Listener<String> successListener() {
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    RestaurantParsing restaurantParsing = new RestaurantParsing(restaurants, response, context);

                    if (restaurantParsing.startParsing()) {
                        restaurants = restaurantParsing.getRestaurants();
                    }

                    for (int restaurantNum = 0; restaurantNum < restaurants.size(); restaurantNum++) {

                        katecConvertThread = new KatecConvertThread(restaurants, restaurantNum, context);
                        katecConvertThread.start();
                    }

                    restaurants = katecConvertThread.getRestaurants();

                    user.setReviewListlock(false);
                    Log.v("레스토랑 리스트뷰락", String.valueOf(user.getReviewListlock()));

                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
        };
    }


    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError e) {

                if (e instanceof TimeoutError) {
                    Log.d("volley", "error : " + "TimeOutError");
                }
                // 네트워크 연결이 모두 끊어진 경우
                else if (e instanceof NoConnectionError) {
                    Log.d("volley", "error : " + "NoConnectionError");
                } else if (e instanceof AuthFailureError) {
                    Log.d("volley", "error : " + "AuthFailureError");
                }
                // 서버에러, URL에 해당 자료가 없어도 이곳이 불린다.
                else if (e instanceof ServerError) {
                    Log.d("volley", "error : " + "ServerError");
                } else if (e instanceof NetworkError) {
                    Log.d("volley", "error : " + "NetworkError");
                } else if (e instanceof ParseError) {
                    Log.d("volley", "error : " + "ParseError");
                }
            }
        };
    }

    public RestaurantResult getRestaurants() {
        return restaurants;
    }


}
