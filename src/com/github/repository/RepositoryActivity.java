package com.github.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.github.R;
import com.github.SplashScreen;
import com.github.branch.BranchActivity;
import com.github.commits.CommitListAdapter;
import com.github.commits.CommitsActivity;
import com.github.commits.CommitsTask;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.RepositoryParserResult;
import com.github.rest.RestClient;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class RepositoryActivity extends Activity {
	
	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;
	
	String userName;
	String repoName;
	
	Boolean flag=true;
	AppStatus mAppStatus;
	RepositoryDBAdapter mRepositoryDBAdapter;
	RepositoryDataModel mRepositoryDataModel;
	ListView listView;
	String PageNo;
	ArrayList<RepositoryDataModel> repositoryData;
	RepositoryListAdapter mRepositoryListAdapter;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.repository_layout);
					
			flag=getIntent().getExtras().getBoolean("LOGIN_FLAG");
			//tvuser.setText(user);
			
			getRepositoryData();
			//generateList();
			//onListClick();
			
		}
		
		
	private void getRepositoryData(){
		
		mAppStatus = AppStatus.getInstance(this);

		mRepositoryDBAdapter = new RepositoryDBAdapter(this,
				Constants.RepositoryTableName);
		mRepositoryDataModel = new RepositoryDataModel();

		try {
			//getting all Repo Data from API into response
			userName=getIntent().getExtras().getString("username");
			String url="users/"+userName+"/repos";
			//String pageNumber = new Integer(PageNo).toString();
							
				showDialog(0);
			
				RepositoryTask mRepositoryTask = new RepositoryTask(this, userName,url);
				mRepositoryTask.execute(userName,url);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void repositoryResponce(String strJsonResponse){
		
		
		Log.i("repository Response --- ", String.valueOf(strJsonResponse));
		
		mRepositoryDBAdapter.deleteAll();
		
		if(strJsonResponse.equals("[]")){
			Log.i("Responce", "Is Empty []");
		}
		else{
			RepositoryParserResult repoParse=new RepositoryParserResult();
			ArrayList<RepositoryDataModel> repoDataModel=repoParse.parseRepositoryData(strJsonResponse);

			for (RepositoryDataModel obj : repoDataModel) 
			{
				ContentValues repositoryValues = new ContentValues();
				repositoryValues.put(mRepositoryDBAdapter.ID, obj.getId());
				repositoryValues.put(mRepositoryDBAdapter.NAME,obj.getName());
				mRepositoryDBAdapter.create(repositoryValues);
			
			}
			generateList();
			onListClick();
		}	
	}
	
	private void generateList(){
		repositoryData = mRepositoryDBAdapter.getRepositoryList(this);
		Log.d("Arraylist", "repository data" + repositoryData);
		mRepositoryListAdapter = new RepositoryListAdapter(RepositoryActivity.this,
				repositoryData);
		listView = (ListView) findViewById(R.id.listView);
		mRepositoryListAdapter.notifyDataSetChanged();
		listView.setAdapter(mRepositoryListAdapter);
		
	}
	
	
	private void onListClick(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (mAppStatus.isOnline(RepositoryActivity.this)) {
					
					repoName = (repositoryData.get(position)).toString();
					Log.d("name---", "" + repoName);

					String url="repos/"+userName+"/"+repoName+"/commits";
					
					//String pageNumber = new Integer(PageNo).toString();
						
					Intent intent = new Intent(RepositoryActivity.this,BranchActivity.class);
					
					intent.putExtra("username", userName);
					intent.putExtra("reponame", repoName);
					startActivity(intent);
					
				} else {
					// dismissDialog(0);
					Log.d("Please check you internet connection", "Check");
					//showMessage("Please check you internet connection!!");

				}
			}
		});
		
		
		}

	
	
	void showLoading(final boolean show, final String title, final String msg) {
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				if (show) {
					if (loading != null) {
						loading.setTitle(title);
						loading.setMessage(msg);
						loading.show();
					}
				} else {
					loading.cancel();
					loading.dismiss();
				}

			}
		});
	}

	void message(String msg) {
		final String mesage = msg;
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(RepositoryActivity.this, mesage, 8000);
				toast.show();
			}
		});
	}
	
	
	// Shows progress dialog box
	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Please Wait...");
		dialog.setMessage("Loading.....");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.i("GitHub", "user cancelling authentication");

			}
		});
		mProgressDialog = dialog;
		return dialog;
	}
}
