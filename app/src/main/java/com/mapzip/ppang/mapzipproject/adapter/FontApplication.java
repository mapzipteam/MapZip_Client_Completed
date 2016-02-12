package com.mapzip.ppang.mapzipproject.adapter;

/**
 * Created by Minjeong on 2015-08-23.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Field;

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        try{
            Fabric.with(this, new Crashlytics());
        } catch (Exception e){
            e.printStackTrace();
        }

        setDefaultFont(this, "DEFAULT", "default_font2.ttf");
        setDefaultFont(this, "SANS_SERIF", "default_font2.ttf");
        setDefaultFont(this, "SERIF", "default_font2.ttf");

    }

    public static void setDefaultFont(Context ctx,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(ctx.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field StaticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            StaticField.setAccessible(true);
            StaticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}