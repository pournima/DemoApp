package com.github.organisation;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.branch.BranchActivity;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationRepositoryParseResult;
import com.github.repository.RepositoryListAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class OrganisationRepositoryActivity extends Activity {

	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;

	
	String organisationRepo;
	String orgRepositoryName;
	String userName;
	
	ListView listView;
	
	AppStatus mAppStatus;
	OrganisationRepositoryDBAdapter mRepositoryDBAdapter;
	OrganisationRepositoryDataModel mRepositoryDataModel;
	
	ArrayList<OrganisationRepositoryDataModel> orgRepositoryData;
	OrganisationRepositoryListAdapter mRepositoryListAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repository_layout);
		mAppStatus = AppStatus.getInstance(this);
		userName=mAppStatus.getSharedUserName(Constants.LOGIN_USERNAME);
		
		String strJsonResponse=getIntent().getExtras().getString("strJsonResponse");
		Log.d("repo response", strJsonResponse);
		getOrganisationRepository(strJsonResponse);
	}
	
	
	
	private void getOrganisationRepository(String strJsonResponse) {
		
		mRepositoryDBAdapter = new OrganisationRepositoryDBAdapter(this,
				Constants.OrgRepositoryTableName);
		mRepositoryDataModel = new OrganisationRepositoryDataModel();
		
		mRepositoryDBAdapter.deleteAll();
		
		OrganisationRepositoryParseResult repoParse=new OrganisationRepositoryParseResult();
		
		ArrayList<OrganisationRepositoryDataModel> repoDataModel=repoParse.parseRepositoryData(strJsonResponse);

		for (OrganisationRepositoryDataModel obj : repoDataModel) 
		{
			ContentValues repositoryValues = new ContentValues();
			repositoryValues.put(mRepositoryDBAdapter.NAME,obj.getName());
			repositoryValues.put(mRepositoryDBAdapter.OWNER,obj.getOwner());
			mRepositoryDBAdapter.create(repositoryValues);
		
		}
		generateList();
		onListClick();
	}	
	

	
	private void generateList(){
		orgRepositoryData = mRepositoryDBAdapter.getRepositoryList(this);
		Log.d("Arraylist", "repository data" + orgRepositoryData);
		mRepositoryListAdapter = new OrganisationRepositoryListAdapter(OrganisationRepositoryActivity.this,
				orgRepositoryData);
		listView = (ListView) findViewById(R.id.listView);
		mRepositoryListAdapter.notifyDataSetChanged();
		listView.setAdapter(mRepositoryListAdapter);
		
	}

	
	private void onListClick(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (mAppStatus.isOnline(OrganisationRepositoryActivity.this)) {
										
					orgRepositoryName = (orgRepositoryData.get(position)).toString();
					Log.d("Organisation Repository name---", "" + orgRepositoryName);

					String repoOwner=orgRepositoryData.get(position).owner.toString();
					Log.d("Owner---", "" + repoOwner);
					
					Intent i=new Intent(getParent(), OrganisationBranchActivity.class);
					
					i.putExtra("owner", repoOwner);
					i.putExtra("reponame", orgRepositoryName);
					//startActivity(intent);
					
					GroupActivity parentActivity = (GroupActivity)getParent();
					parentActivity.startChildActivity("branch intent", i);
					
				} else {
					// dismissDialog(0);
					Log.d("Please check you internet connection", "Check");
					//showMessage("Please check you internet connection!!");

				}
			}
		});
	}
	
}
