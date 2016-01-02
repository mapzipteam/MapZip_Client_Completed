package com.mapzip.ppang.mapzipproject.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;

/**
 * Created by ppangg on 2015-12-29.
 */
public class suggestActivity extends Activity {

    // userData
    public UserData user;

    // toast
    private View layout_toast;
    private TextView text_toast;

    // suggest catecory
    private Spinner sugspinner;
    private ArrayList<String> sugsppinerList;
    private ArrayAdapter sugadapter;

    // suggest contents
    private EditText contents;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        // userData
        user = UserData.getInstance();

        // toast
        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        // action bar
        ActionBar actionBar =getActionBar();
        actionBar.setTitle("    개발자에게 건의하기");
        actionBar.setDisplayShowHomeEnabled(false);

        // spinner
        sugspinner = (Spinner) findViewById(R.id.spinner_suggest);
        sugadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_suggest));
        sugadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sugspinner.setAdapter(sugadapter);

        // contents
        contents = (EditText) findViewById(R.id.editeval_suggest);
    }

    // cancel Btn
    public void cancelOnclick_suggest(View v) {
        this.finish();
    }

    // ok Btn
    public void saveOnclick_suggest(View v) {

        if(contents.getText().toString().trim().length() == 0){
            // toast
            text_toast.setText("내용을 입력해주세요.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

        if(sugspinner.getSelectedItemPosition() == 0){
            // toast
            text_toast.setText("카테고리를 선택해주세요.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(!(mobile.isConnected() || wifi.isConnected()))
        {
            // toast
            text_toast.setText("인터넷 연결이 필요합니다.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", user.getUserID());
            obj.put("username", user.getUserName());
            obj.put("category",sugspinner.getSelectedItem());
            obj.put("contents",contents.getText().toString());
            Log.v("제이손 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_SUGGEST_URL,
                obj,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        queue.add(myReq);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("제이손", response.toString());

                try {
                    if (response.get("state").toString().equals("1001")) {
                        // toast
                        text_toast.setText(user.getUserName() + "님의 소중한 의견 감사합니다.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();

                        finish();
                    }
                }catch (JSONException e){
                    Log.e("제이손","에러");
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

                    Log.e("suggestActivity", error.getMessage());
                } catch (NullPointerException ex) {
                    Log.e("suggestActivity", "nullpointexception");
                }
            }
        };
    }
}
