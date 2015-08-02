package com.example.ppangg.mapzipproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ppangg on 2015-07-31.
 */
public class joinFragment extends Fragment {

    private int mPageNumber;

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
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.join_layout,
                null);
        return v;
    }
}