package com.github.organisation;

import java.io.IOException;
import java.net.URLEncoder;
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

public class OrganisationUserCommitsTask extends AsyncTask<String, Void, String> {

	private OrganisationSearchCommitActivity context;
	AppStatus mAppStatus;
	private String strBranchName;
	private String strUserName;
	private String strRepoName;
	private String strCommitter_name;
	private String strDate;
	Handler mhandler;


	public String getStrBranchName() {
		return strBranchName;
	}



	public void setStrBranchName(String strBranchName) {
		this.strBranchName = strBranchName;
	}


	public OrganisationUserCommitsTask(OrganisationSearchCommitActivity context,String branchName,String userName,String repoName,String commiterName,String date)
	{
		this.context = context;
		this.strBranchName=branchName;
		this.strUserName=userName;
		this.strRepoName=repoName;
		this.strCommitter_name=commiterName;
		this.strDate=date;
		mAppStatus =new AppStatus();
	}
	
	
	
	@Override
	protected String doInBackground(String... branchName) {
		// TODO Auto-generated method stub
		
		String strJsonReponse = null;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			
			params.add(new BasicNameValuePair(Constants.AUTH_KEY, mAppStatus
			.getSharedStringValue(Constants.AUTH_KEY)));
				
				params.add(new BasicNameValuePair("owner", strUserName));
				params.add(new BasicNameValuePair("repository", strRepoName));
				params.add(new BasicNameValuePair(Constants.BRANCH, strBranchName));
				params.add(new BasicNameValuePair("committer_name", URLEncoder.encode(strCommitter_name)));
				params.add(new BasicNameValuePair("date", strDate));		
				//URLEncoder.encode(strCommitter_name);
				try {	
				if (mAppStatus.isOnline(context)) 
				{
					
					strJsonReponse = RestClient.getInstance(context).doApiCall(Constants.strOrganisationUserCommits, "GET", params);
				}
				else{
					Log.d("Please check you internet connection", "You are offline");
				}		

			} catch (IOException e) {
				e.printStackTrace();
			}

		return strJsonReponse;
	
	}

	
	@Override
	protected void onPostExecute(String strJsonReponse) {
		
		Log.i("STRJSON RESPONSE::::", String.valueOf(strJsonReponse));

		if (strJsonReponse.equals("[]")) {
			
			context.dismissDialog(0);
			context.btnOk.setEnabled(true);
			Toast.makeText(context,"No commits for this user",Toast.LENGTH_SHORT).show();
			
			Log.i("JSON RESPONSE::::","Data not found...!!");

		} else {
			/* commits user Activity' activity */
			
			context.dismissDialog(0);
			context.commitsResponce(strJsonReponse);
			}
	}
}
