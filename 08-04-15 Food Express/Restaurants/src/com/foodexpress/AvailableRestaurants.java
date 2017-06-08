package com.foodexpress;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.foodexpress.adapters.RestaurantAdapter;
import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.NetworkConstants;
import com.foodexpress.network.NetworkEngine;

public class AvailableRestaurants extends ActionBarActivity {

	ListView restaurantsListview;
	RestaurantAdapter restaurantAdapter;
	ArrayList<String> restaurants, res_ids;
	ArrayList<Integer> ratings;
	SQLiteDatabase db;
	public static String resId ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.available_restaurants);
		restaurants = new ArrayList<String>();
		res_ids = new ArrayList<String>();
		ratings = new ArrayList<Integer>();
		db = openOrCreateDatabase("user_foodexpress_db", MODE_PRIVATE, null);
		
		String station = getIntent().getStringExtra("station");
		try {
			Cursor c = db.rawQuery("select * from list_restaurants where railway_station = '"+station+"'", null);
			c.moveToFirst();
			restaurants.add(c.getString(c.getColumnIndex("name")));
			res_ids.add(c.getString(c.getColumnIndex("rest_id")));
			try {
				String rating = c.getString(c.getColumnIndex("rating"));
				if(rating.equalsIgnoreCase("null")){
					ratings.add(0);
				}else{
					ratings.add(Integer.parseInt(rating));
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				ratings.add(0);

			}
			while(c.moveToNext()){
				restaurants.add(c.getString(c.getColumnIndex("name")));
				res_ids.add(c.getString(c.getColumnIndex("rest_id")));
				try {
					String rating = c.getString(c.getColumnIndex("rating"));
					if(rating.equalsIgnoreCase("null")){
						ratings.add(0);
					}else{
						ratings.add(Integer.parseInt(rating));
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					ratings.add(0);

				}
			}
			c.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
		restaurantsListview=(ListView)findViewById(R.id.restaurantslist);

restaurantAdapter=new RestaurantAdapter(getApplicationContext(),restaurants,ratings);		
		restaurantsListview.setAdapter(restaurantAdapter);
		restaurantsListview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int pos, long arg3) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(AvailableRestaurants.this);
		        builder.setTitle("Add a review");
		        LinearLayout lila1= new LinearLayout(AvailableRestaurants.this);
		        lila1.setOrientation(LinearLayout.VERTICAL);
		         // Use an EditText view to get user input
		         final EditText input = new EditText(AvailableRestaurants.this);
		         input.setHint("Write a Review");
		         final RatingBar bar = new RatingBar(AvailableRestaurants.this);
		         bar.setRating(5);
//		         bar.setRating(0);
		         bar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		         builder.setView(lila1);
			        lila1.addView(input);
			        lila1.addView(bar);
		 
		        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
		 
		            @Override
		            public void onClick(DialogInterface dialog, int whichButton) {
		                String value = input.getText().toString();
		               if(value.length()>0 && bar.getRating()>0){
		            	   new RestReview().execute(value, ""+bar.getRating(),res_ids.get(pos));
		               }else{
		            	   Toast.makeText(getApplicationContext(), "Please write a review and give some rating!", Toast.LENGTH_SHORT).show();
		               }
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

				return true;
			}
		});
		
		
	restaurantsListview.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
//			String res = restaurantsListview.getItemAtPosition(pos).toString();
			Intent i = new Intent(getApplicationContext(),User_ItemList.class);
			i.putExtra("resId", res_ids.get(pos));
			resId = res_ids.get(pos);
			startActivity(i);

		}
		
	});
	
	}

	private class RestReview extends AsyncTask<String, Void, String> {
		MyDialogs dialog = new MyDialogs();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.showProgressDialog(AvailableRestaurants.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpResponse httpResponse = null;
			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject jobject = new JSONObject();
				jobject.put("review",params[0]);
				jobject.put("rest_id",params[2]);
				jobject.put("rating",params[1]);
				
				jsonArray.put(jobject);
				Log.v("httpResponse", "" + jsonArray);
				NetworkEngine networkEngine = new NetworkEngine();
				httpResponse = networkEngine.postRequset(
						NetworkConstants.restReviews, "RestReviews",jsonArray);

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
	Toast.makeText(getApplicationContext(), "Review Posted", Toast.LENGTH_SHORT).show();
	super.onPostExecute(result);
	}
}


}
