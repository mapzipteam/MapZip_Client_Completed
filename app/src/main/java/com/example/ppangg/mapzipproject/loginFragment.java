package com.example.ppangg.mapzipproject;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ppangg.mapzipproject.main.slidingTap;
import com.example.ppangg.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ppangg on 2015-07-31.
 */
public class loginFragment extends Fragment {


    private TextView state;

    private EditText inputID;
    private EditText inputPW;

    private int mPageNumber;
    private Context cont;

    private Button LoginBtn;

    private View layout;
    private TextView text;

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

        cont = getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text = (TextView) layout.findViewById(R.id.textToShow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_layout, container, false);

        state = (TextView) rootView.findViewById(R.id.TextState);
        inputID = (EditText) rootView.findViewById(R.id.InputID);
        inputPW = (EditText) rootView.findViewById(R.id.InputPW);
        LoginBtn = (Button) rootView.findViewById(R.id.btnLogin);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin(v);
            }
        });

        return rootView;
    }

    public void DoLogin(View v) {
        RequestQueue queue = MyVolley.getInstance(cont).getRequestQueue();

        final String userid = inputID.getText().toString();
        final String userpw = inputPW.getText().toString();
        if (userid != null && !userid.equals("") && userpw != null && !userpw.equals("")) {
            /*
            Map<String,String> params = new HashMap<String,String>();
            params.put("userid", userid);
            params.put("userpw",userpw);
            */

            JSONObject obj = new JSONObject();
            try {
                obj.put("userid", userid);
                obj.put("userpw", userpw);
                Log.v("제이손 보내기",obj.toString());
            } catch (JSONException e) {
                Log.v("제이손", "에러");
            }

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                    SystemMain.SERVER_LOGIN_URL,
                    obj,
                    createMyReqSuccessListener(),
                    createMyReqErrorListener()) {
/*
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("userid", userid);
                    params.put("userpw",userpw);

                    return params;
                }*/
/*
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String utf_String = new String(response.data,"UTF-8");
                        return Response.success(utf_String,HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return Response.error(new VolleyError());
                    }



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
                    if (response.get("login").toString().equals("1")) {
                        // toast
                        text.setText("환영합니다!");
                        Toast toast = new Toast(cont);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        UserData user = UserData.getInstance();
                        user.LoginOK();
                        user.inputID(inputID.getText().toString());
                        user.inputName(response.get("username").toString());

                        Log.v("테스트 아이디", user.getUserID());
                        Log.v("테스트 로그인", String.valueOf(user.getLoginPermission()));
                        Log.v("테스트 이름", user.getUserName());

                        user.setMapmetaArray(response.getJSONArray("mapmeta_info"));

                        Intent intent = new Intent(cont, slidingTap.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        // toast
                        text.setText("존재하지 않는 계정정보입니다.");
                        Toast toast = new Toast(cont);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                } catch (JSONException e) {
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

                Log.e("로그인", error.getMessage());
            }
        };
    }
}