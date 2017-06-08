package com.foodexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RestaurantSelectedItems extends Activity{
	ListView lv;
	Button back,next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
setContentView(R.layout.res_menuitems);
		
		back = (Button)findViewById(R.id.bt_back);
		next = (Button)findViewById(R.id.bt_next);
		lv = (ListView)findViewById(R.id.listView1);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		next.setVisibility(View.GONE);
		
		
		ArrayAdapter<String> adap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,Restaurent_Items.selectedItems);
		lv.setAdapter(adap);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String image = Restaurent_Items.images.get(pos);
				Intent i = 	new Intent(getApplicationContext(),RestaurantItemDetails.class);
				i.putExtra("value", Restaurent_Items.selectedItems.get(pos));
				i.putExtra("image", image);
				i.putExtra("item_id", Restaurent_Items.item_ids.get(pos));
				startActivity(i);
			}
		});
		
	}

}
