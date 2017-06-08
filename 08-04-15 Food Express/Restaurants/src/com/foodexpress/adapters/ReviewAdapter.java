package com.foodexpress.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.foodexpress.R;

public class ReviewAdapter extends ArrayAdapter<String>{
	ArrayList<String> review;
	ArrayList<Integer> rating;
	Context context;

	public ReviewAdapter(Context context,  ArrayList<String> review, ArrayList<Integer> rating) {
		super(context, R.layout.list_item,review);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.review = review;
		this.rating = rating;
		
		
	}
	
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		// TODO Auto-generated method stub
	 final ViewHolder viewHolder;
		 
		 if(rowView==null){
			 viewHolder=new ViewHolder();
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				 rowView= inflater.inflate(R.layout.list_item, null, true);
				 viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.text_heading);
				 viewHolder.bar = (RatingBar) rowView.findViewById(R.id.ratingBar1);
				 rowView.setTag(viewHolder);
		 }else{
			 viewHolder = (ViewHolder) rowView.getTag();
		 }
		 viewHolder.txtTitle.setText(review.get(position));
		 viewHolder.bar.setRating(rating.get(position));

	return rowView;
	}
	class ViewHolder {
		TextView txtTitle;
		RatingBar bar;
	}
	

}
