package com.mapzip.ppang.mapzipproject.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapzip.ppang.mapzipproject.FriendData;
import com.mapzip.ppang.mapzipproject.R;

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

        return v;
    }
}
