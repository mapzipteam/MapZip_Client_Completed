package com.mapzip.ppang.mapzipproject.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by ppangg on 2015-08-30.
 */
public class addfriend extends Activity {
    // toast
    private View layout_toast;
    private TextView text_toast;

    private EditText searchText;
    private Button searchBtn;

    private TextView friendinfo;
    private Button friendadd;

    private UserData user;

    private String friendID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_addfriend);
        user = UserData.getInstance();

        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);
        searchBtn = (Button) findViewById(R.id.searchBtn_addfriend);
        searchText = (EditText) findViewById(R.id.searchText_addfriend);
        friendinfo = (TextView) findViewById(R.id.addfriendText);
        friendadd = (Button) findViewById(R.id.addfriendBtn);

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                addFriend_search(v);

            }
        });
        friendadd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                user.setfriendlock(false);
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(friendinfo.getWindowToken(), 0);
                addFriend_enroll(v);


            }
        });
    }


    public void addFriend_search(View v) {
        if(user.getUserID().equals(searchText.getText().toString())){
            // toast
            text_toast.setText("자기자신은 검색할 수 없습니다.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        } else if(searchText.getText().toString().trim().isEmpty()){
            // toast
            text_toast.setText("검색어를 입력해주세요.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("user_id", user.getUserID());
            obj.put("friend_id",searchText.getText().toString());

            Log.v("addfriend_search 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_ADDFRIENDSEARCH_URL,
                obj,
                createMyReqSuccessListener_search(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    public void addFriend_enroll(View v) {
        RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("user_id", user.getUserID());
            obj.put("friend_id",friendID);
            obj.put("user_name",user.getUserName());

            Log.v("addfriend_enroll 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_ADDFRIENDENROLL_URL,
                obj,
                createMyReqSuccessListener_enroll(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener_search() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("addfriend_search", response.toString());
                friendID = searchText.getText().toString();

                try {

                    friendinfo.setVisibility(View.GONE);
                    friendadd.setVisibility(View.GONE);

                    friendinfo.setText(response.getString("friend_name") + " (" + friendID + ")\n리뷰수: " + response.get("total_review").toString());
                    if(response.getInt("is_friend")==1){
                        friendadd.setBackgroundResource(R.drawable.friend_add2);
                        friendadd.setEnabled(false);
                    }else if(response.getString("total_review").equals("null")){
                        // toast
                        text_toast.setText("존재하지 않는 사용자입니다.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();

                        return;
                    }
                    else {
                        friendadd.setBackgroundResource(R.drawable.friend_add);
                        friendadd.setEnabled(true);
                    }
                    friendinfo.setVisibility(View.VISIBLE);
                    friendadd.setVisibility(View.VISIBLE);

                }catch (JSONException ex){
                    Log.e("제이손","에러");
                }

            }
        };
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener_enroll() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("addfriend_enroll", response.toString());

                friendadd.setVisibility(View.INVISIBLE);
                friendadd.setBackgroundResource(R.drawable.friend_add2);
                friendadd.setEnabled(false);
                friendadd.setVisibility(View.VISIBLE);

                // toast
                text_toast.setText(friendID+"님을 맵갈피에 추가하였습니다.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_toast);
                toast.show();

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

                    Log.e("addfriend", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("addfriend", "nullpointexception");
                }
            }
        };
    }
}
