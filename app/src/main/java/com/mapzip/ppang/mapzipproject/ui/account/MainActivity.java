package com.mapzip.ppang.mapzipproject.ui.account;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity {

    // toast
    private View layout_toast;
    private TextView text_toast;

    // Login fragment, Join fragment
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    // auto_login
    private SharedPreferences pref;
    private int isAuto;
    private String auto_id;
    private String auto_pw;

    // notice
    private String noticeString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toast
        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(1);

        pref = getSharedPreferences(SystemMain.SHARED_PREFERENCE_AUTOFILE, MODE_PRIVATE);
        isAuto = pref.getInt("isAuto", -1);

        UserData userData = UserData.getInstance();


        userData.setIsAuto(isAuto);


        if(isAuto == -1){
            // no auto login
            Log.d("pref","Auto Login off : "+isAuto);
        }
        else if(isAuto == 1){
            Log.d("pref","Auto Login on");
            auto_id = pref.getString("auto_id","");
            auto_pw = pref.getString("auto_pw","");
            Log.d("pref","auto_id : "+auto_id);
            Log.d("pref","auto_pw : "+auto_pw);
        }

        getNotice();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // �ش��ϴ� page�� Fragment�� �����մϴ�.
            if(position == 0)
                return JoinFragment.create(position);
            else
                return LoginFragment.create(position, isAuto, auto_id, auto_pw);

        }

        @Override
        public int getCount() {
            return 2;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // notice get method
    private void getNotice() {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("state", 1);
            Log.v("mainActivity 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_NOTICE_URL,
                obj,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("mainActivity 받기", response.toString());

                try{
                    if(response.get("version").equals(pref.getString("notice_version","0"))){

                    }else {
                        noticeString = "버전: " + response.get("version").toString() + "\n\n";

                        noticeString += response.get("contents").toString()+"\n\n";
                        noticeString += "@이 창은 공지사항탭에서 다시 확인할 수 있습니다.";

                        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                        ab.setTitle("새로운 MapZip의 패치소식 ^0^/");
                        ab.setMessage(noticeString);
                        ab.setPositiveButton("확인", null);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("notice_version", response.get("version").toString());
                        editor.commit();

                        ab.show();
                    }
                }catch (JSONException e){
                    Log.v("제이손", "에러");
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("mainActivity", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("mainActivity", "nullpointexception");
                }
            }
        };
    }
}
