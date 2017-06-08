package com.foodexpress;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class PNRStatus extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		new getPnr().execute(getIntent().getStringExtra("pnr_number"));
	}
	
	class getPnr extends AsyncTask<String, String, JSONObject>{
		Prograss_custom dialog=new Prograss_custom(PNRStatus.this);
		String formattedDate;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setCancelable(false);
			dialog.show();
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);
			 formattedDate = df.format(c.getTime());

			super.onPreExecute();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
		
			JSONObject res ;
		String captcha = "37819";
		String pnr1 = params[0];
		String reqStr = "lccp_pnrno1=" + pnr1 + "&lccp_cap_val=" + captcha + "&lccp_capinp_val=" + captcha + "&submitpnr=Get+Status";
//		Log.v("PNR", params[0]);
		try {
	 res =  getPNRResponse(reqStr, "http://www.indianrail.gov.in/cgi_bin/inet_pnstat_cgi_10521.cgi");
	 System.out.println("<<  <<  "+res);
	 return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		
		}
			
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			try {
				dialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(result==null){
//				pnr_status.setError("Try Again");
			Toast.makeText(getApplicationContext(), "Please try again after sometime", Toast.LENGTH_SHORT).show();
			}else{
				try {
					int ll =result.getJSONArray("passengers").length();
					if(ll>0){
						Intent i =new Intent(getApplicationContext(),FinalPNR.class);
						i.putExtra("result", result.toString());
						startActivity(i);
					}else{
						Toast.makeText(getApplicationContext(), "Invalid PNR", Toast.LENGTH_LONG).show();
//						pnr_status.setError("INVALID PNR");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "Invalid PNR", Toast.LENGTH_LONG).show();
//					pnr_status.setError("INVALID PNR");
				}
				finish();
			}
			super.onPostExecute(result);

		}
	}

	public static JSONObject getPNRResponse(String reqStr, String urlAddr) throws Exception {
        String urlHost = null;
        int port;
        String method = null;
        try {
            URL url = new URL(urlAddr);
            urlHost = url.getHost();
            port = url.getPort();
            method = url.getFile();

            // validate port
            if(port == -1) {
                port = url.getDefaultPort();
            }
        } catch(Exception e) {
//            e.printStackTrace();
            throw new Exception(e);
        }
//        Log.v("message", "message");
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
        HttpProtocolParams.setUseExpectContinue(params, true);

        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        // Required protocol interceptors
        httpproc.addInterceptor(new RequestContent());
        httpproc.addInterceptor(new RequestTargetHost());
        // Recommended protocol interceptors
        httpproc.addInterceptor(new RequestConnControl());
        httpproc.addInterceptor(new RequestUserAgent());
        httpproc.addInterceptor(new RequestExpectContinue());

        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
        HttpContext context = new BasicHttpContext(null);
        HttpHost host = new HttpHost(urlHost, port);
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();

        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
        @SuppressWarnings("unused")
        String resData = null;
        @SuppressWarnings("unused")
        String statusStr = null;
        StringBuffer buff = new StringBuffer();
        try {
            String REQ_METHOD = method;
            String[] targets = { REQ_METHOD };

            for (int i = 0; i < targets.length; i++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, params);
                }
                BasicHttpEntityEnclosingRequest req = new BasicHttpEntityEnclosingRequest("POST", targets[i]);
                req.setEntity(new InputStreamEntity(new ByteArrayInputStream(reqStr.toString().getBytes()), reqStr.length()));
                req.setHeader("Content-Type", "application/x-www-form-urlencoded");
                req.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
                req.setHeader("Cache-Control", "max-age=0");
                req.setHeader("Connection", "keep-alive");
                req.setHeader("Origin", "http://www.indianrail.gov.in");
                req.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                req.setHeader("Referer", "http://www.indianrail.gov.in/pnr_Enq.html");
                req.setHeader("Accept-Language", "en-US,en;q=0.8");
                req.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");


                httpexecutor.preProcess(req, httpproc, context);

                HttpResponse response = httpexecutor.execute(req, conn, context);
                response.setParams(params);
                httpexecutor.postProcess(response, httpproc, context);

                org.apache.http.Header[] headers = response.getAllHeaders();
                for(int j=0; j<headers.length; j++) {
                    if(headers[j].getName().equalsIgnoreCase("ERROR_MSG")) {
                        resData = EntityUtils.toString(response.getEntity());
                    } 
                }
                statusStr = response.getStatusLine().toString();
                InputStream in = response.getEntity().getContent();
                BufferedReader reader = null;
                if(in != null) {
                    reader = new BufferedReader(new InputStreamReader(in));
                }

                String line = null;
                while((line = reader.readLine()) != null) {
                    buff.append(line + "\n");
                }
                try {
                    in.close();
                } catch (Exception e) {}
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
//        Log.v("MSG", " "+buff);
        JSONObject response;
        try {
             response =parseFromServer(buff);
             System.out.print(response);
//            Log.v("kill", response.toString());
		} catch (Exception e) {
			// TODO: handle exception
			response=new JSONObject();
		}
//        ArrayList<ArrayList<String>> res = parseHtml(buff);
       
       
      
       return response;
    }
	 private static JSONObject parseFromServer(StringBuffer buff) throws Exception {
			// TODO Auto-generated method stub

	    	HttpClient client =new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost("http://www.ixigo.com/train/pnr_getstatus?schedule=true");
	    	 httppost.addHeader("Content-Type","text/xml");  
	         httppost.addHeader("Host","www.ixigo.com");
	         httppost.addHeader("Connection","Keep-Alive");
	         httppost.addHeader("User-Agent","android-async-http/1.4.1 (http://loopj.com/android-async-http)");
	         httppost.addHeader("Accept-Encoding","gzip");

	         BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buff.toString().getBytes())));
	         StringBuilder sb = new StringBuilder();
	         String line = null;
	         while ((line = reader.readLine()) != null) 
	         {
	             sb.append(line + "\n");
	         }
	         try {
	           String  result = sb.toString();
	           
	           StringEntity se = new StringEntity(result,HTTP.UTF_8);
	           se.setContentType("text/xml");
	           httppost.setEntity(se);
	           HttpResponse response = client.execute(httppost);
	          
	           InputStream is = response.getEntity().getContent();
	           Header contentEncoding = response.getFirstHeader("Content-Encoding");
	           if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
	               is = new GZIPInputStream(is);
	           }
	           
	           
	           BufferedReader reader2 = new BufferedReader(new InputStreamReader(is));
	           StringBuilder sb2 = new StringBuilder();

	           String line2 = null;
	           try {
	               while ((line2 = reader2.readLine()) != null) {
	                   sb2.append((line2 + "\n"));
	               }
	           } catch (IOException e) {
//	               e.printStackTrace();
	           } finally {
	               try {
	                   is.close();
	               } catch (IOException e) {
//	                   e.printStackTrace();
	               }
	           }
	   		return new JSONObject(sb2.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
		}
	         
//	         aaaaaaa
			return null;
	    	
		}


}
