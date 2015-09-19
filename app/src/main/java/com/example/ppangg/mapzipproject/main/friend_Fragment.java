package com.example.ppangg.mapzipproject.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ppangg.mapzipproject.R;

import com.example.ppangg.mapzipproject.SystemMain;
import com.example.ppangg.mapzipproject.UserData;
import com.example.ppangg.mapzipproject.addfriend;
import com.example.ppangg.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

public class friend_Fragment extends Fragment {

    private UserData user;

    // toast
    private View layout_toast;
    private TextView text_toast;

    private Button ex;
	
	public friend_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();
        getActivity().getActionBar().setTitle("친구목록");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);
 
        View v = inflater.inflate(R.layout.fragment_friend, container, false);
        ex = (Button) v.findViewById(R.id.button_sample);
        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFriendlist(v);
            }
        });

        final Button addfriend = (Button) v.findViewById(R.id.addBtn_friend);
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addfriend.class);
                startActivity(intent);
            }
        });
         
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    //

    public void GetFriendlist(View v) {
            RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

            JSONObject obj = new JSONObject();
            try {
                obj.put("userid", user.getUserID());

                Log.v("friendfragment 보내기", obj.toString());
            } catch (JSONException e) {
                Log.v("제이손", "에러");
            }

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                    SystemMain.SERVER_FRIENDLIST_URL,
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

                Log.v("friendfragment 받기", response.toString());
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
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("friendfragment", error.getMessage());
                } catch (NullPointerException ex) {
                    // toast
                    Log.e("friendfragment", "nullpointexception");
                }
            }
        };
    }


}
