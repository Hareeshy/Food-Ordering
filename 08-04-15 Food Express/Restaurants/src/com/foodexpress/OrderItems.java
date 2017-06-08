package com.foodexpress;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodexpress.adapters.OrderAdapter;
import com.foodexpress.adapters.UserItemsAdapter;
import com.foodexpress.dialogs.MyDialogs;

public class OrderItems extends ActionBarActivity{
	
	ListView lv;
	public static ArrayList<String> names;
	public static ArrayList<Integer> costs;
	public static ArrayList<String> quantities;
	OrderAdapter adap;
	Button buyNow, cancel;
	TextView tot;
	MyDialogs dialog;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		
		names = new ArrayList<String>();
		costs = new ArrayList<Integer>();
		quantities = new ArrayList<String>();
		buyNow = (Button)findViewById(R.id.bt_buyNow);
		tot = (TextView)findViewById(R.id.ordertotal);
		lv = (ListView)findViewById(R.id.listView1);
		dialog = new MyDialogs();
		
		cancel = (Button)findViewById(R.id.bt_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		
		for (int i = 0; i < UserItemsAdapter.selectedName.size(); i++) {
			quantities.add("1");
			names.add(UserItemsAdapter.selectedName.get(i));
			costs.add(UserItemsAdapter.selectedCost.get(i));
		}
		int total = 0;
		for (int i = 0; i < costs.size(); i++) {
			total = total + costs.get(i);
		}
		tot.setText("Rs. "+total);
		
		adap = new OrderAdapter(getApplicationContext(),names,quantities,costs);
		lv.setAdapter(adap);	
		
		
		
		buyNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(OrderItems.this);
		        builder.setTitle("Need PNR number!");
		 
		         // Use an EditText view to get user input.
		         final EditText input = new EditText(OrderItems.this);
		         input.setHint("Enter PNR number");
		         builder.setView(input);
		 
		        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
		 
		            @Override
		            public void onClick(DialogInterface dialog, int whichButton) {
		                String value = input.getText().toString();
		               if(value.length()>8){
		            	   Intent i = new Intent(getApplicationContext(),ListPayment.class);
		            	   i.putExtra("pnr", value);
		            	   startActivity(i);
		            	   
//		            	   showPaymentOption(value);
		               }else{
		            	   Toast.makeText(getApplicationContext(), "Enter Valid PNR Number", Toast.LENGTH_SHORT).show();
		               }
		                return;
		            }
					
		        });
		 
		        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
		 
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

}
