package com.foodexpress.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.util.Log;

public class NetworkEngine {

	HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    public HttpResponse postRequset(String url,String keyParam,JSONArray jsonArray){
        String message=null;
        try{
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            Map<String, String> kvPairs = new HashMap<String, String>();
            kvPairs.put(keyParam, jsonArray.toString());
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(kvPairs.size());
            String k, v;
            Iterator<String> itKeys = kvPairs.keySet().iterator();
            while (itKeys.hasNext()) {
                k = itKeys.next();
                v = kvPairs.get(k);
                nameValuePairs.add(new BasicNameValuePair(k, v));
            }

            Log.v("nameValuePairs", "" + nameValuePairs);

            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
            httpPost.setEntity(urlEncodedFormEntity);
             httpResponse = httpClient.execute(httpPost);
           
             }
        catch (Exception e){
        }
		return httpResponse;
    }
}