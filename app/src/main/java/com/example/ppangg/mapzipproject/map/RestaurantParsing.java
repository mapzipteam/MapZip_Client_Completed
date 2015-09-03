package com.example.ppangg.mapzipproject.map;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.PhantomReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class RestaurantParsing {


    private RestaurantResult restaurants;
    private String response;
    private Context context;


    private boolean tagId_TITLE = false;
    private boolean tagId_LINK = false;
    private boolean tagId_CATEGORY = false;
    private boolean tagId_DESCRIPTION = false;
    private boolean tagId_TELEPHONE = false;
    private boolean tagId_ADRESS = false;
    private boolean tagId_ROADADRESS = false;
    private boolean tagId_KATECX = false;
    private boolean tagId_KATECY = false;


    private int katecX;//경도, X, longtitude
    private int katecY;//위도, Y, latitude

    private Restaurant restaurant = new Restaurant();



    public RestaurantParsing(RestaurantResult restaurants, String response, Context context) {

        this.restaurants = restaurants;
        this.response = response;
        this.context = context;
    }


    public boolean startParsing() {

        try {

            final XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();

            final XmlPullParser parser = parserCreator.newPullParser();

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

                        if (tag.equals("title")) {
                            tagId_TITLE = true;
                        } else if (tag.equals("link")) {
                            tagId_LINK = true;
                        } else if (tag.equals("category")) {
                            tagId_CATEGORY = true;
                        } else if (tag.equals("description")) {
                            tagId_DESCRIPTION = true;
                        } else if (tag.equals("telephone")) {
                            tagId_TELEPHONE = true;
                        } else if (tag.equals("address")) {
                            tagId_ADRESS = true;
                        } else if (tag.equals("roadAddress")) {
                            tagId_ROADADRESS = true;
                        } else if (tag.equals("mapx")) {
                            tagId_KATECX = true;
                        } else if (tag.equals("mapy")) {
                            tagId_KATECY = true;
                        }
                        break;

                    case XmlPullParser.END_TAG:

                        setTagIdFalse();
                        break;

                    case XmlPullParser.TEXT:

                        if (tagId_TITLE == true) {

                            restaurant.setTitle(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "0~>: " + parser.getText().trim());

                        } else if (tagId_LINK == true) {

                            restaurant.setLink(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "1: " + parser.getText().trim());

                        } else if (tagId_CATEGORY == true) {

                            restaurant.setCategory(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "2: " + parser.getText().trim());

                        } else if (tagId_DESCRIPTION == true) {

                            restaurant.setDescription(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "3: " + parser.getText().trim());

                        } else if (tagId_TELEPHONE == true) {

                            restaurant.setTelephone(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "4: " + parser.getText().trim());

                        } else if (tagId_ADRESS == true) {

                            restaurant.setAdress(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "5: " + parser.getText().trim());

                        } else if (tagId_ROADADRESS == true) {

                            restaurant.setRoadadress(parser.getText().trim().replaceAll("<b>", "").replaceAll("</b>", ""));
                            setTagIdFalse();
                            //Log.d("volley", "6: " + parser.getText().trim());

                        } else if (tagId_KATECX == true) {

                            katecX = Integer.parseInt(parser.getText().trim().replaceAll("<b>","").replaceAll("</b>", ""));

                            restaurant.setKatecX(katecX);
                            setTagIdFalse();
                            //Log.d("volley", "7: " + parser.getText().trim());

                        } else if (tagId_KATECY == true) {

                            katecY = Integer.parseInt(parser.getText().trim().replaceAll("<b>","").replaceAll("</b>" ,""));

                            restaurant.setKatecY(katecY);
                            setTagIdFalse();
                            //Log.d("volley", "8: " + parser.getText().trim());


                            restaurant = addRestaurant(restaurant);

                        }


                        setTagIdFalse();
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return true;
    }

    public void setTagIdFalse() {

        tagId_TITLE = false;
        tagId_LINK = false;
        tagId_CATEGORY = false;
        tagId_DESCRIPTION = false;
        tagId_TELEPHONE = false;
        tagId_ADRESS = false;
        tagId_ROADADRESS = false;
        tagId_KATECX = false;
        tagId_KATECY = false;
    }

    // restaurants.add(restaurant);
    // restaurant = new Restaurant(); 이 두 부분을 메소드화 시킨 메소드
    public Restaurant addRestaurant(Restaurant restaurant) {

        restaurants.add(restaurant);
        return new Restaurant();
    }

    public RestaurantResult getRestaurants() {
        return restaurants;
    }

}

