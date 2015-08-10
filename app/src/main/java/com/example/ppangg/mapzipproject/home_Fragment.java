package com.example.ppangg.mapzipproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class home_Fragment extends Fragment {
    private TextView topstate ;
    private UserData user;
    private ArrayList<String> sppinerList;
    private View imageview;

	public home_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();

        sppinerList = new ArrayList<String>();
        sppinerList.add("첫번째");
        sppinerList.add("second");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        imageview = (View)v.findViewById(R.id.mapimage);

        topstate = (TextView)v.findViewById(R.id.topstate);
        topstate.setText(user.getUserName());
        topstate.append("(");
        topstate.append(user.getUserID());
        topstate.append(")");

        //스피너
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,sppinerList);
        Spinner spinner = (Spinner)v.findViewById(R.id.spinner);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_number, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    imageview.setBackgroundResource(R.drawable.seoul);
                }
                else
                {
                    imageview.setBackgroundResource(R.drawable.p1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
}
