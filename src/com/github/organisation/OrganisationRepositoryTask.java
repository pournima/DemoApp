package com.github.organisation;

import java.io.IOException;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.rest.RestClient;

public class OrganisationRepositoryTask extends AsyncTask<String, Void, String> {

	private OrganisationActivity context;
	AppStatus mAppStatus;
	String strOwner;
	Handler mhandler;

	public OrganisationRepositoryTask(OrganisationActivity context,String orgName)
	{
		this.context = context;
		this.strOwner=orgName;
		mAppStatus =new AppStatus();
	}
	
	
	
	@Override
	protected String doInBackground(String... orgName) {
		// TODO Auto-generated method stub
		
		String strJsonReponse = null;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			
			params.add(new BasicNameValuePair(Constants.AUTH_KEY, mAppStatus
					.getSharedStringValue(Constants.AUTH_KEY)));
			params.add(new BasicNameValuePair("organization",orgName[0]));
			
			try {	
			if (mAppStatus.isOnline(context)) 
			{ 
				strJsonReponse = RestClient.getInstance(context).doApiCall(Constants.strOrganisationRepository, "GET", params);
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
			Toast.makeText(context,"No organisation Repository for this user",Toast.LENGTH_SHORT).show();

		} else {
			/* RepositoryActivity' activity */
			context.dismissDialog(0);
			context.organisationRepositoryResponse(strJsonReponse,strOwner);
			}
	}
}
