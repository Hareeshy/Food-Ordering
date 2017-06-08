package com.foodexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class User_Home extends ActionBarActivity{
	Button orders, cancel, modify, pnr_status, train_status, activitylist;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_home);
		
	SharedPreferences	sh = getSharedPreferences("user_details", MODE_PRIVATE);
	String welcome = sh.getString("name", "");
	setTitle("Welcome "+welcome);
		
		orders =(Button)findViewById(R.id.bt_orders);
		cancel =(Button)findViewById(R.id.bt_items);
		modify =(Button)findViewById(R.id.bt_reviews);
		pnr_status =(Button)findViewById(R.id.bt_pnr_status);
		train_status =(Button)findViewById(R.id.bt_train_status);
		activitylist = (Button)findViewById(R.id.bt_active);
		db = openOrCreateDatabase("rest_db", MODE_PRIVATE, null);

		orders.setText("Book Order");
		cancel.setText("Cancel Order");
		modify.setText("Modify Order");
//		modify.setVisibility(View.GONE);
		activitylist.setVisibility(View.GONE);
		
		
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),Modify_Orders.class);
//				i.putExtra("result", result);
				startActivity(i);	
			}
		});
		final String result = getIntent().getStringExtra("result");
		
		orders.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),SelectLocation.class);
				i.putExtra("result", result);
				startActivity(i);	
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),User_Orders.class);
//				i.putExtra("result", result);
				startActivity(i);	
			}
		});
		pnr_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(User_Home.this);

				alert.setTitle("PNR Number");

				// Set an EditText view to get user input 
				final EditText input = new EditText(User_Home.this);
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
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		super.onBackPressed();
	}

}
