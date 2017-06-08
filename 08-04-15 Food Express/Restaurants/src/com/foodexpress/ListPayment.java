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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.foodexpress.dialogs.MyDialogs;
import com.foodexpress.network.NetworkConstants;

public class ListPayment extends ActionBarActivity {
	
	MyDialogs dialog;
	RadioButton cards,netbank,cod;
	LinearLayout cardsView ;
	Spinner banks;
	EditText cardnumber,expdate,expmnth,expyear,cvv,name;
	Button payment;
	String pnr;
	String urls[] ={"","https://retail.onlinesbi.com/retail/login.htm#","https://www.onlineandhrabank.net.in/BankAwayRetail/(S(oulrr155q32lchmiezlkkj45))/web/L001/retail/jsp/user/RetailSignOn.aspx?RequestId=40357018","https://www.onlinesbh.com/retail/sbhlogin.htm",
			"https://netbanking.hdfcbank.com/netbanking/?_ga=1.230325979.679702345.1428255885","http://www.icicibank.com/Personal-Banking/insta-banking/internet-banking/index.page","https://pib.dcbbank.com/corp/BANKAWAY?Action.RetUser.Init.001=Y"
			,"https://www.denaiconnect.co.in/corp/BANKAWAY?Action.RetUser.Init.001=Y&AppSignonBankId=018&AppType=corporate",
			"https://netbanking.apmaheshbank.com:8080/internetbanking/lhome.do","https://www.kvbnet.co.in/retail/entry?&fldAppId=RR&fldTxnId=PLN&fldScrnSeqNbr=01&fldLangId=eng&fldDeviceId=01"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_payment);
		
		dialog = new MyDialogs();
		pnr = getIntent().getStringExtra("pnr");

		cards = (RadioButton)findViewById(R.id.radio0);
		netbank = (RadioButton)findViewById(R.id.radio1);
		cod = (RadioButton)findViewById(R.id.radio3);
		cardsView = (LinearLayout)findViewById(R.id.CreditCardTablelayout);
		banks = (Spinner)findViewById(R.id.spinner1);
		cod.setVisibility(View.GONE);
		cardnumber =(EditText)findViewById(R.id.cardNumberTextEdit);
		expdate =(EditText)findViewById(R.id.cardDateEditText);
		expmnth =(EditText)findViewById(R.id.cardMonthEditText);
		
		expyear =(EditText)findViewById(R.id.cardYearEditText);
		cvv =(EditText)findViewById(R.id.verificationEditText);
		name =(EditText)findViewById(R.id.cardHolderEditText);
		
		payment=(Button)findViewById(R.id.registerButton);

		
	payment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(cards.isChecked()){
					cardPayment();
				}else if(netbank.isChecked()){
					if(banks.getSelectedItemPosition()!=0 && netbank.isChecked()){
				//	new Orderitems().execute(pnr);
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(urls[banks.getSelectedItemPosition()]));
					startActivity(i);
					
					}else{
						Toast.makeText(getApplicationContext(), "Please select a bank for payment! ", Toast.LENGTH_SHORT).show();
					}
				}else{
					new Orderitems().execute(pnr);
				}
			}
		});
	
		cards.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean val) {
				// TODO Auto-generated method stub
				if(val){
					banks.setVisibility(View.GONE);
					cardsView.setVisibility(View.VISIBLE);
				}
			}
		});
		
		cod.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean val) {
				// TODO Auto-generated method stub
				if(val){
					banks.setVisibility(View.GONE);
					cardsView.setVisibility(View.GONE);
				}
			}
		});
		netbank.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean val) {
				// TODO Auto-generated method stub
				if(val){
					banks.setVisibility(View.VISIBLE);
					cardsView.setVisibility(View.GONE);
				}
			}
		});
		
	}
	
	protected void cardPayment() {
		// TODO Auto-generated method stub
		String num=cardnumber.getText().toString();
		String cvv1=cvv.getText().toString();
		String mnth=expdate.getText().toString();
		String yr=expyear.getText().toString();
		String name1=name.getText().toString();
		if(num.length()==16){
			if(mnth.length()==2){
				if(yr.length()==4 && Integer.parseInt(yr)>2017){
					if(cvv1.length()==3){
						if(name1.length()>3){
							new Orderitems().execute(pnr);
							
						}else{
							Toast.makeText(getApplicationContext(), "Enter valid name", Toast.LENGTH_SHORT).show();
						}

					}else{
						Toast.makeText(getApplicationContext(), "Enter cvv", Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(getApplicationContext(), "Enter valid year", Toast.LENGTH_SHORT).show();
				}

			}else{
				Toast.makeText(getApplicationContext(), "Enter valid month", Toast.LENGTH_SHORT).show();
			}

		}else{
			Toast.makeText(getApplicationContext(), "Enter valid card number", Toast.LENGTH_SHORT).show();
		}

	}

	private class Orderitems extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.showProgressDialog(ListPayment.this);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpResponse httpResponse = null;
			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject jobject = new JSONObject();
				jobject.put("pnr",params[0]);
				jobject.put("restId",AvailableRestaurants.resId);
				
				JSONArray item = new JSONArray();
				for (int i = 0; i < OrderItems.names.size(); i++) {
					
					JSONObject onb = new JSONObject();
					onb.put("itemId", "0");
					onb.put("itemName",OrderItems. names.get(i));
					onb.put("itemQuantity", OrderItems.quantities.get(i));
					onb.put("itemCost", ""+OrderItems.costs.get(i));
					item.put(onb);
					
				}
				jobject.put("items", item);
				SharedPreferences sh = getSharedPreferences("user_details", MODE_PRIVATE);
				JSONObject obj = new JSONObject();
				obj.put("name", sh.getString("email", "NA"));
				obj.put("phone", sh.getString("phone", "000"));
				jobject.put("user", obj);
				
				jsonArray.put(jobject);
				Log.v("httpResponse", "" + jsonArray);
				HttpClient httpClient = new DefaultHttpClient();
			    HttpPost httpPost = new HttpPost(NetworkConstants.buy_orders);

				Map<String, String> kvPairs = new HashMap<String, String>();
				    kvPairs.put(NetworkConstants.buyOrders_key, jsonArray.toString());
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
			     httpResponse = httpClient.execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					
					
					 InputStream inputStream = httpResponse.getEntity().getContent();
					    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					    StringBuilder stringBuilder = new StringBuilder();
					    String bufferedStrChunk;
					    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
					        stringBuilder.append(bufferedStrChunk);
					    }
					    Log.v("httpResponse order", "" + stringBuilder.toString());
					return "success";
			}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "BAD";
		}
@SuppressWarnings("deprecation")
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
				PendingIntent i=PendingIntent.getActivity(ListPayment.this, 0,
														new Intent(ListPayment.this, User_Home.class),
														0);
				
				note.setLatestEventInfo(ListPayment.this, "Order Placed Successfully",
										"Thankyou for using FoodExpress.", i);
				
				//After uncomment this line you will see number of notification arrived
				//note.number=2;
//				note.DEFAULT_VIBRATE;
				
				mgr.notify(10, note);
				startActivity(new Intent(getApplicationContext(),User_Home.class));
				finish();

				
			Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "Order Failed", Toast.LENGTH_SHORT).show();
		}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	super.onPostExecute(result);
	}
}

}
