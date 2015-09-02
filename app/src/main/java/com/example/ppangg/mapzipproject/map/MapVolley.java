package com.example.ppangg.mapzipproject.map;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Song  Ji won on 2015-08-23.
 */
    public class MapVolley {
        private static MapVolley one;
        private RequestQueue requestQueue;
        private ImageLoader imageLoader;

        private MapVolley(Context context) {
            requestQueue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(requestQueue,new ImageLoader.ImageCache(){
                private final LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(20);

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

        public static MapVolley getInstance(Context context) {
            if (one == null) {
                one = new MapVolley(context);
            }
            return one;
        }

        public RequestQueue getRequestQueue() {
            return requestQueue;
        }

        public ImageLoader getImageLoader() {
            return imageLoader;
        }

    }