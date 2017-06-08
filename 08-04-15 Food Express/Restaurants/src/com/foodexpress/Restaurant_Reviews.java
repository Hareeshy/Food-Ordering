package com.foodexpress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.foodexpress.adapters.ReviewAdapter;
import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.NetworkConstants;

public class Restaurant_Reviews extends ActionBarActivity{
	ListView lv;
	ArrayList<String> review;
	ArrayList<Integer> rating;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.res_reviews);
		lv = (ListView)findViewById(R.id.listView1);
		rating = new ArrayList<Integer>();
		review = new ArrayList<String>();

		

		new getReviews().execute();
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				
				 new AlertDialog.Builder(Restaurant_Reviews.this)
		          .setTitle("Review ("+rating.get(pos)+")")
		          .setMessage(review.get(pos))
		          .setCancelable(false)
		          .setPositiveButton("Ok", new OnClickListener() {
		              @Override
		              public void onClick(DialogInterface dialog, int which) {
		              dialog.dismiss();                        
		              }
		          }).create().show(); 
				
			}
		});
	}
	private class getReviews extends AsyncTask<JSONArray, String, String>{
		MyDialogs dialogs = new MyDialogs();
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialogs.showProgressDialog(Restaurant_Reviews.this);
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(JSONArray... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(NetworkConstants.viewReview);

			JSONArray jsonArray = new JSONArray();
			JSONObject jobject = new JSONObject();
			try {
				jobject.put("rest_id", Restaurant_Home.restaurent_id);
				jsonArray.put(jobject);
				Map<String, String> kvPairs = new HashMap<String, String>();
			    kvPairs.put("ViewReview", jsonArray.toString());
			    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(kvPairs.size());
			    String k, v;
			    Iterator<String> itKeys = kvPairs.keySet().iterator();
			    while (itKeys.hasNext()) {
			        k = itKeys.next();
			        v = kvPairs.get(k);
			        nameValuePairs.add(new BasicNameValuePair(k, v));
			    }
			    
			    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
			    httpPost.setEntity(urlEncodedFormEntity);

			    HttpResponse httpResponse = httpClient.execute(httpPost);
			    InputStream inputStream = httpResponse.getEntity().getContent();
			    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			    StringBuilder stringBuilder = new StringBuilder();
			    String bufferedStrChunk;
			    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
			        stringBuilder.append(bufferedStrChunk);
			    }
			    Log.v("response", stringBuilder.toString());
			    return stringBuilder.toString();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "null";
		
	}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			//{"Data":[{"id":"2","rest_id":"3","review":"SUper","rating":"5"},{"id":"3","rest_id":"3","review":"Waste rice","rating":"1"}]}

			try {
				JSONObject main_obj = new JSONObject(result.toString());
				JSONArray orders = main_obj.getJSONArray("Data");
				review = new ArrayList<String>();
				rating = new ArrayList<Integer>();
				for (int i = 0; i < orders.length(); i++) {
					JSONObject itm = orders.getJSONObject(i);
					review.add(itm.getString("review"));
					rating.add((int)Float.parseFloat(itm.getString("rating")));
				}
				
				ReviewAdapter adap = new ReviewAdapter(Restaurant_Reviews.this,  review, rating);
				lv.setAdapter(adap);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "No reviews found", Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
				
			dialogs.cancelProgressDialog();
			super.onPostExecute(result);
		}
		
	}

}
