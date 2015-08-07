package com.example.ppangg.mapzipproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class home_Fragment extends Fragment {
    private TextView topstate ;
    private UserData user;

	public home_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View v = inflater.inflate(R.layout.homelayout, container, false);

        topstate = (TextView)v.findViewById(R.id.topstate);
        topstate.setText(user.getUserName());
        topstate.append("(");
        topstate.append(user.getUserID());
        topstate.append(")");

        //스피너
        Spinner spinner = (Spinner)v.findViewById(R.id.spinner);
        //어댑터 생성
        //이 예제 같은 경우 string,xml에 리스트를 추가해 놓고 그 리스트를 불러온다.
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_number, android.R.layout.simple_spinner_item);
        //스피너와 어댑터 연결
        spinner.setAdapter(adapter);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
}
