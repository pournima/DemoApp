package com.github.commits;

import java.util.ArrayList;

import com.github.R;
import com.github.database.DbAdapter;
import com.github.helper.AppStatus;
import com.github.helper.CommitsParserResult;
import com.github.helper.Constants;
import com.github.helper.RepositoryParserResult;
import com.github.repository.RepositoryActivity;
import com.github.repository.RepositoryDBAdapter;
import com.github.repository.RepositoryDataModel;
import com.github.repository.RepositoryListAdapter;
import com.github.repository.RepositoryTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class CommitsActivity extends Activity {
	
	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;
	
	String userName;
	String branchName;
	String repoName;
	
	Boolean flag=true;
	AppStatus mAppStatus;
	CommitsDBAdapter mCommitsDBAdapter;
	CommitsDataModel mCommitsDataModel;
	ListView listView;
	String PageNo;
	ArrayList<CommitsDataModel> commitsData;
	CommitListAdapter mCommitListAdapter;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.commit_layout);

			//tvuser.setText(user);
			
			getCommitData();
			//generateList();

		}
		
		
	private void getCommitData(){
		
		mAppStatus = AppStatus.getInstance(this);

		mCommitsDBAdapter = new CommitsDBAdapter(this,
				Constants.CommitsTableName);
		mCommitsDataModel = new CommitsDataModel();

		try {
			//getting all commits Data from API into response
			userName=getIntent().getExtras().getString("username");
			branchName=getIntent().getExtras().getString("branchname");
			repoName=getIntent().getExtras().getString("reponame");
			
			String url="repos/"+userName+"/"+repoName+"/commits";
				
			
			//String pageNumber = new Integer(PageNo).toString();
			
			
			
				showDialog(0);
				mCommitsDBAdapter.deleteAll();
			
				CommitsTask mCommitsTask = new CommitsTask(this, branchName,url);
				mCommitsTask.execute(branchName,url);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void commitsResponce(String strJsonResponse){

		Log.i("commits Response --- ", String.valueOf(strJsonResponse));

		if(strJsonResponse.equals("[]")){
			Log.i("Responce", "Is Empty []");
		}
		else{
			CommitsParserResult commitsParse=new CommitsParserResult();
			ArrayList<CommitsDataModel> commitDataModel=commitsParse.parseCommitsData(strJsonResponse);
			
			for (CommitsDataModel obj : commitDataModel) 
			{
				ContentValues commitsValues = new ContentValues();
				
				commitsValues.put(mCommitsDBAdapter.NAME,obj.getName());
				commitsValues.put(mCommitsDBAdapter.DATE, obj.getDate());
				commitsValues.put(mCommitsDBAdapter.MESSAGE, obj.getMessage());
				mCommitsDBAdapter.create(commitsValues);
			
			}
			dismissDialog(0);
			generateList();
		}	
	}
	
	private void generateList(){
		commitsData = mCommitsDBAdapter.getCommitsList(this);
		Log.d("Arraylist", "commits data" + commitsData);
		mCommitListAdapter = new CommitListAdapter(CommitsActivity.this,
				commitsData);
		listView = (ListView) findViewById(R.id.listViewCommit);
		mCommitListAdapter.notifyDataSetChanged();
		listView.setAdapter(mCommitListAdapter);
	
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
				Toast toast = Toast.makeText(CommitsActivity.this, mesage, 8000);
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