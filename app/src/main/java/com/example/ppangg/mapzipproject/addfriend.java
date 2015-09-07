package com.example.ppangg.mapzipproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ppangg on 2015-08-30.
 */
public class addfriend extends Activity {

    private TextView searchText;
    private Button searchBtn;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        searchText = (TextView) findViewById(R.id.searchText_addfriend);
        searchBtn = (Button) findViewById(R.id.searchBtn_addfriend);
    }
}
