package com.foodexpress.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
	
	
	public static boolean isOnline(Context context){
		
	ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
	
	if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
	 {
		return true;
	}	
		
	return false;	
	}
	
}
