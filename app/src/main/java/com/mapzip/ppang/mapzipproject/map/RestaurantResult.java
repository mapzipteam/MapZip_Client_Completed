package com.mapzip.ppang.mapzipproject.map;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Song  Ji won on 2015-08-30.
 */
public class RestaurantResult {

    private ArrayList<Restaurant> restaurants;

    public RestaurantResult() {
        restaurants = new ArrayList<Restaurant>();
    }

    public void add(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void add(int i, Restaurant restaurant) {
        restaurants.add(i, restaurant);
    }

    public int size() {
        return restaurants.size();
    }

    public Restaurant get(int i) {
        return restaurants.get(i);
    }

    public ArrayList<Restaurant> getRestaurants(){ return restaurants; }

    public void clear(){
        restaurants.clear();
    }

    public void showInfo() {
        for (int i = 0; i < restaurants.size(); i++) {
            Log.d("volley", "=====" + i + "번째 음식점=====");
            Log.d("volley", "== title : " + restaurants.get(i).getTitle());
            Log.d("volley", "== link : " + restaurants.get(i).getLink());
            Log.d("volley", "== category : " + restaurants.get(i).getCategory());
            Log.d("volley", "== description : " + restaurants.get(i).getDescription());
            Log.d("volley", "== telephone : " + restaurants.get(i).getTelephone());
            Log.d("volley", "== adress : " + restaurants.get(i).getAdress());
            Log.d("volley", "== roadadress : " + restaurants.get(i).getRoadadress());
            Log.d("volley", "== katecX : " + restaurants.get(i).getKatecX());
            Log.d("volley", "== katecY : " + restaurants.get(i).getKatecY());
            Log.d("volley", "== lngX : " + restaurants.get(i).getLngX());
            Log.d("volley", "== lngY : " + restaurants.get(i).getLatY());

        }
    }

    public void showMinimumInfo() {
        for (int i = 0; i < restaurants.size(); i++) {
            Log.d("volley", "레스트런트 사이즈 "+restaurants.size());
            Log.d("volley", "=====" + i + "번째 음식점=====");
            Log.d("volley", "== title : " + restaurants.get(i).getTitle());
            //Log.d("volley","== link : "+restaurants.get(i).getLink() );
            //Log.d("volley","== category : "+restaurants.get(i).getCategory() );
            //Log.d("volley","== description : "+restaurants.get(i).getDescription() );
            Log.d("volley", "== telephone : " + restaurants.get(i).getTelephone());
            Log.d("volley","== adress : "+restaurants.get(i).getAdress() );
            //Log.d("volley","== roadadress : "+restaurants.get(i).getRoadadress() );
            Log.d("volley", "== katecX : " + restaurants.get(i).getKatecX());
            //Log.d("volley","== katecY : "+restaurants.get(i).getKatecY() );
            Log.d("volley", "== lngX : " + restaurants.get(i).getLngX());
            //Log.d("volley","== lngY : "+restaurants.get(i).getLatY() );

        }
    }

}
