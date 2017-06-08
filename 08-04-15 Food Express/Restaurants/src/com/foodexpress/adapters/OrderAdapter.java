package com.foodexpress.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foodexpress.R;

/**
 * Created by ADMIN on 3/25/2015.
 */
public class OrderAdapter extends
        ArrayAdapter<String> {
    ArrayList<String> itemsName;
    ArrayList<String> itemsQuantitie;
    ArrayList<Integer> itemsCost;
    Context context;

    public OrderAdapter(Context context, ArrayList<String> itemsName,
    ArrayList<String> itemsQuantitie,
    ArrayList<Integer> itemsCost) {
        super(context, R.layout.items, itemsName);
        this.context = context;
        this.itemsName =itemsName;
        this.itemsQuantitie=itemsQuantitie;
        this.itemsCost=itemsCost;
    }

    public static class ViewHolder {
        TextView itemName, itemCost, itemQuantity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
            convertView = layoutInflater.inflate(R.layout.items, null);
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.txt_itemName);
            viewHolder.itemQuantity = (TextView) convertView.findViewById(R.id.txt_itemQuantity);
            viewHolder.itemCost = (TextView) convertView.findViewById(R.id.txt_itemCost);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(itemsName.get(position));
        viewHolder.itemQuantity.setText(itemsQuantitie.get(position));
        viewHolder.itemCost.setText(" "+itemsCost.get(position));

        return convertView;
    }
}
