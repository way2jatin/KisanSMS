package com.jatin.kisansms.util;

/**
 * Created by jatinjha on 10/05/17.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCall {

    private static int GET = 1;
    private static int POST = 2;

    private static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");


    public static JSONObject getJsonFromUrl(String url){
        return getJson(url,GET,null);
    }

    public static JSONObject postJsonFromUrl(String url,String jsonString){
        return getJson(url,POST,jsonString);
    }

    private static JSONObject getJson(String url,int method,String jsonStringParams){
        JSONObject json = null;
        // try parse the string to a JSON object
        try {
            String jsonString = makeServiceCall(url, method, jsonStringParams);
            if (jsonString != null) {
                json = new JSONObject(jsonString);
            }
            return json;
        } catch (Exception e) {
            Log.e("ApiCall", "Error parsing data " + e.toString());
            return json;
        }
    }

    private static String makeServiceCall(String url,int method, String jsonStringParams) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request;
        if (method == POST){
            RequestBody body = RequestBody.create(JSON, jsonStringParams);
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(url)
                    .build();
        }

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
