package com.foodexpress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.foodexpress.adapters.ActiveItemsAdapter;
import com.foodexpress.network.NetworkConstants;

public class RestaurantActiveItems extends ActionBarActivity{
	SQLiteDatabase db;
	ArrayList<String> itemIds,itemCosts,itemStatus,itemNames,itemImages;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.available_restaurants);
		
		lv = (ListView)findViewById(R.id.restaurantslist);
		
		new GetAllItems().execute();

		
	}
	private class GetAllItems extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			db = openOrCreateDatabase("rest_db", MODE_PRIVATE, null);
			
			Cursor c = db.rawQuery("select * from rest_items", null);
			itemIds = new ArrayList<String>();
			itemCosts = new ArrayList<String>();
			itemStatus = new ArrayList<String>();
			itemNames = new ArrayList<String>();
			itemImages = new ArrayList<String>();
			
			c.moveToFirst();
			itemIds.add(c.getString(c.getColumnIndex("item_id")));
			itemCosts.add(c.getString(c.getColumnIndex("item_cost")));
			itemStatus.add(c.getString(c.getColumnIndex("status")));
			while(c.moveToNext()){
				itemIds.add(c.getString(c.getColumnIndex("item_id")));
				itemCosts.add(c.getString(c.getColumnIndex("item_cost")));
				itemStatus.add(c.getString(c.getColumnIndex("status")));
			}
			
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			 InputStream inputStream = null;
		        String result = "";
			 try {
		            HttpClient httpclient = new DefaultHttpClient();
		            HttpResponse httpResponse = httpclient.execute(new HttpGet(NetworkConstants.all_items));
		            inputStream = httpResponse.getEntity().getContent();
		            if(inputStream != null)
		                result = convertInputStreamToString(inputStream);
		            else
		                result = "Error";
		 
		        } catch (Exception e) {
		            Log.v("InputStream", e.getLocalizedMessage());
		            result = "Error";
		        }
			
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			if(!result.equalsIgnoreCase("Error")){
				final ArrayList<String> list_items = new ArrayList<String>();
				final ArrayList<String> item_idss = new ArrayList<String>();
				final ArrayList<String> images = new ArrayList<String>();
							
				try {
					JSONObject main_obj = new JSONObject(result.toString());
					JSONArray arr = main_obj.getJSONArray("items");
						for (int i = 0; i < arr.length(); i++) {
							JSONObject obj = arr.getJSONObject(i);
							list_items.add(obj.getString("menu_items"));
							item_idss.add(obj.getString("item_id"));
							String item_image = obj.getString("item_image");
							images.add(item_image);
							
						}
						
						for (int i = 0; i < itemIds.size(); i++) {
							
							int po = item_idss.indexOf(itemIds.get(i));
								itemNames.add(list_items.get(po));
								itemImages.add(images.get(po));
						}
						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ActiveItemsAdapter adap = new ActiveItemsAdapter(getApplicationContext(), itemNames, itemStatus, itemCosts);
				lv.setAdapter(adap);
				
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
							long arg3) {
						// TODO Auto-generated method stub
						String image = itemImages.get(pos);
						Intent i = 	new Intent(getApplicationContext(),RestaurantItemDetails.class);
						i.putExtra("value", itemNames.get(pos));
						i.putExtra("image", image);
						i.putExtra("item_id", itemIds.get(pos));
						startActivity(i);
					}
				});
			}
			super.onPostExecute(result);
		}
		
	}
	  private static String convertInputStreamToString(InputStream inputStream) throws IOException{
	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	        String line = "";
	        String result = "";
	        while((line = bufferedReader.readLine()) != null)
	            result += line;
	 
	        inputStream.close();
	        return result;
	 
	    }

}
