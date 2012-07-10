package com.github.repository;

import java.util.ArrayList;
import com.github.GroupActivity;
import com.github.LoginTask;
import com.github.R;
import com.github.branch.BranchActivity;
import com.github.branch.BranchTask;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.RepositoryParserResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class RepositoryActivity extends Activity {
	
	public ProgressDialog mProgressDialog;
	Handler mhandler;
	
	String userName;
	String repoName;
	String organisationRepo;
	
	//Boolean flag=true;
	AppStatus mAppStatus;
	RepositoryDBAdapter mRepositoryDBAdapter;
	RepositoryDataModel mRepositoryDataModel;
	ListView listView;
	String PageNo;
	ArrayList<RepositoryDataModel> repositoryData;
	RepositoryListAdapter mRepositoryListAdapter;

	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.repository_layout);
					
			mAppStatus = AppStatus.getInstance(this);
			Button btnOrgMember;
			btnOrgMember=(Button) findViewById(R.id.buttonMember);
			btnOrgMember.setVisibility(View.INVISIBLE);
			
			userName=mAppStatus.getSharedUserName(Constants.LOGIN_USERNAME);
			//userName=getIntent().getExtras().getString("username");
			
//			if(Constants.gitflag.booleanValue()){
//				showDialog(0);
//				if (mAppStatus.isOnline(RepositoryActivity.this)) {
//			
//					LoginTask mLoginTask = new LoginTask(RepositoryActivity.this,userName);
//					mLoginTask.execute(userName);
//					
//					if(Constants.flagAuthonticate){
//						getRepositoryData();
//					}
//					
//				} else {
//					
//					Log.v("SPLASH_SCREEN", "You are not online!!!!");
//					// showLoading(false, "", "");
//					warningDialogBox("Please check you internet connection!!");
//				}
//			}else{
//				
//				getRepositoryData();
//				
//			}
			getRepositoryData();
			//generateList();
			//onListClick();
			
		}
	
	private void getRepositoryData(){

		mRepositoryDBAdapter = new RepositoryDBAdapter(this,
				Constants.RepositoryTableName);
		mRepositoryDataModel = new RepositoryDataModel();

		try {
			//getting all Repo Data from API into response
			showDialog(0);
			
			if (mAppStatus.isOnline(RepositoryActivity.this)) {		
				
				mRepositoryDBAdapter.deleteAll();
				
				RepositoryTask mRepositoryTask = new RepositoryTask(this, userName);
				mRepositoryTask.execute(userName);
			}
			else{
				dismissDialog(0);
				generateList();
				onListClick();
			}
				

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public void repositoryResponce(String strJsonResponse)
	{
		
		Log.i("repository Response --- ", String.valueOf(strJsonResponse));

		if(strJsonResponse.equals("[]")){
			Log.i("Responce", "Is Empty []");
		}
		else{
			RepositoryParserResult repoParse=new RepositoryParserResult();
			ArrayList<RepositoryDataModel> repoDataModel=repoParse.parseRepositoryData(strJsonResponse);

			for (RepositoryDataModel obj : repoDataModel) 
			{
				ContentValues repositoryValues = new ContentValues();
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
				
				showDialog(0);
				if (mAppStatus.isOnline(RepositoryActivity.this)) {
					
					repoName = (repositoryData.get(position)).toString();
					Log.d("Repository name---", "" + repoName);				
					
					BranchTask mBranchTask = new BranchTask(RepositoryActivity.this, userName,repoName);
					mBranchTask.execute(userName);
					
//					Intent i=new Intent(getParent(), BranchActivity.class);
//					i.putExtra("username", userName);
//					i.putExtra("reponame", repoName);
//					GroupActivity parentActivity = (GroupActivity)getParent();
//					parentActivity.startChildActivity("branch intent", i);
					
				} else {
					dismissDialog(0);
					Log.d("Please check you internet connection", "Check");
					

				}
			}
		});
		
		
		}

	public void branchResponce(String strJsonResponse){

		Log.i("branch Response --- ", String.valueOf(strJsonResponse));
		
		Intent i=new Intent(getParent(), BranchActivity.class);
		i.putExtra("branchResponse", strJsonResponse);
		i.putExtra("username", userName);
		i.putExtra("reponame", repoName);
		GroupActivity parentActivity = (GroupActivity)getParent();
		parentActivity.startChildActivity("branch intent", i);
		
	}

	/*---------- Backup button captured ----------------- */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("Backup Button", "Pressed");
			warningDialogBox();
			
		}

		return super.onKeyDown(keyCode, event);
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

	public void warningDialogBox(String warningText) {
		// TODO Auto-generated method stub

		// prepare the alert box
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

		// set the message to display
		alertbox.setMessage(warningText);

		// add a neutral button to the alert box and assign a click listener
		alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

			// click listener on the alert box
			public void onClick(DialogInterface arg0, int arg1) {
				// the button was clicked
			}
		});

		// show it
		alertbox.show();
	}
	
	private void warningDialogBox() {
		Log.i("Warning......Dialog", "ssssss");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								dialog.dismiss();
								finish();
								return;
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
