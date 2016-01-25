package com.mapzip.ppang.mapzipproject.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Created by ppangg on 2015-07-31.
 */
public class loginFragment extends Fragment {

    private boolean lockBtn;
    private LoadingTask Loading;
    private TextView state;
    private Resources res;
    private EditText inputID;
    private EditText inputPW;
    private int mPageNumber;
    private Button LoginBtn;
    public UserData user;
    public int map;
    public ProgressDialog  asyncDialog;

    // toast
    private View layout_toast;
    private TextView text_toast;

    //auto_login
    private int isAuto;
    private String auto_id;
    private String auto_pw;
    private CheckBox check_auto;


    public static loginFragment create(int pageNumber,int isAuto,String auto_id, String auto_pw) {

        loginFragment fragment = new loginFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        args.putInt("isAuto",isAuto);
        args.putString("auto_id",auto_id);
        args.putString("auto_pw",auto_pw);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");
        isAuto = getArguments().getInt("isAuto");
        if(isAuto == 1){
            auto_id = getArguments().getString("auto_id","");
            auto_pw = getArguments().getString("auto_pw","");
        }
        res = getResources();
        asyncDialog = new ProgressDialog(this.getActivity());
        user = UserData.getInstance();

        Loading = new LoadingTask();
        lockBtn=false;

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

        // Auto_Login CheckBox
        check_auto = (CheckBox)rootView.findViewById(R.id.check_auto);
        check_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_auto.isChecked()) {
                    user.setIsAuto(1);
                } else {
                    user.setIsAuto(0);

                }
            }
        });

        inputID.setOnFocusChangeListener(ofcl);
        inputPW.setOnFocusChangeListener(ofcl);

        if(user.getIsAuto() == 1){
            check_auto.setChecked(true);
            inputID.setText(auto_id);
            inputPW.setText(auto_pw);
        }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(lockBtn==false) {
                 lockBtn = true;
                 DoLogin(v);
             }
            }
        });



        return rootView;
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

    @SuppressLint("LongLogTag")
    public void DoLogin(View v) {
        RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

        final String userid = inputID.getText().toString();
        final String userpw = inputPW.getText().toString();
        final String gcm_key = user.getGcm_token();


        if (userid != null && !userid.equals("") && userpw != null && !userpw.equals("")) {

            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputPW.getWindowToken(), 0);
            InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputID.getWindowToken(), 0);

            JSONObject obj = new JSONObject();
            try {
                obj.put("userid", userid);
                obj.put("userpw", userpw);
                obj.put("gcm_key", gcm_key);
                Log.v("제이손 보내기", obj.toString());
            } catch (JSONException e) {
                Log.v("제이손", "에러");
            }

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                    SystemMain.SERVER_LOGIN_URL,
                    obj,
                    createMyReqSuccessListener(),
                    createMyReqErrorListener());

            queue.add(myReq);
        }
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("제이손", response.toString());

                try {
                    if (response.get("state").toString().equals("200")) {

                        user.LoginOK();
                        user.inputID(inputID.getText().toString());
                        user.inputPW(inputPW.getText().toString());
                        user.inputName(response.get("username").toString());

                        Log.v("테스트 아이디", user.getUserID());
                        Log.v("테스트 로그인", String.valueOf(user.getLoginPermission()));
                        Log.v("테스트 이름", user.getUserName());

                        user.setMapmetaArray(response.getJSONArray("mapmeta_info"));

                        JSONObject jar = response.getJSONObject("gu_enroll_num");
                        Log.v("구넘버", String.valueOf(jar));



                        Log.v("구넘버", "진입");

                        int mapcount = response.getJSONArray("mapmeta_info").length();
                        map = mapcount;

                        for (int mapnum = 1; mapnum <= mapcount; mapnum++) {
                            if (jar.has(String.valueOf(mapnum))) {
                                JSONObject tmp = jar.getJSONObject(String.valueOf(mapnum));
                                int gunumber = 1;
                                int reviewnum = 0;
                                for (gunumber = 1; gunumber <= SystemMain.SeoulGuCount; gunumber++) {
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
                            } else {
                                for (int gunumber = 1; gunumber <= SystemMain.SeoulGuCount; gunumber++)
                                    user.setReviewCount(mapnum, gunumber, 0);
                            }
                        }

                           Loading.execute();



                        // toast
                        text_toast.setText("환영합니다!");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout_toast);
                        toast.show();
                    } else if(response.get("state").toString().equals("201")) {
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

                lockBtn =false;
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
                } catch (NullPointerException ex) {
                    // toast
                    Log.e("loginFragment", "nullpointexception");
                }

                lockBtn =false;
            }
        };
    }
    protected class LoadingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");
            asyncDialog.setCanceledOnTouchOutside(false);
            // show dialog
            asyncDialog.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            for (int mapnum = 1; mapnum <= map; mapnum++)
            user.setMapImage(mapnum, res);
         return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            if (asyncDialog != null) {
                asyncDialog.dismiss();
                Intent intent = new Intent(getActivity(), slidingTap.class);
                startActivity(intent);
                getActivity().finish();
            }

            super.onPostExecute(result);
        }



    }
}