package com.mapzip.ppang.mapzipproject.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;

import java.io.IOException;

/**
 * Created by myZZUNG on 2016. 1. 24..
 */
public class RegistrationIntentService extends IntentService{

    private static final String TAG = "RegistrationIntentService";

    private SharedPreferences pref;

    private UserData userdata = UserData.getInstance();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService() {
        super(TAG);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {
        // GCM Instance ID의 토큰을 가져오는 작업이 시작되면 LocalBoardcast로 GENERATING 액션을 알려 ProgressBar가 동작하도록 한다.

        pref = getSharedPreferences(SystemMain.SHARED_PREFERENCE_GCMFILE, MODE_PRIVATE);

        int old_version = pref.getInt("version_code",0);
        int current_version = getAppVersion(getApplicationContext());

        String token = null;

        if(old_version == current_version){
            // 이럴경우 굳이 gcmkey를 요청할 필요가 없다.

            token = pref.getString("GCM_token",null);
            Intent registrationNoChange = new Intent(QuickstartPreferences.REGISTRATION_NOCHANGE);
            registrationNoChange.putExtra("token", token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationNoChange);

            userdata.setGcm_token(token);
            return;
        }
        else{
            // 버전이 달라졋을 경우 gcmkey를 새로 요청해야한다.
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(new Intent(QuickstartPreferences.REGISTRATION_GENERATING));
            // GCM을 위한 Instance ID를 가져온다.
            InstanceID instanceID = InstanceID.getInstance(this);

            try {
                synchronized (TAG) {
                    // GCM 앱을 등록하고 획득한 설정파일인 google-services.json을 기반으로 SenderID를 자동으로 가져온다.
                    String default_senderId = getString(R.string.gcm_defaultSenderId);
                    // GCM 기본 scope는 "GCM"이다.
                    String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                    // Instance ID에 해당하는 토큰을 생성하여 가져온다.
                    token = instanceID.getToken(default_senderId, scope, null);

                    Log.i(TAG, "GCM Registration Token: " + token);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // GCM Instance ID에 해당하는 토큰을 획득하면 LocalBoardcast에 COMPLETE 액션을 알린다.
            // 이때 토큰을 함께 넘겨주어서 UI에 토큰 정보를 활용할 수 있도록 했다.
            Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

            // 새로운 gcmkey를 받았으니, version_code 를 갈아 낀다
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("version_code", current_version);
            editor.putString("GCM_token", token);
            editor.commit();

            userdata.setGcm_token(token); // gcm-key 를 userdata에 세팅
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Destroy this service");
    }

    /**
     * 프로젝트의 버전코드를 얻어와서 기존 것과 다르면 gcmkey를 재요청한다.
     * @param context
     * @return
     */
    private int getAppVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
