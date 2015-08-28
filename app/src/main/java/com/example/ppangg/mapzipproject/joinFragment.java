package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ppangg.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ppangg on 2015-07-31.
 */
public class joinFragment extends Fragment {

    private TextView state;

    private EditText inputID;
    private EditText inputName;
    private EditText inputPW;

    private int mPageNumber;
    private Context cont;

    private Button JoinBtn;

    private View layout;
    private TextView text;

    public static joinFragment create(int pageNumber) {
        joinFragment fragment = new joinFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");

        cont=getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text = (TextView)layout.findViewById(R.id.textToShow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.join_layout, container, false);

        state = (TextView) rootView.findViewById(R.id.TextState2);
        inputID = (EditText) rootView.findViewById(R.id.InputID2);
        inputName = (EditText) rootView.findViewById(R.id.InputName2);
        inputPW = (EditText) rootView.findViewById(R.id.InputPW2);
        JoinBtn = (Button) rootView.findViewById(R.id.btnJoin);

        JoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoJoin(v);
            }
        });

        return rootView;
    }
    /*
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

        }
    */
    public void DoJoin(View v) {
        RequestQueue queue = MyVolley.getInstance(cont).getRequestQueue();
        String url = SystemMain.SERVER_JOIN_URL;
        final String userid = inputID.getText().toString();
        final String userpw = inputPW.getText().toString();
        final String username = inputName.getText().toString();
        if (userid != null && !userid.equals("") && userpw != null && !userpw.equals("") && username != null && !username.equals("")) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("userid", userid);
                obj.put("userpw", userpw);
                obj.put("username",username);
                Log.v("제이손 보내기",obj.toString());
            } catch (JSONException e) {
                Log.v("제이손", "에러");
            }

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                    SystemMain.SERVER_JOIN_URL,
                    obj,
                    createMyReqSuccessListener(),
                    createMyReqErrorListener()) {
                /*
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userid);
                    params.put("userpw", userpw);
                    params.put("username", username);
                    return params;
                }*/
            };
            queue.add(myReq);
        }
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("제이손", response.toString());

                try {
                    if (response.get("join").toString().equals("1")) {
                        // toast
                        text.setText("회원가입에 성공하였습니다.");
                        Toast toast = new Toast(cont);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        //state.setText("회원가입에 성공하였습니다.");
                        Log.v("회원가입", "성공");
                    } else {
                        // toast
                        text.setText("이미 존재하는 계정정보입니다.");
                        Toast toast = new Toast(cont);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        //state.setText("이미 존재하는 계정입니다.");
                        Log.v("회원가입", "실패");
                    }
                }catch (JSONException e){
                    Log.v("에러", "제이손");
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // toast
                text.setText("인터넷 연결이 필요합니다.");
                Toast toast = new Toast(cont);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

               // Log.e("회원가입", error.getMessage());
                //state.setText("인터넷 연결이 필요합니다.");
            }
        };
    }
}