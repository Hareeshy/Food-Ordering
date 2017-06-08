package com.foodexpress.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.foodexpress.R;

public class RestaurantAdapter extends ArrayAdapter<String>{

	public Context context;
	private ArrayList<String> restaurantslist;
	ArrayList<Integer> ratings;

	
	public RestaurantAdapter(Context context, 
			ArrayList<String> restaurants, ArrayList<Integer> ratings) {
		super(context, R.layout.list_item, restaurants);
		// TODO Auto-generated constructor stub
	this.context=context;
	this.restaurantslist=restaurants;
	this.ratings = ratings;
	}
	
	public class ViewHolder{
		TextView resName,resAddress,resNumber;
		ImageView resImage;
		RatingBar bar;
		
		
	}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
     ViewHolder viewHolder;
     if (convertView==null) {
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewHolder=new ViewHolder();
		convertView=inflater.inflate(R.layout.list_item, null, true);
    	 
    	viewHolder.resName=(TextView)convertView.findViewById(R.id.text_heading);
    	viewHolder.bar = (RatingBar)convertView.findViewById(R.id.ratingBar1);
    	
//    	viewHolder.resImage=(ImageView)convertView.findViewById(R.id.img_resavatar);
//    	viewHolder.resName=(TextView)convertView.findViewById(R.id.txt_resname);
    	//viewHolder.resNumber=(TextView)view.findViewById(R.id.txt)
    	 
    	convertView.setTag(viewHolder);
	}
     else{
    	 
             viewHolder=(ViewHolder)convertView.getTag();    	 
     }
     
     viewHolder.resName.setText(restaurantslist.get(position));
     viewHolder.bar.setRating(ratings.get(position));
	
	return convertView;

}

}
