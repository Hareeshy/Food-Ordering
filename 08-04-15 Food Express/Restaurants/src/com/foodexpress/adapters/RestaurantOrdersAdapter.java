package com.foodexpress.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foodexpress.R;

public class RestaurantOrdersAdapter extends ArrayAdapter<String>{

	public Context context;
	private ArrayList<String> orderIds,names,emails,phones,pnrs;

	
	public RestaurantOrdersAdapter(Context context, 
	ArrayList<String> orderIds, ArrayList<String> names,ArrayList<String> emails,ArrayList<String> phones,ArrayList<String> pnrs) {
		super(context, R.layout.order_list_item, orderIds);
		// TODO Auto-generated constructor stub
			this.context=context;
			this.orderIds=orderIds;
			this.names=names;
			this.emails=emails;
			this.phones=phones;
			this.pnrs=pnrs;
	}
	
	public class ViewHolder{
		TextView orderId,name,phNumber,pnr,email;
		
		
	}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
     ViewHolder viewHolder;
     if (convertView==null) {
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewHolder=new ViewHolder();
		convertView=inflater.inflate(R.layout.order_list_item, parent, false);
    	 
    	viewHolder.orderId=(TextView)convertView.findViewById(R.id.textView1);
    	viewHolder.name=(TextView)convertView.findViewById(R.id.textView2);
    	viewHolder.email=(TextView)convertView.findViewById(R.id.textView3);
    	viewHolder.phNumber=(TextView)convertView.findViewById(R.id.textView4);
    	viewHolder.pnr=(TextView)convertView.findViewById(R.id.textView5);
    	convertView.setTag(viewHolder);
	}
     else{
    	 
             viewHolder=(ViewHolder)convertView.getTag();    	 
     }
     
     viewHolder.orderId.setText("Order Id : "+orderIds.get(position));
     viewHolder.name.setText("Order by: "+names.get(position));
     viewHolder.email.setText("Status : "+emails.get(position));
     viewHolder.phNumber.setText("Phone : "+phones.get(position));
     viewHolder.pnr.setText("PNR : "+pnrs.get(position));
	
	return convertView;

}

}
