package com.mapzip.ppang.mapzipproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ppangg on 2015-12-29.
 */
public class suggestActivity extends Activity {

    // toast
    private View layout_toast;
    private TextView text_toast;

    // suggest catecory
    private Spinner sugspinner;
    private ArrayList<String> sugsppinerList;
    private ArrayAdapter sugadapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        // toast
        LayoutInflater inflater = this.getLayoutInflater();
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        // action bar
        ActionBar actionBar =getActionBar();
        actionBar.setTitle("    개발자에게 건의하기");
        actionBar.setDisplayShowHomeEnabled(false);

        // spinner
        sugspinner = (Spinner) findViewById(R.id.spinner_suggest);
        sugadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_suggest));
        sugadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sugspinner.setAdapter(sugadapter);
    }

    // cancel Btn
    public void cancelOnclick_suggest(View v) {
        this.finish();
    }

    // ok Btn
    public void saveOnclick(View v) {

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(!(mobile.isConnected() || wifi.isConnected()))
        {
            // toast
            text_toast.setText("인터넷 연결이 필요합니다.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_toast);
            toast.show();

            return;
        }

    }
}
