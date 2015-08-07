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
import com.android.volley.toolbox.StringRequest;
import com.example.ppangg.mapzipproject.network.MyVolley;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ppangg on 2015-07-31.
 */
public class loginFragment extends Fragment {


    private TextView state ;

    private EditText inputID;
    private EditText inputPW;

    private int mPageNumber;
    private Context cont;

    private Button LoginBtn;


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

        cont=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_layout, container, false);

        state = (TextView)rootView.findViewById(R.id.TextState);
        inputID = (EditText)rootView.findViewById(R.id.InputID);
        inputPW = (EditText)rootView.findViewById(R.id.InputPW);
        LoginBtn = (Button)rootView.findViewById(R.id.btnLogin);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DoLogin(v);
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

    public void DoLogin(View v) {
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

    private Response.Listener<String> NetSuccessListener(){
        return new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.v("내용", response);
                Log.v("길이", String.valueOf(response.length()));

                if(response.charAt(3) == '1'){
                    Log.v("로그인", "성공");

                    //Toast.makeText(cont, "로그인 성공!", Toast.LENGTH_LONG).show();

                    UserData user = UserData.getInstance();
                    user.LoginOK();
                    user.inputID(inputID.getText().toString());
                    user.inputName(response.substring(4));

                    Log.v("테스트 아이디", user.getUserID());
                    Log.v("테스트 로그인",String.valueOf(user.getLoginPermission()));
                    Log.v("테스트 이름",user.getUserName());

              //      Intent intent = new Intent(cont,Tapactivity.class);
                    Intent i = new Intent(cont,slidingTap.class);
                    startActivity(i);

                    getActivity().finish();
                }
                else {
                    state.setText("존재하지 않는 계정정보입니다.");
                    Log.v("로그인", "실패");
                }
            }
        };
    }
    private Response.ErrorListener NetErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("로그인",error.getMessage());
                state.setText("인터넷 연결이 필요합니다.");
            }
        };
    }
}