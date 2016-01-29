package com.mapzip.ppang.mapzipproject.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by ppangg on 2015-07-31.
 */
public class joinFragment extends Fragment {

    private EditText inputID;
    private EditText inputName;
    private EditText inputPW;
    private EditText inputPW2;

    private int mPageNumber;

    private Button JoinBtn;

    // head
    private ImageView joinhead;
    private ImageView joinhead2;
    private ImageView joinhead3;
    private ImageView joinhead4;

    // toast
    private View layout_toast;
    private TextView text_toast;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.join_layout, container, false);

        inputID = (EditText) rootView.findViewById(R.id.InputID);
        inputName = (EditText) rootView.findViewById(R.id.InputName);
        inputPW = (EditText) rootView.findViewById(R.id.InputPW);
        inputPW2 = (EditText) rootView.findViewById(R.id.InputPW2);
        JoinBtn = (Button) rootView.findViewById(R.id.btnJoin);
        joinhead = (ImageView) rootView.findViewById(R.id.joinhead);
        joinhead2 = (ImageView) rootView.findViewById(R.id.joinhead2);
        joinhead3 = (ImageView) rootView.findViewById(R.id.joinhead3);
        joinhead4 = (ImageView) rootView.findViewById(R.id.joinhead4);

        inputID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    joinhead.setBackgroundResource(R.drawable.joinedithead);
                else
                    joinhead.setBackgroundResource(R.color.transparent);
            }
        });

        inputName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    joinhead2.setBackgroundResource(R.drawable.joinedithead);
                else
                    joinhead2.setBackgroundResource(R.color.transparent);
            }
        });

        inputPW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    joinhead3.setBackgroundResource(R.drawable.joinedithead);
                else
                    joinhead3.setBackgroundResource(R.color.transparent);
            }
        });

        inputPW2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    joinhead4.setBackgroundResource(R.drawable.joinedithead);
                else
                    joinhead4.setBackgroundResource(R.color.transparent);
            }
        });

        JoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputID.getWindowToken(), 0);
                InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(inputPW.getWindowToken(), 0);
                InputMethodManager imm3 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(inputName.getWindowToken(), 0);
                DoJoin(v);
            }
        });

        return rootView;
    }

    public void DoJoin(View v) {
        RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();
        String url = SystemMain.SERVER_JOIN_URL;
        final String userid = inputID.getText().toString();
        final String userpw = inputPW.getText().toString();
        final String username = inputName.getText().toString();

        if( userid.length() > 5 ){
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");//영문, 숫자, 한글만 허용
            if(!ps.matcher(userid).matches()){
                // toast
                text_toast.setText("아이디는 영문과 숫자의 조합으로 생성해주세요.");
                Toast toast = new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_toast);
                toast.show();

                return;
            }
        }

        if(userpw.length() < 8){
            // toast
            text_toast.setText("ID는 5자, Password는 8자이상 입니다.");
            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

        if(userpw.equals(inputPW2.getText().toString()) == false){
            // toast
            text_toast.setText("비밀번호확인을 틀리셨습니다.");
            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

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
                        text_toast.setText("회원가입에 성공하였습니다.");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();

                        //state.setText("회원가입에 성공하였습니다.");
                        Log.v("회원가입", "성공");
                    } else {
                        // toast
                        text_toast.setText("이미 존재하는 계정정보입니다.");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
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
                try {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("searchmap", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("searchmap", "nullpointexception");
                }
            }
        };
    }
}