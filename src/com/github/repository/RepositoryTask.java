package com.github.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.rest.RestClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


public class RepositoryTask extends AsyncTask<String, Void, String> {

	private RepositoryActivity context;
	AppStatus mAppStatus;
	private String strUserName;

	Handler mhandler;

	
	public String getStrUserName() {
		return strUserName;
	}


	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}


	public RepositoryTask(RepositoryActivity context,String userName)
	{
		this.context = context;
		this.strUserName=userName;
		mAppStatus =new AppStatus();
	}
	
	
	
	@Override
	protected String doInBackground(String... userName) {
		// TODO Auto-generated method stub
		
		String strJsonReponse = null;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			
			params.add(new BasicNameValuePair(Constants.AUTH_KEY, mAppStatus
					.getSharedStringValue(Constants.AUTH_KEY)));
			
			//params.add(new BasicNameValuePair("page", "1"));
			params.add(new BasicNameValuePair("username", userName[0]));
			try {	
			if (mAppStatus.isOnline(context)) 
			{ 
				strJsonReponse = RestClient.getInstance(context).doApiCall(Constants.strRepository, "GET", params);
			}
			else{
				Log.d("Please check you internet connection", "You are offline");
			}
				
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strJsonReponse;
	
	}

	
	@Override
	protected void onPostExecute(String strJsonReponse) {
		
		Log.i("STRJSON RESPONSE::::", String.valueOf(strJsonReponse));

		if (strJsonReponse.equals("[]")) {
			
			Log.i("JSON RESPONSE::::","Data not found...!!");
			Toast.makeText(context,"No repository",Toast.LENGTH_SHORT).show();

		} else {
			/* RepositoryActivity' activity */
			context.dismissDialog(0);
			context.repositoryResponce(strJsonReponse);
			}
	}
}
