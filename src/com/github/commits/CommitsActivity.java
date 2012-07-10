package com.github.commits;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.LoginInActivity;
import com.github.R;
import com.github.branch.BranchActivity;
import com.github.helper.AppStatus;
import com.github.helper.CommitsParserResult;
import com.github.helper.Constants;
import com.github.organisation.OrganisationSearchCommitActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CommitsActivity extends Activity {
	
	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;
	
	String userName;
	String owner;
	String branchName;
	String repoName;
	String Response;
	String commitsResponce;
	Button btnSearch;
	
	Boolean flag=true;
	AppStatus mAppStatus;
	CommitsDBAdapter mCommitsDBAdapter;
	CommitsDataModel mCommitsDataModel;
	ListView listView;
	String PageNo;
	ArrayList<CommitsDataModel> commitsData;
	CommitListAdapter mCommitListAdapter;
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.commit_layout);

			getCommitData();

			btnSearch=(Button) findViewById(R.id.buttonSearch);
			
			btnSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				Intent i=new Intent(getParent(), OrganisationSearchCommitActivity.class);
				i.putExtra("response", Response);
				i.putExtra("owner", owner);
				i.putExtra("reponame", repoName);
				i.putExtra("branchname",branchName);
				
				GroupActivity parentActivity = (GroupActivity)getParent();
				parentActivity.startChildActivity("orgSearchCommit intent", i);

				}
			});
			
		}
		
		
	private void getCommitData(){
		
		mAppStatus = AppStatus.getInstance(this);

		mCommitsDBAdapter = new CommitsDBAdapter(this,
				Constants.CommitsTableName);
		mCommitsDataModel = new CommitsDataModel();

//		try {
	
			//getting all commits Data from API into response
			commitsResponce=getIntent().getExtras().getString("commitsResponce");
			userName=getIntent().getExtras().getString("username");
			branchName=getIntent().getExtras().getString("branchname");
			repoName=getIntent().getExtras().getString("reponame");

//			if (mAppStatus.isOnline(CommitsActivity.this)) {	
//				showDialog(0);				
				
				mCommitsDBAdapter.deleteAll();
				commitsResponce(commitsResponce);
//				CommitsTask mCommitsTask = new CommitsTask(this, branchName,userName,repoName);
//				mCommitsTask.execute(branchName);
				
//			}else{
//				generateList();
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	
	public void commitsResponce(String strJsonResponse){

		Log.i("commits Response --- ", String.valueOf(strJsonResponse));
		
		Response=strJsonResponse;
		
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
			//dismissDialog(0);
			generateList();
	
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
