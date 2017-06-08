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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.ConnectionManager;
import com.foodexpress.network.NetworkConstants;
import com.squareup.picasso.Picasso;

public class RestaurantItemDetails extends Activity{
	TextView tv_heading;
	EditText item_cost, item_numbers;
	Button subimt, cancel;
	RadioGroup grp;
	String image;
	String item_id ;
	int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_details);
		
		tv_heading = (TextView)findViewById(R.id.textView1);
		item_cost = (EditText)findViewById(R.id.editText1);
		item_numbers = (EditText)findViewById(R.id.editText2);
		subimt = (Button)findViewById(R.id.bt_submit);
		cancel = (Button)findViewById(R.id.bt_cancel);
		grp = (RadioGroup)findViewById(R.id.radioGroup1);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		subimt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cost = item_cost.getText().toString();
				//String num = item_numbers.getText().toString();
				int radioButtonID = grp.getCheckedRadioButtonId();
				View radioButton = grp.findViewById(radioButtonID);
				int idx = grp.indexOfChild(radioButton);
				
				if(cost.length()>1){
					//if(num.length()>0){
					sendDataToServer(cost,idx);
					pos = idx;
				//	}else{
				//		Toast.makeText(getApplicationContext(), "Cost is not valid", Toast.LENGTH_SHORT).show();
					//	}
				}else{
					Toast.makeText(getApplicationContext(), "Cost is not valid", Toast.LENGTH_SHORT).show();
				}

				
			}
		});
		
		String value = getIntent().getStringExtra("value");
		String image = getIntent().getStringExtra("image");
		 item_id = getIntent().getStringExtra("item_id");
		tv_heading.setText(value);
		
		ImageView iv = (ImageView)findViewById(R.id.imageView1);
		Picasso.with(getApplicationContext()).load(NetworkConstants.image_url+image).into(iv);
		
	}

	protected void sendDataToServer(String cost,  int idx) {
		// TODO Auto-generated method stub
		
		if(ConnectionManager.isOnline(getApplicationContext())){
			try{
				JSONArray arr = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("rest_id", Restaurant_Home.restaurent_id);
				obj.put("item_id", item_id);
				obj.put("item_cost", cost);
//				obj.put("numbers", num);
				obj.put("status", idx);
				arr.put(obj);
				Log.v("data", ""+arr.toString());
			
				new SendData().execute(arr);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}
	
	private class SendData extends AsyncTask<JSONArray, String, String>{
		MyDialogs dialogs = new MyDialogs();
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialogs.showProgressDialog(RestaurantItemDetails.this);
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(JSONArray... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(NetworkConstants.res_item_cost);
		    Log.v("response", "doInBack");
			try {
				Map<String, String> kvPairs = new HashMap<String, String>();
			    kvPairs.put(NetworkConstants.res_item_cost_key, params[0].toString());
			    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(kvPairs.size());
			    String k, v;
			    Iterator<String> itKeys = kvPairs.keySet().iterator();
			    while (itKeys.hasNext()) {
			        k = itKeys.next();
			        v = kvPairs.get(k);
			        nameValuePairs.add(new BasicNameValuePair(k, v));
			    }
			    Log.v("response", " "+nameValuePairs);

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
			    return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "null";

		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equalsIgnoreCase("success")){
				
				if(pos == 0){
					Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "Item is Inactive!", Toast.LENGTH_SHORT).show();

				}
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Item Added Failed, Please try again", Toast.LENGTH_SHORT).show();
			}
			dialogs.cancelProgressDialog();
			super.onPostExecute(result);
		}
		
	}
	
	

}
