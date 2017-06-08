package com.foodexpress.dialogs;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.foodexpress.R;

public class MyDialogs {

	Dialog progressDialog;
	public   void getDialog(String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
               .setCancelable(false)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               });
      builder.show();
    }
public boolean validation(List<EditText> asList) {
	// TODO Auto-generated method stub

	for (EditText editText :asList) {
		
		if (editText.getText().toString().length()==0) {
			
			return false;
		}
		
	}
	return true;
}
public void showProgressDialog(Activity activity){
	
	progressDialog = new Dialog(activity);
	progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
     progressDialog.setContentView(R.layout.loading);
     progressDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
             RelativeLayout.LayoutParams.MATCH_PARENT);
     progressDialog.setCanceledOnTouchOutside(false);
     progressDialog.setCancelable(false);
     progressDialog.onBackPressed();
     progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
     try {
    	 progressDialog.show();
     } catch (Exception e) {
         e.printStackTrace();
     }
	
}

public void cancelProgressDialog(){
	
	if (progressDialog.isShowing()) {
		progressDialog.cancel();
	}
	
}

}
