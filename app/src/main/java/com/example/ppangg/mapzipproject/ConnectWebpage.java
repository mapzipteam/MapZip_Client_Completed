package com.example.ppangg.mapzipproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ljs93kr on 2015-07-27.
 */
public class ConnectWebpage extends AsyncTask<String,String,String>{

    private Context currContext;

    private String params;

    public ConnectWebpage(){};
    public ConnectWebpage(Context context) {
        currContext = context;
    }
    public ConnectWebpage(Context context, String params){
        currContext = context;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            return (String)DownloadUrl((String) params[0]);
        }
        catch(IOException e){
            Log.d("CONNECTION","The msg is "+e.getMessage());
            return "download fail";
        }

    }



    private String DownloadUrl(String strUrl) throws IOException{
        InputStream inStream  = null;
        Reader reader = null;
        int len = 500;

        try{
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(params);
            writer.flush();
            writer.close();

            //conn.connect();

            int resp = conn.getResponseCode();
            Log.d("CONNECTION", "The response is "+resp);
            inStream = conn.getInputStream();
            reader = new InputStreamReader(inStream,"UTF-8");
            char[] buff = new char[len];
            reader.read(buff);
            return new String(buff);
        }
        finally{
            if(inStream!=null){
                inStream.close();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(currContext,result,Toast.LENGTH_LONG).show();


    }

}
