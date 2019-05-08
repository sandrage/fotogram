package com.project.fotogram.communication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestWithParams extends StringRequest {
    private Map<String,String> parametersMaps;

    public RequestWithParams(int method, String url, Response.Listener<String> ifSuccess, Response.ErrorListener ifError){
        super(method, url, ifSuccess, ifError);
        this.parametersMaps=new HashMap<>();
    }

    @Override
    protected Map<String,String> getParams(){
        return parametersMaps;
    }

    public void addParam(String key, String value){
        this.parametersMaps.put(key,value);
    }

}
