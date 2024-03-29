package com.github.members;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.branch.BranchActivity;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationMemberParseResult;
import com.github.repository.RepositoryActivity;
import com.github.repository.RepositoryDBAdapter;
import com.github.repository.RepositoryDataModel;
import com.github.repository.RepositoryListAdapter;

import android.R.string;
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


public class OrganisationMemberActivity extends Activity {
	
	public ProgressDialog mProgressDialog;
	Handler mhandler;
	
	String organisation;
	ListView listView;
	AppStatus mAppStatus;
	String strMember;
	String strMemberResponse;
	
	ArrayList<RepositoryDataModel> memberData;
	RepositoryDBAdapter mRepositoryDBAdapter;
	RepositoryDataModel mRepositoryDataModel;
	RepositoryListAdapter mMemberListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organisation_member_layout);
		
		mAppStatus = AppStatus.getInstance(this);
		
		organisation=getIntent().getExtras().getString("organisation");
		getMembers(organisation);
		
	}
	
	private void getMembers(String strOrganisation){
		
//		showDialog(0);
//		if (mAppStatus.isOnline(OrganisationMemberActivity.this)) {	
//			OrganisationMemberTask orgMemberTask=new OrganisationMemberTask(OrganisationMemberActivity.this,strOrganisation);
//			orgMemberTask.execute(strOrganisation);
//		
//		}
		strMemberResponse=getIntent().getExtras().getString("memberResponse");
		mRepositoryDBAdapter = new RepositoryDBAdapter(this,
				Constants.MembersTableName);
		mRepositoryDataModel = new RepositoryDataModel();
		mRepositoryDBAdapter.deleteAll();
		
		memberResponse(strMemberResponse);
		
	}
	
	
	public void memberResponse(String strJsonResponse){
		
		OrganisationMemberParseResult repoParse=new OrganisationMemberParseResult();
		ArrayList<RepositoryDataModel> memberDataModel=repoParse.parseRepositoryData(strJsonResponse);

		for (RepositoryDataModel obj : memberDataModel) 
		{
			ContentValues repositoryValues = new ContentValues();
			repositoryValues.put(mRepositoryDBAdapter.NAME,obj.getName());
			mRepositoryDBAdapter.create(repositoryValues);
		
		}
		generateList();
		onListClick();
	}
	
	private void generateList(){
		memberData = mRepositoryDBAdapter.getRepositoryList(this);
		Log.d("Arraylist", "repository data" + memberData);
		mMemberListAdapter = new RepositoryListAdapter(OrganisationMemberActivity.this,
				memberData);
		listView = (ListView) findViewById(R.id.listViewOrganisationMember);
		mMemberListAdapter.notifyDataSetChanged();
		listView.setAdapter(mMemberListAdapter);
		
	}
	
	
	private void onListClick(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (mAppStatus.isOnline(OrganisationMemberActivity.this)) {
					
					strMember = (memberData.get(position)).toString();
					Log.d("Member name---", "" + strMember);
					
					
					
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
