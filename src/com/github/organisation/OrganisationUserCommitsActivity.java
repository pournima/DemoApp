package com.github.organisation;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.commits.CommitListAdapter;
import com.github.commits.CommitsDBAdapter;
import com.github.commits.CommitsDataModel;
import com.github.helper.AppStatus;
import com.github.helper.CommitsParserResult;
import com.github.helper.Constants;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrganisationUserCommitsActivity extends Activity {

	public ProgressDialog mProgressDialog;
	Handler mhandler;
	
	String userName;
	String owner;
	String branchName;
	String repoName;
	String committer_name;
	String date;
	String commitResponse;
	
	Boolean flag=true;
	AppStatus mAppStatus;
	CommitsDBAdapter mCommitsDBAdapter;
	CommitsDataModel mCommitsDataModel;
	ListView listView;
	String PageNo;
	ArrayList<CommitsDataModel> commitsData;
	CommitListAdapter mCommitListAdapter;
	
	String Response;
	Button btnSearch;
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.commit_layout);
			
			btnSearch=(Button) findViewById(R.id.buttonSearch);
			
			if(Constants.flagUserCommit){
				btnSearch.setVisibility(View.INVISIBLE);
			}
			else{
				btnSearch.setVisibility(View.VISIBLE);
			}
			getCommitData();
			
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
				parentActivity.startChildActivity("orgUserCommit intent", i);

				}
			});
			
		}
		
		
		
	private void getCommitData(){
		
		mAppStatus = AppStatus.getInstance(this);

		mCommitsDBAdapter = new CommitsDBAdapter(this,
				Constants.CommitsTableName);
		mCommitsDataModel = new CommitsDataModel();

		try {
			//Commit by search
				commitResponse=getIntent().getExtras().getString("commitResponse");
				committer_name=getIntent().getExtras().getString("committer_name");
				date=getIntent().getExtras().getString("date");
				owner=getIntent().getExtras().getString("owner");
				branchName=getIntent().getExtras().getString("branchname");
				repoName=getIntent().getExtras().getString("reponame");
				
				commitsResponce(commitResponse);
//				showDialog(0);
//				if (mAppStatus.isOnline(OrganisationUserCommitsActivity.this)) {	
//					
//					OrganisationUserCommitsTask mCommitsTask = new OrganisationUserCommitsTask(this, branchName,owner,repoName,committer_name,date);
//					mCommitsTask.execute(branchName);
//					
//				}else{
//					dismissDialog(0);
//					generateList();
//				}
	
			
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
			mCommitsDBAdapter.deleteAll();	
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
	}
	
	private void generateList(){
		commitsData = mCommitsDBAdapter.getCommitsList(this);
		Log.d("Arraylist", "commits data" + commitsData);
		mCommitListAdapter = new CommitListAdapter(OrganisationUserCommitsActivity.this,
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
