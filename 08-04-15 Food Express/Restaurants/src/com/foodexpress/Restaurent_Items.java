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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.foodexpress.network.NetworkConstants;

public class Restaurent_Items extends Activity{
	ListView lv;
	Button back,next;
	public static ArrayList<String> selectedItems = new ArrayList<String>();
	public static ArrayList<String> images = new ArrayList<String>();
	public static ArrayList<String> item_ids = new ArrayList<String>();

	//	String items[] = {"Idly","Plain Dosa","Masala Dosa","Upma","Biryani","Chicken Biryani"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.res_menuitems);
		
		back = (Button)findViewById(R.id.bt_back);
		next = (Button)findViewById(R.id.bt_next);
		lv = (ListView)findViewById(R.id.listView1);
		
		new GetAllItems().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	
	private class GetAllItems extends AsyncTask<String, String, String>{

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
				final ArrayList<String> images = new ArrayList<String>();
				final ArrayList<String> item_idss = new ArrayList<String>();
			
				try {
					JSONObject main_obj = new JSONObject(result.toString());
					JSONArray arr = main_obj.getJSONArray("items");
					for (int i = 0; i < arr.length(); i++) {
						
					JSONObject obj = arr.getJSONObject(i);
					String name = obj.getString("menu_items");
					String item_id = obj.getString("item_id");
					String item_image = obj.getString("item_image");
					list_items.add(name);
					images.add(item_image);
					item_idss.add(item_id);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayAdapter<String> adap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,list_items);
				lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				lv.setAdapter(adap);
				next.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SparseBooleanArray checked = lv.getCheckedItemPositions();
						selectedItems = new ArrayList<String>();
						Restaurent_Items.images= new ArrayList<String>();
						item_ids = new ArrayList<String>();
						if(checked.size()>0){
						for (int i = 0; i < lv.getAdapter().getCount(); i++) {
						    if (checked.get(i)) {
						        selectedItems.add(list_items.get(i));
						        Restaurent_Items.images.add(images.get(i));
						        item_ids.add(item_idss.get(i));
						        
						    }
						}
						}
						if(selectedItems.size()>0){
							startActivity(new Intent(getApplicationContext(),RestaurantSelectedItems.class));
						}
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
