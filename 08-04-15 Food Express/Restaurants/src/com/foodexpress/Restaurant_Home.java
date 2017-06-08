package com.foodexpress;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Restaurant_Home extends Activity{
	
	Button orders, items, reviews, pnr_status, train_status, activeItems;
	SQLiteDatabase db;
	public static String restaurent_id ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_home);
		
		orders =(Button)findViewById(R.id.bt_orders);
		items =(Button)findViewById(R.id.bt_items);
		reviews =(Button)findViewById(R.id.bt_reviews);
		pnr_status =(Button)findViewById(R.id.bt_pnr_status);
		train_status =(Button)findViewById(R.id.bt_train_status);
		activeItems = (Button)findViewById(R.id.bt_active);
		
		db = openOrCreateDatabase("rest_db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists rest_items(item_id varchar primary key,item_cost varchar,status varchar,rest_id varchar)");
		String result = getIntent().getStringExtra("result");
		db.execSQL("delete from rest_items");
		Log.v("result", "  "+result);
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray array = obj.getJSONArray("items");
			JSONArray arrayid = obj.getJSONArray("rest_id");
			JSONObject objId = arrayid.getJSONObject(0);
			restaurent_id = objId.getString("rest_id");
			for (int i = 0; i < array.length(); i++) {
				JSONObject itm = array.getJSONObject(i);
				String item_id = itm.getString("item_id");
				String item_cost = itm.getString("item_cost");
				String status = itm.getString("status");
				String rest_id = itm.getString("rest_id");
				restaurent_id = rest_id;
				db.execSQL("insert or replace into rest_items values('"+item_id+"','"+item_cost+"','"+status+"','"+rest_id+"')");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		activeItems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				startActivity(new Intent(getApplicationContext(),RestaurantActiveItems.class));
				
				
				
			}
		});
		
		
		items.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),Restaurent_Items.class));
					
			}
		});
		
		
		pnr_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(Restaurant_Home.this);

				alert.setTitle("PNR Number");

				// Set an EditText view to get user input 
				final EditText input = new EditText(Restaurant_Home.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  String value = input.getText().toString();
				  Intent i =	new Intent(getApplicationContext(),PNRStatus.class);
				  i.putExtra("pnr_number", value);
					startActivity(i);
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    dialog.cancel();
				  }
				});

				alert.show();
			
			}
		});
		
		train_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),FinalTrain.class));
			}
		});
		reviews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),Restaurant_Reviews.class));
			}
		});
		orders.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),Restaurant_Orders.class));
			}
		});
		
	}

}
