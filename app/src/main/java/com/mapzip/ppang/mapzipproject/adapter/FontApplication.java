package com.mapzip.ppang.mapzipproject.adapter;

/**
 * Created by Minjeong on 2015-08-23.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;

import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Field;

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initFabric();
        AppEventsLogger.activateApp(this);

        setDefaultFont(this, "DEFAULT", "default_font2.ttf");
        setDefaultFont(this, "SANS_SERIF", "default_font2.ttf");
        setDefaultFont(this, "SERIF", "default_font2.ttf");

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        AppEventsLogger.deactivateApp(this);
    }

    /**
     * Fabric을 애플리케이션 실행시에 초기화 시키는 루틴입니다.
     * debuggable의 true 인자는 Play Store에 배포시에 false로 변경해야합니다.
     *
     */

    private void initFabric() {
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true) // Play Store 배포시에는 이것을 false로 변경하여야합니다.
                .build();
        try{
            Fabric.with(fabric);
        } catch (Exception e){
            e.printStackTrace();
        }
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