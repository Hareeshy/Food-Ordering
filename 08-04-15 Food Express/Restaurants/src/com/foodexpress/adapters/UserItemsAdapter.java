package com.foodexpress.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.foodexpress.R;

/**
 * Created by ADMIN on 3/25/2015.
 */
public class UserItemsAdapter extends
        ArrayAdapter<String> {
    ArrayList<String> itemsName;
    ArrayList<Integer> itemsCost;
    public static ArrayList<String> selectedName;
    public static ArrayList<Integer> selectedCost;

    Context context;

    public UserItemsAdapter(Context context, ArrayList<String> itemsName,
    ArrayList<Integer> itemsCost) {
        super(context, R.layout.user_items, itemsName);
        this.context = context;
        this.itemsName =itemsName;
        this.itemsCost=itemsCost;
        selectedCost = new ArrayList<Integer>();
        selectedName = new ArrayList<String>();
    }

    public static class ViewHolder {
        TextView itemName, itemCost;
        CheckBox chkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
            convertView = layoutInflater.inflate(R.layout.user_items, null);
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.txt_itemName);
            viewHolder.chkBox = (CheckBox) convertView.findViewById(R.id.chkbox);
            viewHolder.itemCost = (TextView) convertView.findViewById(R.id.txt_itemCost);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(itemsName.get(position));
        viewHolder.itemCost.setText(" "+itemsCost.get(position));
        
        viewHolder.chkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean chk) {
				// TODO Auto-generated method stub
				if(chk){
					if(!selectedName.contains(itemsName.get(position))){
						selectedName.add(itemsName.get(position));
						selectedCost.add(itemsCost.get(position));
					}

					
				}else 
					if(selectedName.contains(itemsName.get(position))){
						int pos = selectedName.indexOf(itemsName.get(position));
						selectedName.remove(pos);
						selectedCost.remove(pos);
					}
				}
		});

        return convertView;
    }
}
