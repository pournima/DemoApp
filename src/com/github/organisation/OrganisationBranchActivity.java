package com.github.organisation;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.branch.BranchDBAdapter;
import com.github.branch.BranchDataModel;
import com.github.branch.BranchListAdapter;
import com.github.commits.CommitsActivity;
import com.github.helper.AppStatus;
import com.github.helper.BranchParserResult;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class OrganisationBranchActivity extends Activity {

	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;
	
	AppStatus mAppStatus;
	BranchDBAdapter mBranchDBAdapter; 
	BranchDataModel mBranchDataModel;
	ListView listView;
	
	ArrayList<BranchDataModel> branchData;
	BranchListAdapter mBranchListAdapter;
	
	String owner;
	String branchName;
	String repoName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.branch_layout);
		
		getBranchData();
		
	}

	public void getBranchData(){
		
		mAppStatus = AppStatus.getInstance(this);

		mBranchDBAdapter = new BranchDBAdapter(this,
				Constants.BranchTableName);
		mBranchDataModel = new BranchDataModel();

		try {
			//getting all branches Data from API into response
			//userName=mAppStatus.getSharedUserName(Constants.LOGIN_USERNAME);
			owner=getIntent().getExtras().getString("owner");
			repoName=getIntent().getExtras().getString("reponame");
			
			//String pageNumber = new Integer(PageNo).toString();
			if (mAppStatus.isOnline(OrganisationBranchActivity.this)) {		
				showDialog(0);
				
				mBranchDBAdapter.deleteAll();
			
				OrganisationBranchTask mBranchTask = new OrganisationBranchTask(this, owner,repoName);
				mBranchTask.execute(owner);
			}else{
				generateList();
				onListClick();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void branchResponce(String strJsonResponse){

		Log.i("branch Response --- ", String.valueOf(strJsonResponse));

		if(strJsonResponse.equals("[]")){
			Log.i("Responce", "Is Empty []");
		}
		else{
	
			BranchParserResult branchParse=new BranchParserResult();
			ArrayList<BranchDataModel> branchDataModel=branchParse.parseBranchData(strJsonResponse);
			
			for (BranchDataModel obj : branchDataModel) 
			{
				ContentValues branchValues = new ContentValues();
				branchValues.put(mBranchDBAdapter.NAME,obj.getBranchName());
				mBranchDBAdapter.create(branchValues);
			
			}
			dismissDialog(0);
			generateList();
			onListClick();
		}	
	}
	
	private void generateList(){
		branchData = mBranchDBAdapter.getBranchList(this);
		Log.d("Arraylist", "branch data" + branchData);
		mBranchListAdapter = new BranchListAdapter(OrganisationBranchActivity.this,
				branchData);
		listView = (ListView) findViewById(R.id.listViewBranch);
		mBranchListAdapter.notifyDataSetChanged();
		listView.setAdapter(mBranchListAdapter);
		
	}
	
	private void onListClick(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (mAppStatus.isOnline(OrganisationBranchActivity.this)) {
					
					branchName = (branchData.get(position)).toString();
					Log.d("branch name---", "" + branchName);
				
					//String pageNumber = new Integer(PageNo).toString();
					Constants.flagCommit=false;
					Intent intent = new Intent(getParent(),CommitsActivity.class);
					
					intent.putExtra("owner", owner);
					intent.putExtra("reponame", repoName);
					intent.putExtra("branchname",branchName);
					//startActivity(intent);
					
					GroupActivity parentActivity = (GroupActivity)getParent();
					parentActivity.startChildActivity("branch intent", intent);
					
					
				} else {
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
				Toast toast = Toast.makeText(OrganisationBranchActivity.this, mesage, 8000);
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
