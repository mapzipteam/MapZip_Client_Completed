package com.example.ppangg.mapzipproject.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ppangg.mapzipproject.R;
import com.example.ppangg.mapzipproject.review_register;

public class review_Fragment extends Fragment {

	public review_Fragment(){}
	


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        final Button review_regi = (Button) v.findViewById(R.id.registerBtn_review);
        review_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), review_register.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    //

}


