package com.foodexpress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.ConnectionManager;
import com.foodexpress.network.NetworkConstants;
import com.foodexpress.network.NetworkEngine;

public class MainActivity extends ActionBarActivity {
	EditText edt_email, edt_password, edt_name, edt_phnmber, edt_address;
	MyDialogs dialogs;
	ConnectionManager connectionManager;
	Button bt_login, bt_signup, bt_guest;
	NetworkEngine networkEngine;
	SharedPreferences sh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edt_email = (EditText) findViewById(R.id.edt_usermail);
		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_address = (EditText) findViewById(R.id.edt_address);
		edt_phnmber = (EditText) findViewById(R.id.edt_phnumber);
		edt_name = (EditText) findViewById(R.id.edt_name);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_signup = (Button) findViewById(R.id.bt_signup);
		bt_guest = (Button) findViewById(R.id.bt_guest);
		dialogs = new MyDialogs();
		networkEngine = new NetworkEngine();
		connectionManager = new ConnectionManager();
		sh = getSharedPreferences("user_details", MODE_PRIVATE);
        
		edt_email.setText("res_bawarchi@foodexpress.com");
		edt_password.setText("123456");
		bt_guest.setVisibility(View.GONE);
        bt_guest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Editor ed = sh.edit();
				ed.putString("email", "Guest");
				ed.putString("phone", "000");
				ed.putString("name", "Guest");
				ed.commit();
//
				startActivity(new Intent(getApplicationContext(),SelectLocation.class));
//				startActivity(new Intent(getApplicationContext(),Restaurant_Home.class));
				finish();
				
			}
		});
	}

	public void login(View v) {

		if (bt_login.getText().toString()
				.equalsIgnoreCase(getResources().getString(R.string.login))) {

//			if (dialogs.validation(Arrays.asList(edt_email, edt_password))) {
//				if (connectionManager.isOnline(getApplicationContext())) {

					if(edt_email.getText().toString().startsWith("res_")){
						new RestLoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}else{
						new UserLoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
//						if(edt_email.getText().toString().equalsIgnoreCase("res_avanthi@foodexpress.com")){
//							if(edt_password.getText().toString().equalsIgnoreCase("123456")){
//								startActivity(new Intent(getApplicationContext(),Restaurant_Home.class));
//							}else{
//								dialogs.getDialog("Wrong Password", MainActivity.this);
//							}
//						}else{
//							dialogs.getDialog("Invalid email", MainActivity.this);
//						}
					
					
//					
//					
//				}
//
//			} else {
//
//				dialogs.getDialog("All fields reqired", MainActivity.this);
//			}

		} else if (bt_login.getText().toString()
				.equalsIgnoreCase("Submit")) {

			if (dialogs.validation(Arrays.asList(edt_email, edt_password,
					edt_name, edt_phnmber, edt_address))) {
				if (ConnectionManager.isOnline(getApplicationContext())) {

					new SignUpTask()
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					// dialogs.getDialog("SignUpTask().executeOnExecutor",MainActivity.this);

				}

			} else {

				dialogs.getDialog("All fields reqired", MainActivity.this);
			}

		}

	}

	public void signup(View v) {

		if (bt_signup.getText().toString()
				.equalsIgnoreCase(getResources().getString(R.string.signup))) {

			bt_login.setText("Submit");
			bt_signup.setText("Cancel");
			edt_address.setVisibility(View.VISIBLE);
			edt_name.setVisibility(View.VISIBLE);
			edt_phnmber.setVisibility(View.VISIBLE);

		} else if (bt_signup.getText().toString()
				.equalsIgnoreCase("Cancel")) {
			bt_login.setText(getResources().getString(R.string.login));
			bt_signup.setText(getResources().getString(R.string.signup));
			edt_address.setVisibility(View.GONE);
			edt_name.setVisibility(View.GONE);
			edt_phnmber.setVisibility(View.GONE);

		}

	}

	

	class SignUpTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	dialogs.showProgressDialog(MainActivity.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(NetworkConstants.register_Url);

			JSONArray jsonArray = new JSONArray();
			JSONObject jobject = new JSONObject();
			try {
				jobject.put("cust_name", edt_name.getText().toString());
				jobject.put("cust_email", edt_email.getText().toString());
				jobject.put("cust_phone", edt_phnmber.getText().toString());
				jobject.put("cust_password", edt_password.getText().toString());
				jobject.put("cust_address", edt_address.getText().toString());
				jsonArray.put(jobject);
				Map<String, String> kvPairs = new HashMap<String, String>();
			    kvPairs.put("Customers", jsonArray.toString());
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
			dialogs.cancelProgressDialog();
			try {
				JSONObject obj = new JSONObject(result);
				String res = obj.getString("Success");
				if(res.equalsIgnoreCase("You are Successfully Register")){
					Editor ed = sh.edit();
					ed.putString("email", edt_email.getText().toString());
					ed.putString("phone", edt_phnmber.getText().toString());
					ed.putString("name", edt_name.getText().toString());
					ed.commit();

					
					Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getApplicationContext(),User_Home.class);
					i.putExtra("result", result);
					startActivity(i);
					finish();
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			super.onPostExecute(result);
		}

	}

	class UserLoginTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	 dialogs.showProgressDialog(MainActivity.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpResponse httpResponse = null;
			try {

				JSONArray jsonArray = new JSONArray();
				JSONObject jobject = new JSONObject();
				jobject.put("cust_email", edt_email.getText().toString());
				jobject.put("cust_password", edt_password.getText().toString());
				jsonArray.put(jobject);

				httpResponse = networkEngine.postRequset(
						NetworkConstants.login_Url, NetworkConstants.login_Key,
						jsonArray);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.v("httpResponse", "" + httpResponse);
					InputStream inputStream = httpResponse.getEntity()
							.getContent();
					InputStreamReader inputStreamReader = new InputStreamReader(
							inputStream);
					BufferedReader bufferedReader = new BufferedReader(
							inputStreamReader);
					 StringBuilder stringBuilder = new StringBuilder();
					 String bufferedStrChunk;
					    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
					        stringBuilder.append(bufferedStrChunk);
					    }
					    
					    return stringBuilder.toString();
			}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "error";
		}
@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	dialogs.cancelProgressDialog();
	Log.v("httpResponse", "" + result);
	try {
		
		if(!result.equalsIgnoreCase("error")){
			
			try {
				JSONObject obj = new JSONObject(result);
				Editor ed = sh.edit();
				ed.putString("email", edt_email.getText().toString());
				ed.putString("phone", obj.getString("phone"));
//				ed.putString("name", edt_name.getText().toString());
				ed.commit();

				
			} catch (Exception e) {
				// TODO: handle exception
			}
			

			Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(),User_Home.class);
			i.putExtra("result", result);
			startActivity(i);
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
		}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	super.onPostExecute(result);
	}
}
	class RestLoginTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	 dialogs.showProgressDialog(MainActivity.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpResponse httpResponse = null;
		//	String message = null;
		//	RestaurantObject restaurantObject = null;
			try {

				JSONArray jsonArray = new JSONArray();
				JSONObject jobject = new JSONObject();
				jobject.put("email", edt_email.getText().toString());
				jobject.put("password", edt_password.getText().toString());
				jsonArray.put(jobject);

				httpResponse = networkEngine.postRequset(
						NetworkConstants.rest_login, NetworkConstants.res_login_key,
						jsonArray);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.v("httpResponse", "" + httpResponse);
					InputStream inputStream = httpResponse.getEntity().getContent();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(
							inputStreamReader);
					 StringBuilder stringBuilder = new StringBuilder();
					 String bufferedStrChunk;
					    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
					        stringBuilder.append(bufferedStrChunk);
					    }
					    return stringBuilder.toString();
				
									//	myApplication.restaurantsDatas.add(restaurantObject);
				//	Log.v("restaurantsDatas", ""+myApplication.restaurantsDatas.size()+myApplication.restaurantsDatas);

				}
         
				
			} catch (Exception e) {

			}

			return "error";
		}
@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	
	Log.v("response", " "+result);
	dialogs.cancelProgressDialog();
	try {
		JSONObject restaurantsJson=new JSONObject(result);
		String success = restaurantsJson.getString("Success");
		if(!success.equalsIgnoreCase("Login Failed")){
			Intent i =new Intent(getApplicationContext(),Restaurant_Home.class);
			i.putExtra("result", result);
			startActivity(i);
			finish();
		}
		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
	super.onPostExecute(result);
	
}
	}
}


