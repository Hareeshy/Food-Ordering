package com.foodexpress;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class FinalPNR extends Activity{
	TextView tv_chart,train_name;
	Button day,date,dep,train_number;
	LinearLayout list_passengers;
	Typeface ttf_train,ttf_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pnrlist);
		tv_chart=(TextView)findViewById(R.id.textView_chart);
		train_name=(TextView)findViewById(R.id.text_train_name);
		day=(Button)findViewById(R.id.button_day);
		date=(Button)findViewById(R.id.button_date);
		dep=(Button)findViewById(R.id.button_time);
		train_number=(Button)findViewById(R.id.button1);
		list_passengers=(LinearLayout)findViewById(R.id.ll_passengers);
	
		String result = getIntent().getStringExtra("result");
	//	ttf_train=Typeface.createFromAsset(getAssets(), "JI_Toy_Train.ttf");
	//	ttf_text=Typeface.createFromAsset(getAssets(), "FredokaOne-Regular.ttf");
		tv_chart.setTypeface(ttf_text);
		train_name.setTypeface(ttf_train);
		train_name.setSelected(true);
		
		train_name.setEllipsize(TruncateAt.MARQUEE);
		train_name.setSingleLine(true);
		
		parseJson(result);
	}

	@SuppressLint("DefaultLocale")
	private void parseJson(String result) {
		// TODO Auto-generated method stub
//		Log.v("result", "re "+result);
		try {
			JSONObject ress =new JSONObject(result);
			String status =ress.getString("chartStat");
		final	String train_number=ress.getString("trainNo");
			String train_name=ress.getString("trainName");
			String dep_time =ress.getString("trainDepart");
//			String arr_time =ress.getString("trainArrive");
//			String train_origin=ress.getString("trainOrigin");
//			String train_destination=ress.getString("trainDest");
//			String train_board =ress.getString("trainBoard");
//			String train_class=ress.getString("trainFareClass");
			String journey_date=ress.getString("trainJourney");
//			String reserved_upto=ress.getString("trainEmbark");
//			String trainBoardCode=ress.getString("trainBoardCode");
			tv_chart.setText(status);
			dep.setText(" Dep: "+dep_time);
			this.train_number.setText("TRAIN NO.\n"+train_number);
			this.train_number.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			this.train_name.setText("("+train_name.toUpperCase().replace(" ", "-")+")      "+"("+train_name.toUpperCase().replace(" ", "-")+") ");
			journey_date =journey_date.replaceAll(" ", "");
			
			
			SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = inFormat.parse(journey_date);
			SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
			String goal = outFormat.format(date);
			this.day.setText(goal);
			String sp[] =journey_date.split("-");
			
			
			journey_date=sp[0]+"-"+getMonth(sp[1])+"\n"+sp[2];
//			journey_date =parseDate(journey_date);
			this.date.setText(journey_date);
			
			JSONArray passengers =ress.getJSONArray("passengers");
			list_passengers.removeAllViews();
			for (int i = 0; i < passengers.length(); i++) {
				
				View v=getLayoutInflater().inflate(R.layout.passengers, null);
			//	View v1=(View)v.findViewById(R.id.view1);
	        //	LetterImageView pasenger=(LetterImageView)v.findViewById(R.id.textView1);
	        	Button pass=(Button)v.findViewById(R.id.textView1);
				Button berth=(Button)v.findViewById(R.id.textView2);
	        	Button status2=(Button)v.findViewById(R.id.textView3);
				JSONObject obj=passengers.getJSONObject(i);
				String trainBookingBerth=obj.getString("trainBookingBerth");
				String trainPassenger=obj.getString("trainPassenger");
				String trainCurrentStatus=obj.getString("trainCurrentStatus");
			//	String num =trainPassenger.replace("Passenger", "").trim();
				pass.setText(trainPassenger);
			//	pasenger.setOval(false);	
			//	pasenger.setLetter(num.charAt(0));
//				berth.setTextColor(pasenger.newColor());
//				status2.setTextColor(pasenger.newColor());
			//	v1.setBackgroundColor(pasenger.newColor());
	        	berth.setText(trainBookingBerth.replace(" ", ""));
	        	status2.setText(" Status:"+trainCurrentStatus.replace(" ", ""));
	        	list_passengers.addView(v);
	        	
			}

		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
		}
		
	}
	private String getMonth(String string) {
		// TODO Auto-generated method stub
		int mon=Integer.parseInt(string);
		switch (mon) {
		case 1: return "JAN";
		case 2: return "FEB";
		case 3: return "MAR";
		case 4: return "APR";
		case 5: return "MAY";
		case 6: return "JUN";
		case 7: return "JUL";
		case 8: return "AUG";
		case 9: return "SEP";
		case 10: return "OCT";
		case 11: return "NOV";
		case 12: return "DEC";

		default: return string;
			
		}
	}

	public String parseDate(String time) {
	    String inputPattern = "dd-mm-yyyy";
	    String outputPattern = "dd-MMM**yyyy";
	    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
	    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

	    Date date = null;
	    String str = null;

	    try {
	        date = inputFormat.parse(time);
	        str = outputFormat.format(date);
	    } catch (Exception e) {
//	        e.printStackTrace();
	    }
	    return str.replace("**", "\n");
	}
	
		
}
