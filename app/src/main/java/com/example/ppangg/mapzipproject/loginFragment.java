package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ppangg.mapzipproject.network.MyVolley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ppangg on 2015-07-31.
 */
public class loginFragment extends Fragment {


    private TextView state ;

    private EditText inputID;
    private EditText inputName;
    private EditText inputPW;

    private int mPageNumber;

    public static loginFragment create(int pageNumber) {
        loginFragment fragment = new loginFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_layout, container, false);

        state = (TextView)rootView.findViewById(R.id.TextState);
        inputID = (EditText)rootView.findViewById(R.id.InputID);
        inputName = (EditText)rootView.findViewById(R.id.InputName);
        inputPW = (EditText)rootView.findViewById(R.id.InputPW);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }


    public void DoLogin(View v) {

        Context cont;
        cont=getActivity();

        RequestQueue queue = MyVolley.getInstance(cont).getRequestQueue();

        final String userid = inputID.getText().toString();
        final String userpw = inputPW.getText().toString();
        if(userid !=null && !userid.equals("")&& userpw !=null && !userpw.equals("")){
            StringRequest myReq = new StringRequest(Request.Method.POST,
                    SystemMain.SERVER_LOGIN_URL,
                    NetSuccessListener(),
                    NetErrorListener()){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("userid", userid);
                    params.put("userpw",userpw);

                    return params;
                }
            };
            queue.add(myReq);
        }
    }
    public void DoJoin(View v) {
        RequestQueue queue = MyVolley.getInstance(this.getActivity()).getRequestQueue();
        String url = SystemMain.SERVER_JOIN_URL;
        final String userid = inputID.getText().toString();
        final String userpw = inputPW.getText().toString();
        final String username = inputName.getText().toString();
        if (userid != null && !userid.equals("") && userpw != null && !userpw.equals("") && username != null && !username.equals("")) {
            StringRequest myReq = new StringRequest(Request.Method.POST,
                    url,
                    NetSuccessListener(),
                    NetErrorListener()) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userid);
                    params.put("userpw", userpw);
                    params.put("username", username);

                    return params;
                }
            };
            queue.add(myReq);
        }


    }

    private Response.Listener<String> NetSuccessListener(){
        return new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                state.setText(response);
            }
        };
    }
    private Response.ErrorListener NetErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                state.setText(error.getMessage());
            }
        };
    }
}