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

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.foodexpress.adapters.RestaurantOrdersAdapter;
import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.NetworkConstants;

public class Restaurant_Orders extends ActionBarActivity{
	SQLiteDatabase db;
	ListView lv;
	private ArrayList<String> orderIds,names,emails,phones,pnrs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.res_reviews);
		db = openOrCreateDatabase("rest_db", MODE_PRIVATE, null);

		new GetAllOrders().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		lv = (ListView)findViewById(R.id.listView1);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getApplicationContext(),ResOrderItems.class);
						i.putExtra("order_id", orderIds.get(pos));
						i.putExtra("pnr", pnrs.get(pos));
						i.putExtra("status", emails.get(pos));
						startActivity(i);
					}
		});
		
	
		
		
	}
	
	
	private class GetAllOrders extends AsyncTask<String, String, String>{
		MyDialogs dalog = new MyDialogs();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dalog.showProgressDialog(Restaurant_Orders.this);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(NetworkConstants.orderView);

			JSONArray jsonArray = new JSONArray();
			JSONObject jobject = new JSONObject();
			try {
				jobject.put("rest_id", Restaurant_Home.restaurent_id);
				jsonArray.put(jobject);
				Map<String, String> kvPairs = new HashMap<String, String>();
			    kvPairs.put("RestOrdersView", jsonArray.toString());
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
			dalog.cancelProgressDialog();
			if(!result.equalsIgnoreCase("Error")){
			//{"orders":[{"order_id":"1","pnr":"123456789","rest_id":"3","order_status":"complete","customer_name":"raajjj","customer_phone":"168899999"},{"order_id":"2","pnr":"123456786","rest_id":"3","order_status":"pending","customer_name":"raajjj","customer_phone":"168899999"},{"order_id":"3","pnr":"123456789","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"4","pnr":"111111111","rest_id":"3","order_status":"Completed","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"5","pnr":"000999878","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"6","pnr":"123456666","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"7","pnr":"00000000000","rest_id":"3","order_status":"pending","customer_name":"Guest","customer_phone":"000"},{"order_id":"8","pnr":"123456789","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"10","pnr":"123456789","rest_id":"3","order_status":"pending","customer_name":"raajjj","customer_phone":"168899999"},{"order_id":"11","pnr":"123456789","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"12","pnr":"8633605172","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""},{"order_id":"13","pnr":"8633605172","rest_id":"3","order_status":"pending","customer_name":"shree@gmail.com","customer_phone":""}]}
				db.execSQL("create table if not exists rest_orders(order_id varchar primary key,pnr varchar,order_status varchar,customer_name varchar,customer_phone varchar)");

				try {
					JSONObject main_obj = new JSONObject(result.toString());
					JSONArray orders = main_obj.getJSONArray("orders");
					for (int i = 0; i < orders.length(); i++) {
						JSONObject itm = orders.getJSONObject(i);
						String order_id = itm.getString("order_id");
						String pnr = itm.getString("pnr");
						String order_status = itm.getString("order_status");
						String customer_name = itm.getString("customer_name");
						String customer_phone = itm.getString("customer_phone");
						db.execSQL("insert or replace into rest_orders values('"+order_id+"','"+pnr+"','"+order_status+"','"+customer_name+"','"+customer_phone+"')");
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				try {
					getFromDB();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			super.onPostExecute(result);
		}
		
		
	}


	public void getFromDB() {
		// TODO Auto-generated method stub
		
		orderIds = new ArrayList<String>();
		names = new ArrayList<String>();
		phones = new ArrayList<String>();
		emails = new ArrayList<String>();
		pnrs = new ArrayList<String>();

		Cursor c = db.rawQuery("select * from rest_orders", null);
		c.moveToFirst();
		
		if(c.getCount()>0){

			orderIds.add(c.getString(c.getColumnIndex("order_id")));
			names.add(c.getString(c.getColumnIndex("customer_name")));
			emails.add(c.getString(c.getColumnIndex("order_status")));
			phones.add(c.getString(c.getColumnIndex("customer_phone")));
			pnrs.add(c.getString(c.getColumnIndex("pnr")));
			
			while(c.moveToNext()){
				orderIds.add(c.getString(c.getColumnIndex("order_id")));
				names.add(c.getString(c.getColumnIndex("customer_name")));
				emails.add(c.getString(c.getColumnIndex("order_status")));
				phones.add(c.getString(c.getColumnIndex("customer_phone")));
				pnrs.add(c.getString(c.getColumnIndex("pnr")));
			}
			RestaurantOrdersAdapter adap = new RestaurantOrdersAdapter(getApplicationContext(), orderIds, names, emails, phones, pnrs);
			lv.setAdapter(adap);
		}
		
		
		
		
		
	}
}
