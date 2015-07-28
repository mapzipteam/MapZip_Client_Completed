package com.example.ppangg.mapzipproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    ConnectWebpage testcw = null;

    TextView state ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // new line

        state = (TextView)findViewById(R.id.TextState);
        testcw = new ConnectWebpage(this);

    }

    public void DoLogin(View v) {
        Toast.makeText(getApplicationContext(),"Btn Clicked!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(),Tabactivity.class);
        startActivity(intent);

    }
    public void DoJoin(View v){
        String strUrl = "http://ljs93kr.cafe24.com/mapzip/login/joincheck.php";
        try{
            if(strUrl!=null && strUrl.length()>0){
                ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netState = conMgr.getActiveNetworkInfo();
                if(netState!=null && netState.isConnected()){
                    testcw.execute(strUrl);
                }
                else{
                    throw new Exception("NETWORK ERROR");
                }
            }
            else{
                throw new Exception("BAD URL");
            }
        }
        catch(Exception e){
            state.setText(e.getMessage());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
