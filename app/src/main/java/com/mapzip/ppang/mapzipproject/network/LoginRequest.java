package com.mapzip.ppang.mapzipproject.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.mapzip.ppang.mapzipproject.model.SystemMain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljs93kr on 2015-07-29.
 */
public class LoginRequest extends Request<String> {

    private Map<String,String> mParams;

    public LoginRequest(String userid, String userpw,Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, SystemMain.SERVER_JOIN_URL,errorListener);

        mParams = new HashMap<String,String>();
        mParams.put("userid",userid);
        mParams.put("userpw",userpw);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(String response) {

    }
}
