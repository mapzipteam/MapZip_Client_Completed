package com.example.ppangg.mapzipproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ppangg on 2015-07-31.
 */
public class loginFragment extends Fragment {


    private TextView state;
    private Resources res;
    private EditText inputID;
    private EditText inputPW;

    private int mPageNumber;

    private Button LoginBtn;

    // toast
    private View layout_toast;
    private TextView text_toast;

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
        res = getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

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
        RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

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
                        UserData user = UserData.getInstance();
                        user.LoginOK();
                        user.inputID(inputID.getText().toString());
                        user.inputName(response.get("username").toString());

                        Log.v("테스트 아이디", user.getUserID());
                        Log.v("테스트 로그인", String.valueOf(user.getLoginPermission()));
                        Log.v("테스트 이름", user.getUserName());

                        user.setMapmetaArray(response.getJSONArray("mapmeta_info"));

                        JSONObject jar = response.getJSONObject("gu_enroll_num");
                        Log.v("구넘버", String.valueOf(jar));

                        if(!jar.getString("state").equals("0")) {
                            Log.v("구넘버","진입");

                            int mapcount = response.getJSONArray("mapmeta_info").length();

                            for(int mapnum = 1; mapnum<=mapcount; mapnum++) {
                                JSONObject tmp = jar.getJSONObject(String.valueOf(mapnum));

                                int gunumber = 1;
                                int reviewnum = 0;
                                for (gunumber = 1; gunumber <= 25; gunumber++) {
                                    if (tmp.has(String.valueOf(gunumber))) {
                                        reviewnum = tmp.getInt(String.valueOf(gunumber));
                                        Log.v("구넘버o", tmp.get(String.valueOf(gunumber)).toString());
                                        //배열에 추가
                                    } else {
                                        reviewnum = 0;
                                        Log.v("구넘버x", String.valueOf(gunumber));
                                        //배열에 0 추가
                                    }
                                    user.setReviewCount(mapnum, gunumber, reviewnum);
                                }

                                user.setMapImage(mapnum, res);
                            }
                        }

                        Intent intent = new Intent(getActivity(), slidingTap.class);
                        startActivity(intent);
                        getActivity().finish();

                        // toast
                        text_toast.setText("환영합니다!");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout_toast);
                        toast.show();
                    } else {
                        // toast
                        text_toast.setText("존재하지 않는 계정정보입니다.");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
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
                try {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("loginFragment", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("loginFragment", "nullpointexception");
                }
            }
        };
    }
}