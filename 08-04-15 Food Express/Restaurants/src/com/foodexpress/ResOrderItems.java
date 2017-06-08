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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodexpress.adapters.OrderAdapter;
import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.NetworkConstants;
import com.foodexpress.network.NetworkEngine;

public class ResOrderItems extends ActionBarActivity{
	
	ListView lv;
	ArrayList<String> names;
	ArrayList<Integer> costs;
	ArrayList<String> quantities;
	OrderAdapter adap;
	Button completeNow,PNR;
	TextView tot;
	MyDialogs dialog;
	SQLiteDatabase db;
	String pnr ;
	String OrderId ;
	String value = "";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.res_order);
		
		names = new ArrayList<String>();
		costs = new ArrayList<Integer>();
		quantities = new ArrayList<String>();
		completeNow = (Button)findViewById(R.id.bt_buyNow);
		PNR = (Button)findViewById(R.id.bt_pnr);
		db = openOrCreateDatabase("rest_db", MODE_PRIVATE, null);

		 OrderId = getIntent().getStringExtra("order_id");
			pnr = getIntent().getStringExtra("pnr");
			value = getIntent().getStringExtra("status");
					 
		new GetOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		
		
		PNR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i =	new Intent(getApplicationContext(),PNRStatus.class);
				  i.putExtra("pnr_number", pnr);
					startActivity(i);
			}
		});
		
		tot = (TextView)findViewById(R.id.ordertotal);
		lv = (ListView)findViewById(R.id.listView1);
		dialog = new MyDialogs();
		
		
		completeNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String msg = "";
				if(value.equalsIgnoreCase("complete")){
					msg = "Order status changes from Complete to Pending";
					value = "Pending";
				}else{
					msg = "Order status changes from Pending to Complete";
					value = "Complete";
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ResOrderItems.this);
		        builder.setTitle("Are you sure");
		        builder.setMessage(msg);
		 
		        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		 
		            @Override
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	   new Orderitems().execute(value);
		            	   
		                return;
		            }

					
		        });
		 
		        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                return;
		            }
		        }); 
		        builder.create();
		        builder.show();
			}
		});
		
	}

	private class Orderitems extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.showProgressDialog(ResOrderItems.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpResponse httpResponse = null;
			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject jobject = new JSONObject();
				jobject.put("order_status",params[0]);
				jobject.put("order_id",OrderId);
				jsonArray.put(jobject);
				Log.v("httpResponse", "" + jsonArray);
				NetworkEngine networkEngine = new NetworkEngine();
				httpResponse = networkEngine.postRequset(
						NetworkConstants.orderStatus, "OrderStatus",jsonArray);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.v("httpResponse order", "" + httpResponse);
					return "success";
			}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "BAD";
		}
@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	dialog.cancelProgressDialog();
	try {
		
		if(!result.equalsIgnoreCase("error")){
			
			final NotificationManager mgr=
					(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				Notification note=new Notification(R.drawable.ic_launcher,
																"FoodExpress",
																System.currentTimeMillis());
				
				// This pending intent will open after notification click
				PendingIntent i=PendingIntent.getActivity(ResOrderItems.this, 0,
														new Intent(ResOrderItems.this, SelectLocation.class),
														0);
				
				note.setLatestEventInfo(ResOrderItems.this, "Order Status Changed",
										"Please send items in 30min", i);
				
				
				mgr.notify(10, note);
				
			Toast.makeText(getApplicationContext(), "Order Status Changed", Toast.LENGTH_SHORT).show();
		}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	super.onPostExecute(result);
	}
}
	private class GetOrder extends AsyncTask<String, String, String>{
		MyDialogs dalog = new MyDialogs();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dalog.showProgressDialog(ResOrderItems.this);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(NetworkConstants.orderDetailsView);

			JSONArray jsonArray = new JSONArray();
			JSONObject jobject = new JSONObject();
			try {
				jobject.put("order_id", OrderId);
				jsonArray.put(jobject);
				Map<String, String> kvPairs = new HashMap<String, String>();
			    kvPairs.put("OrderDetailsView", jsonArray.toString());
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
		
				try {
					JSONObject main_obj = new JSONObject(result.toString());
					JSONArray orders = main_obj.getJSONArray("ordered_items");
					quantities = new ArrayList<String>();
					names = new ArrayList<String>();
					costs = new ArrayList<Integer>();
					for (int i = 0; i < orders.length(); i++) {
						JSONObject itm = orders.getJSONObject(i);
						quantities.add(itm.getString("item_quantity"));
						names.add(itm.getString("item_name"));
						costs.add(Integer.parseInt(itm.getString("item_cost")));
					}
					
				int total = 0;
				for (int i = 0; i < costs.size(); i++) {
					total = total + costs.get(i);
				}
				tot.setText("Rs. "+total);
				adap = new OrderAdapter(getApplicationContext(),names,quantities,costs);
				lv.setAdapter(adap);	

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			super.onPostExecute(result);
		}
		
		
	}


	
}
