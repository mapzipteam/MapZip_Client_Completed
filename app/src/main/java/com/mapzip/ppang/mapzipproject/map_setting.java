package com.mapzip.ppang.mapzipproject;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ppangg on 2015-08-13.
 */
public class map_setting extends Activity {
    private UserData user;
    private EditText mapname;
    private String mapid;
    private EditText hashtag1;
    private EditText hashtag2;
    private EditText hashtag3;
    private EditText hashtag4;
    private EditText hashtag5;
    private String hashtag_send = "";
    private int mapkindnum;

    // toast
    private View layout_toast;
    private TextView text_toast;

    private Button saveBtn;
    private Button cancelBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapsetting);
        user = UserData.getInstance();

        ActionBar actionBar =getActionBar();
        actionBar.setTitle("    지도 설정");
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        mapname = (EditText) findViewById(R.id.maptext);
        hashtag1 = (EditText) findViewById(R.id.hashtext1);
        hashtag2 = (EditText) findViewById(R.id.hashtext2);
        hashtag3 = (EditText) findViewById(R.id.hashtext3);
        hashtag4 = (EditText) findViewById(R.id.hashtext4);
        hashtag5 = (EditText) findViewById(R.id.hashtext5);

        mapname.setOnFocusChangeListener(ofcl);
        hashtag1.setOnFocusChangeListener(ofcl);
        hashtag2.setOnFocusChangeListener(ofcl);
        hashtag3.setOnFocusChangeListener(ofcl);
        hashtag4.setOnFocusChangeListener(ofcl);
        hashtag5.setOnFocusChangeListener(ofcl);

        mapid = getIntent().getStringExtra("mapid");

        String str_mapname = getIntent().getStringExtra("mapcurname");
        mapname.setText(str_mapname);

        String hashorgin = getIntent().getStringExtra("hashtag");
        String[] hasharr = hashorgin.split("#");

        for (int i = 1; i < hasharr.length; i++) {
            switch (i) {
                case 1:
                    hashtag1.setText(hasharr[i]);
                    break;
                case 2:
                    hashtag2.setText(hasharr[i]);
                    break;
                case 3:
                    hashtag3.setText(hasharr[i]);
                    break;
                case 4:
                    hashtag4.setText(hasharr[i]);
                    break;
                case 5:
                    hashtag5.setText(hasharr[i]);
                    break;
            }
        }

        // map name
        Spinner spinner = (Spinner) findViewById(R.id.spinner_mapsetting);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_number));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mapkindnum = Integer.parseInt(getIntent().getStringExtra("mapkindnum"));
        spinner.setSelection(mapkindnum - 1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapkindnum = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    View.OnFocusChangeListener ofcl = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus)
                v.setBackgroundResource(R.drawable.editback2);
            else
                v.setBackgroundResource(R.drawable.editback);
        }
    };

    public void saveOnclick(View v) {

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

        if (!hashtag1.getText().toString().trim().isEmpty())
            hashtag_send += "#" + hashtag1.getText().toString();

        if (!hashtag2.getText().toString().trim().isEmpty())
            hashtag_send += "#" + hashtag2.getText().toString();

        if (!hashtag3.getText().toString().trim().isEmpty())
            hashtag_send += "#" + hashtag3.getText().toString();

        if (!hashtag4.getText().toString().trim().isEmpty())
            hashtag_send += "#" + hashtag4.getText().toString();

        if (!hashtag5.getText().toString().trim().isEmpty())
            hashtag_send += "#" + hashtag5.getText().toString();

        // hash_tag <= hashtag_send
        // category <= mapkindnum
        // title <= mapname.getText().toString();
        // map_id <= mapid

        DoMapset(v);
        hashtag_send = "";
    }

    public void cancelOnclick(View v) {
        this.finish();
    }

    public void DoMapset(View v) {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("userid", user.getUserID());
            obj.put("hash_tag", hashtag_send);
            obj.put("category", mapkindnum);
            obj.put("map_id", mapid);
            obj.put("title", mapname.getText().toString());
            Log.v("mapsetting 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_MAPSETTING_URL,
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

                Log.v("mapsetting 받기", response.toString());

                try {
                    user.setMapmetaArray(response.getJSONArray("mapmeta_info"));
                } catch (JSONException ex) {

                }

                //home_Fragment hf = (home_Fragment) getFragmentManager().findFragmentByTag("home_fragment");
                //hf.refresh();

                user.setMapmetaNum(1);

                text_toast.setText("저장되었습니다.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_toast);
                toast.show();

                finish();
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

                    Log.e("map_setting", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("map_setting", "nullpointexception");
                }
            }
        };
    }
}
