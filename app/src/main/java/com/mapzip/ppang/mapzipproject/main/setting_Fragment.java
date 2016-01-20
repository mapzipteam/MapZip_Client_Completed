package com.mapzip.ppang.mapzipproject.main;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.activity.suggestActivity;

/**
 * Created by ppangg on 2015-12-29.
 */
public class setting_Fragment extends Fragment {

    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle("     설정");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_setting, container, false);

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
                                // 'YES'
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

        return v;
    }
}
