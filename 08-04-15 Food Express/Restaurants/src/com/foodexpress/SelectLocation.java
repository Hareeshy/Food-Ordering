package com.foodexpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;


public class SelectLocation extends ActionBarActivity {
	
	ArrayList<String> list_cities;
	Spinner cities;
	ListView list_stations;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_location);
		cities = (Spinner)findViewById(R.id.sp_city);
		list_stations = (ListView)findViewById(R.id.listView1);
		db = openOrCreateDatabase("user_foodexpress_db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists list_restaurants(rest_id varchar primary key,name varchar,phone varchar, address varchar,city varchar, state varchar,country varchar,railway_station varchar, rating varchar)");
		
		String result = getIntent().getStringExtra("result");
		
		try {
			
			JSONObject restaurantsJson=new JSONObject(result);
			Log.v("restaurantsJson", ""+restaurantsJson);
			JSONArray responsearray=restaurantsJson.getJSONArray("Success");
			for (int i = 0; i < responsearray.length(); i++) {

		JSONObject restaurant=responsearray.getJSONObject(i);
		     
		   String resId=restaurant.getString("rest_id");
		   String resName=restaurant.getString("name");
		   String resPhone=restaurant.getString("phone");
		   String resAddress=restaurant.getString("address");
		   String resStation = restaurant.getString("railway_station");
		   String resCity=restaurant.getString("city");
		   String resState=restaurant.getString("state");
		   String resCountry=restaurant.getString("country");
		   String resRating=restaurant.getString("rating");
		   
		   db.execSQL("insert or replace into list_restaurants values('"+resId+"','"+resName+"','"+resPhone+"','"+resAddress+"','"+resCity+"','"+resState+"','"+resCountry+"','"+resStation+"','"+resRating+"')");
		   
		   
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		list_cities = new ArrayList<String>();
		
		try {
			Cursor c = db.rawQuery("select DISTINCT city from list_restaurants", null);
			c.moveToFirst();
			//list_cities.add("Select a city");
			list_cities.add(c.getString(c.getColumnIndex("city")));
			
			while(c.moveToNext()){
				list_cities.add(c.getString(c.getColumnIndex("city")));
			}
			c.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		ArrayAdapter<String> spinner_Adap = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,list_cities);
		cities.setAdapter(spinner_Adap);
		cities.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				ArrayList<String> stations = new ArrayList<String>();
				Cursor c = db.rawQuery("select Distinct railway_station from list_restaurants where city ='"+cities.getSelectedItem().toString()+"'", null);
				c.moveToFirst();
				if(c.getCount()>0){
					stations.add(c.getString(c.getColumnIndex("railway_station")));
					while (c.moveToNext()) {
						stations.add(c.getString(c.getColumnIndex("railway_station")));
					}
					ArrayAdapter<String> adap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stations);
					list_stations.setAdapter(adap);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	
		list_stations.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				
				String station = list_stations.getItemAtPosition(pos).toString();
				Intent i = new Intent(getApplicationContext(),AvailableRestaurants.class);
				i.putExtra("station", station);
				startActivity(i);
				
			}
		});
		
		
	}

	
	
}
