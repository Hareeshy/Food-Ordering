package com.foodexpress;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class FinalTrain extends Activity {
	WebView wb;
	Button go;
	EditText ed;
	String formattedDate,toServer;
	String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_train);
		wb=(WebView)findViewById(R.id.webView1);
		ed= (EditText)findViewById(R.id.editText1);
		go = (Button)findViewById(R.id.button1);
		
		go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(ed.getText().toString().length()>0){
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
				 formattedDate = df.format(c.getTime());
				 toServer=sdf.format(c.getTime());
				 new getTrain().execute("http://railenquiry.in/runningstatus/"+ed.getText().toString()+"/"+toServer);
				}
				 
			}
		});

		
		 wb.setWebViewClient(new WebViewClient(){
	        	public boolean shouldOverrideUrlLoading(WebView view, String url) {  
		        	    boolean shouldOverride = false;  
//			        	    Log.v("res", " "+url);
			        	    if (url.startsWith("https://")) {
			        	    	shouldOverride = true;  
			        	    }  
			        	    return shouldOverride;
			        	}
			        	
			        });
		
	}
	
	private class getTrain extends AsyncTask<String, String, String>{

		Prograss_custom dialog=new Prograss_custom(FinalTrain.this);
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
			super.onPreExecute();
		}
		
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
		return	getTrainResponse(params[0]);
		
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			LoadHtmlinWebView(result);
			dialog.dismiss();
			super.onPostExecute(result);
		}
		
	}
	
	public static String getTrainResponse(String string) {
		// TODO Auto-generated method stub
		try {
			HttpClient client =new DefaultHttpClient();
			HttpGet get =new HttpGet(string);
			HttpResponse res =client.execute(get);
			 InputStream in = res.getEntity().getContent();
             BufferedReader reader = null;
             if(in != null) {
                 reader = new BufferedReader(new InputStreamReader(in));
             }
             StringBuffer buff = new StringBuffer();
             String line = null;
             while((line = reader.readLine()) != null) {
                 buff.append(line + "\n");
             }
             try {
                 in.close();
             } catch (Exception e) {}
             String allData =" ";
             String line2=" ";
             BufferedReader reader2 = null;
             if(buff != null) {
                 reader2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buff.toString().getBytes())));
                 while ((line2 = reader2.readLine()) != null) {
                	 allData=allData+line2;
                 }
             Document doc = Jsoup.parse(allData);
             int div_size = doc.select("DIV").size();
             for (int i = 0; i < div_size; i++) {
            	 Element div_id = doc.select("DIV").get(i);
            	 String id =div_id.attr("id");
//            	  Log.v("ID", "<<< "+id);
            	 if(id.equalsIgnoreCase("respan1")){
            		Elements ele = div_id.getElementsByTag("table");
                    return " "+ele;
            	 }
			}
             } 
            
             } catch (Exception e) {
			// TODO: handle exception
//            	 e.printStackTrace();
		}
		
		return null;
	}

	public void LoadHtmlinWebView(String result2) {
		// TODO Auto-generated method stub
		result = result2;
		result = result.replaceAll("align=\"center\"", "align=\"center\"");
		result = result.replace("border=\"1\"", "border=\"0\"");
		result = result.replaceAll("class=\"head\"", "style=\"font-weight:bold;font-size:14px\"");
		result = result.replaceAll("class=\"normal\"", "style=\"font-size:10px\"");
		
		
		try {
			int startIndex1 = result.indexOf("<a");
			int endIndex1 = result.indexOf("</a>");
			String replacement1 = "<b>Train Status</b><br>";
			String toBeReplaced1 = result.substring(startIndex1, endIndex1);
//			System.out.println(result.replace(toBeReplaced1, replacement1));
			result = result.replace(toBeReplaced1, replacement1);
		} catch (Exception e) {
			// TODO: handle exception
		}
//		System.out.println(result);

		try {
			int startIndex = result.indexOf("<a");
			int endIndex = result.indexOf("</a>");
			String replacement = "<b> </b><br>";
//			if(endIndex<0){
//				String ls= ""+endIndex;
//				ls=ls.replace("-","");
//				endIndex=Integer.parseInt(ls);
//				
//			}
			String toBeReplaced = result.substring(startIndex, endIndex);
//			System.out.println(result.replace(toBeReplaced, replacement));
			result = result.replaceAll(toBeReplaced, replacement);
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
		}
		
		
		wb.loadData(result, "text/html", "UTF-8");

	}


}
