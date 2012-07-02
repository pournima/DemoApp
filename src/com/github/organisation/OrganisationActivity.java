package com.github.organisation;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationParserResult;
import com.github.helper.RepositoryParserResult;
import com.github.repository.RepositoryActivity;
import com.github.repository.RepositoryDataModel;

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
			if (mAppStatus.isOnline(OrganisationActivity.this)) {		
				showDialog(0);
				mOrganisationDBAdapter.deleteAll();
				
				OrganisationTask mOrganisationTask = new OrganisationTask(this);
				mOrganisationTask.execute();
			}
			else{
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
	
	public void organisationRepositoryResponse(String strJsonResponse){
		Log.i("organisation Repository Response --- ", String.valueOf(strJsonResponse));

		Intent i=new Intent(getParent(), OrganisationRepositoryActivity.class);
		i.putExtra("strJsonResponse", strJsonResponse);
		//startActivity(i);
		GroupActivity parentActivity = (GroupActivity)getParent();
		parentActivity.startChildActivity("orgBranch intent", i);
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

				if (mAppStatus.isOnline(OrganisationActivity.this)) {
					
					
					orgName = (organisationData.get(position)).toString();
					Log.d("Organisation name---", "" + orgName);
					
					showDialog(0);
					OrganisationRepositoryTask mOrganisationRepositoryTask = new OrganisationRepositoryTask(OrganisationActivity.this);
					mOrganisationRepositoryTask.execute(orgName);
					

				} else {
					// dismissDialog(0);
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
	
	
	
}
