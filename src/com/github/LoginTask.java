package com.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.organisation.GroupOrganisationRepositoryActivity;
import com.github.repository.GroupRepositoryActivity;
import com.github.repository.RepositoryActivity;
import com.github.rest.RestClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Void, String> {
	Context context;
	LoginInActivity loginContext;
	AppStatus mAppStatus;
	String strUserName;

	
	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	
	public LoginTask(Context context,String userName) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.strUserName=userName;
		mAppStatus = new AppStatus();

	}

	@Override
	protected String doInBackground(String... userName) {
		// TODO Auto-generated method stub
		// showLoading(true, "Loading", "In Process please wait...");
		String strJsonReponse = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("username", userName[0]));
			
			strJsonReponse = RestClient.getInstance(context).doApiCall(
					Constants.strLoginUserName, "GET", params);
			Log.i("Authenticate UserName Response:....",
					String.valueOf(strJsonReponse));
		
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

		try {
			JSONObject mJsonObject = new JSONObject(strJsonReponse);
			Log.i("Status.........", String.valueOf(strJsonReponse));
			String strStatus = mJsonObject.getString("success");
			Log.i("Status.........", String.valueOf(strStatus));
			if (strStatus.equals("true")) 
			{
				String strAuthToken = mJsonObject.getString("auth_token");
				Log.i("Auth Token:.....", strAuthToken);

				String strUser = mJsonObject.getString("username");
				Log.i("User:.....", strUser);
				String strUserLoginName = mJsonObject.getString("username");
				Log.i("UserLoginName:.....", strUserLoginName);
				
				/* --------------------START--------------------- */
				/* Stored AUTH Token into shared preferences */
				mAppStatus = AppStatus.getInstance(context);
				boolean bResponse = mAppStatus.saveAuthKey(Constants.AUTH_KEY,
						strAuthToken);
				
				mAppStatus.saveAuthKey(Constants.LOGIN_USERNAME,strUserLoginName);
				
				if (bResponse == true)
					Log.i("AUTH TOKEN", "save successfully....!!");
				else
					Log.i("AUTH TOKEN", "NOT save ......!!");
				/*---------------------END------------------------- */
				Constants.flagAuthonticate=true;
				Constants.gitflag=false;
				
				//loginContext.userAuthenticate(strUserLoginName);
				Intent intent = new Intent(context, GroupOrganisationRepositoryActivity.class); 
				intent.putExtra("username",strUserLoginName);
				context.startActivity(intent);
				((Activity) context).finish();

			} else {
				Log.i("Status", "User should authenticate first redirect to GITHUB.com");
				Constants.flagAuthonticate=false;
				Toast.makeText(context, "User should authenticate first", Toast.LENGTH_SHORT).show();
				//context.warningDialogBox("Please login User !");
				((Activity) context).dismissDialog(0);  //To disable progress bar
				
				//loginContext.userNotAuthenticate(strUserName);			
				//Web View --- Github.com
					Intent intentWeb = new Intent(context, GitHubAppActivity.class);
					intentWeb.putExtra("username",strUserName);
					context.startActivity(intentWeb);
					((Activity) context).finish();

				//return;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
