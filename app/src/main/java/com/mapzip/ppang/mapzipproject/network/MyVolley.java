package com.mapzip.ppang.mapzipproject.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by ljs93kr on 2015-07-29.
 * Google Volley lib 을 이용하여 network 통신
 */
public class MyVolley {
    private static MyVolley one;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private final LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(20);

    private MyVolley(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue,new ImageLoader.ImageCache(){

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });

    }

    public static MyVolley getInstance(Context context) {
        if (one == null) {
            one = new MyVolley(context);
        }
        return one;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void removeCache(String url){
        Log.v("마이볼리 캐쉬 사이즈", String .valueOf(cache.size()));
        Log.v("마이볼리 캐쉬", String .valueOf(cache));
        Log.v("url name", url);

        if(cache.remove(url) == null){
            Log.v("url","null");
        }

        Log.v("마이볼리 캐쉬 사이즈", String .valueOf(cache.size()));
        Log.v("마이볼리 캐쉬", String.valueOf(cache));
    }

    public void clearCache() {
        Log.v("마이볼리 캐쉬 사이즈", String .valueOf(cache.size()));
        Log.v("마이볼리 캐쉬", String .valueOf(cache));
/*
        ClearCacheRequest ccr = new ClearCacheRequest(requestQueue.getCache(),
                new Runnable() {
                    @Override
                    public void run() {
                    }
                });
        ccr.setTag(this);
        Log.v("캐쉬","클리어");
        requestQueue.add(ccr);*/
        cache.evictAll();
        Log.v("캐쉬","클리어");
        Log.v("마이볼리 캐쉬 사이즈", String .valueOf(cache.size()));
        Log.v("마이볼리 캐쉬", String.valueOf(cache));
    }

}
