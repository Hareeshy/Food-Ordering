package com.foodexpress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.foodexpress.adapters.UserItemsAdapter;
import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.NetworkConstants;
import com.foodexpress.network.NetworkEngine;

public class User_ItemList extends ActionBarActivity{
	
	ListView lv;
	//RestaurantAdapter restaurantAdapter;
	ArrayList<String> item_name,item_id,item_image;
	ArrayList<Integer> item_cost;
	public ArrayList<String> selectedItems;
	Button ordernow;
	
	MyDialogs dialogs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.available_restaurants2);
		dialogs = new MyDialogs();
		lv=(ListView)findViewById(R.id.restaurantslist);
		ordernow = (Button)findViewById(R.id.button1);
		ordernow.setVisibility(View.VISIBLE);

		String res_id = getIntent().getStringExtra("resId");
		new getItems().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,res_id);

		ordernow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				SparseBooleanArray checked = lv.getCheckedItemPositions();
//				 selectedItems = new ArrayList<String>();
//				if(checked.size()>0){
//				for (int i = 0; i < lv.getAdapter().getCount(); i++) {
//				    if (checked.get(i)) {
//				        selectedItems.add(item_name.get(i));
//				    }
//				}
//				}
				if(UserItemsAdapter.selectedName.size()>0){
					startActivity(new Intent(getApplicationContext(),OrderItems.class));
				}else{
					Toast.makeText(getApplicationContext(), "Select atleast 1 item", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
	}
	
	private class getItems extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogs.showProgressDialog(User_ItemList.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpResponse httpResponse = null;
			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject jobject = new JSONObject();
				jobject.put("rest_id",params[0]);
				jsonArray.put(jobject);
				Log.v("httpResponse", "" + jsonArray);
				NetworkEngine networkEngine = new NetworkEngine();
				httpResponse = networkEngine.postRequset(
						NetworkConstants.list_res_items, NetworkConstants.list_res_itm,jsonArray);

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
	try {
		
		if(!result.equalsIgnoreCase("error")){
		
			JSONObject obj = new JSONObject(result);
			String res = obj.getString("Success");
			Log.v("data ", res);
			JSONArray arr = obj.getJSONArray("Success");
			item_name = new ArrayList<String>();
			item_id = new ArrayList<String>();
			item_cost = new ArrayList<Integer>();
			item_image = new ArrayList<String>();

			for (int i = 0; i < arr.length(); i++) {
				JSONObject itm= arr.getJSONObject(i);
				if(!itm.getString("status").equalsIgnoreCase("1")){
					item_name.add(itm.getString("menu_items"));
					item_cost.add(Integer.parseInt(itm.getString("item_cost")));
					item_id.add(itm.getString("item_id"));
					item_image.add(itm.getString("item_image"));
				}
			}
			
			//ArrayAdapter<String> adap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,item_name);
			
			UserItemsAdapter adap = new UserItemsAdapter(getApplicationContext(), item_name, item_cost);
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			lv.setAdapter(adap);
		}else{
			Toast.makeText(getApplicationContext(), "Items not found", Toast.LENGTH_SHORT).show();
		}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	super.onPostExecute(result);
	}
}

}
