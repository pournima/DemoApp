package com.github.organisation;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationParserResult;
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
import android.widget.ListView;

public class OrganisationActivity extends Activity {
	
	ListView listView;
		
	Handler mhandler;
	AppStatus mAppStatus;
	OrganisationDBAdapter mOrganisationDBAdapter;
	OrganisationDataModel mOrganisationDataModel;

	ArrayList<OrganisationDataModel> organisationData;
	OrganisationListAdapter mOrganisationListAdapter;
	
	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	String orgName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organisation_layout);
		
		mAppStatus = AppStatus.getInstance(this);
		
		getOrganisationData();
		
	}

	
	private void getOrganisationData(){

		mOrganisationDBAdapter = new OrganisationDBAdapter(this,
				Constants.OrganisationName);
		mOrganisationDataModel = new OrganisationDataModel();

		try {
			//getting all Repo Data from API into response
			
			//String pageNumber = new Integer(PageNo).toString();
			showDialog(0);
			if (mAppStatus.isOnline(OrganisationActivity.this)) {		
				
				mOrganisationDBAdapter.deleteAll();
				
				OrganisationTask mOrganisationTask = new OrganisationTask(this);
				mOrganisationTask.execute();
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

	
	public void organisationResponse(String strJsonResponse){
		Log.i("organisation Response --- ", String.valueOf(strJsonResponse));

		
			OrganisationParserResult organisationParse=new OrganisationParserResult();
			ArrayList<OrganisationDataModel> orgDataModel=organisationParse.parseRepositoryData(strJsonResponse);

			for (OrganisationDataModel obj : orgDataModel) 
			{
				ContentValues organisationValues = new ContentValues();
				organisationValues.put(mOrganisationDBAdapter.ORG_NAME,obj.getOrgName());
				mOrganisationDBAdapter.create(organisationValues);
			
			}
			generateList();
			onListClick();
			
	}
	
	public void organisationRepositoryResponse(String strJsonResponse,String organisation){
		Log.i("organisation Repository Response --- ", String.valueOf(strJsonResponse));

		Intent i=new Intent(getParent(), OrganisationRepositoryActivity.class);
		i.putExtra("strJsonResponse", strJsonResponse);
		i.putExtra("organisation", organisation);
		//startActivity(i);
		GroupActivity parentActivity = (GroupActivity)getParent();
		parentActivity.startChildActivity("orgbranches intent", i);
	}
	
	private void generateList(){
		organisationData = mOrganisationDBAdapter.getOrganisationList(this);
		Log.d("Arraylist", "organisation data" + organisationData);
		mOrganisationListAdapter = new OrganisationListAdapter(OrganisationActivity.this,
				organisationData);
		listView = (ListView) findViewById(R.id.listViewOrganisation);
		mOrganisationListAdapter.notifyDataSetChanged();
		listView.setAdapter(mOrganisationListAdapter);
		
	}
	
	private void onListClick(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				showDialog(0);
				if (mAppStatus.isOnline(OrganisationActivity.this)) {
					
					
					orgName = (organisationData.get(position)).toString();
					Log.d("Organisation name---", "" + orgName);
					
					
					OrganisationRepositoryTask mOrganisationRepositoryTask = new OrganisationRepositoryTask(OrganisationActivity.this,orgName);
					mOrganisationRepositoryTask.execute(orgName);
					

				} else {
					dismissDialog(0);
					Log.d("Please check you internet connection", "Check");
					//showMessage("Please check you internet connection!!");

				}
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("Backup Button", "Pressed");
			warningDialogBox();
			
		}

		return super.onKeyDown(keyCode, event);
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
