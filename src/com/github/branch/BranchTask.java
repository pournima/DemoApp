package com.github.branch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.rest.RestClient;

public class BranchTask extends AsyncTask<String, Void, String> {

	private BranchActivity context;
	AppStatus mAppStatus;
	private String strUserName;
	private String strRepoName;
	Handler mhandler;

	
	public String getStrUserName() {
		return strUserName;
	}


	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}


	public BranchTask(BranchActivity context,String userName,String repoName)
	{
		this.context = context;
		this.strUserName=userName;
		this.strRepoName=repoName;
		mAppStatus =new AppStatus();
	}
	
	
	
	@Override
	protected String doInBackground(String... userName) {
		// TODO Auto-generated method stub
		
		String strJsonReponse = null;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);

			params.add(new BasicNameValuePair(Constants.AUTH_KEY, mAppStatus
			.getSharedStringValue(Constants.AUTH_KEY)));
			
			params.add(new BasicNameValuePair("username",userName[0]));
			params.add(new BasicNameValuePair("repository",strRepoName));
				
			
			//params.add(new BasicNameValuePair("page", "1"));
			try {	
			if (mAppStatus.isOnline(context)) 
			{

					strJsonReponse = RestClient.getInstance(context).doApiCall(Constants.strBranch, "GET", params);

				
			}
			else{
				Log.d("Please check you internet connection", "You are offline");
			}
		
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
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
			
			context.dismissDialog(0);
			
			Toast.makeText(context,"No branches for this repository",Toast.LENGTH_SHORT).show();
			
			Log.i("JSON RESPONSE::::","Data not found...!!");

		} else {
			/* RepositoryActivity' activity */
			
			context.dismissDialog(0);
			context.branchResponce(strJsonReponse);
			}
	}
}
