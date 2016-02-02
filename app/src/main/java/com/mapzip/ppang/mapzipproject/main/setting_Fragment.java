package com.mapzip.ppang.mapzipproject.main;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.activity.suggestActivity;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ppangg on 2015-12-29.
 */
public class setting_Fragment extends Fragment {

    private View v;

    // user data
    private UserData user;

    // toast
    private View layout_toast;
    private TextView text_toast;

    // notice
    private SharedPreferences pref;
    private String noticeString = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle("     설정");
        user = UserData.getInstance();
        pref = getActivity().getSharedPreferences(SystemMain.SHARED_PREFERENCE_AUTOFILE, getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_setting, container, false);

        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        ViewGroup layout = (ViewGroup) v.findViewById(R.id.mailLayout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), suggestActivity.class);
                startActivity(intent);
            }
        });

        ViewGroup layout2 = (ViewGroup) v.findViewById(R.id.leaveLayout);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setMessage("탈퇴시 그 동안 작성한 지도와 리뷰정보가 모두 소멸됩니다.\n정말 탈퇴하시겠습니까?\n").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES' target_id,
                                RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("target_id", user.getUserID());
                                    Log.v("userdat_del 보내기", obj.toString());
                                } catch (JSONException e) {
                                    Log.v("제이손", "에러");
                                }

                                JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                                        SystemMain.SERVER_DELETEUSER_URL,
                                        obj,
                                        createMyReqSuccessListener(),
                                        createMyReqErrorListener()) {
                                };
                                queue.add(myReq);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        ViewGroup layout3 = (ViewGroup) v.findViewById(R.id.fetchLayout);
        layout3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getNotice();
            }
        });

        return v;
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("userdata_del 받기", response.toString());

                try {
                    if (Integer.parseInt(response.getString("state")) == SystemMain.LEAVE_ALL_SUCCESS) {

                        // toast
                        text_toast.setText("정상적으로 탈퇴가 완료되었습니다.");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        // toast
                        text_toast.setText("회원탈퇴에 실패하였습니다.");
                        Toast toast = new Toast(getActivity());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_toast);
                        toast.show();
                    }

                }catch (JSONException ex){
                    Log.v("제이손","에러");
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

                    Log.e("user_del", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("user_del", "nullpointexception");
                }
            }
        };
    }
    // notice get method
    private void getNotice() {
        RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("state", 1);
            Log.v("mainActivity 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_NOTICE_URL,
                obj,
                createNoticeReqSuccessListener(),
                createNoticeReqErrorListener()) {
        };
        queue.add(myReq);
    }

    private Response.Listener<JSONObject> createNoticeReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("mainActivity 받기", response.toString());

                try{

                        noticeString = "버전: " + response.get("version").toString() + "\n\n";

                        noticeString += response.get("contents").toString()+"\n\n";
                        noticeString += "@이 창은 공지사항탭에서 다시 확인할 수 있습니다.";

                        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                        ab.setTitle("새로운 MapZip의 패치소식 ^0^/");
                        ab.setMessage(noticeString);
                        ab.setPositiveButton("확인", null);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("notice_version", response.get("version").toString());
                        editor.commit();

                        ab.show();

                }catch (JSONException e){
                    Log.v("제이손", "에러");
                }
            }
        };
    }

    private Response.ErrorListener createNoticeReqErrorListener() {
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

                    Log.e("mainActivity", error.getMessage());
                }catch (NullPointerException ex){
                    // toast
                    Log.e("mainActivity", "nullpointexception");
                }
            }
        };
    }
}
