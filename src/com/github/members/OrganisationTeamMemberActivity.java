package com.github.members;

import java.util.ArrayList;

import com.github.R;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationMemberParseResult;
import com.github.repository.RepositoryDBAdapter;
import com.github.repository.RepositoryDataModel;
import com.github.repository.RepositoryListAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class OrganisationTeamMemberActivity extends Activity {

	public ProgressDialog mProgressDialog;
	Handler mhandler;
	
	String organisation;
	ListView listView;
	AppStatus mAppStatus;
	String strMember;
	ArrayList<RepositoryDataModel> memberDataModel;

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
		
		showDialog(0);
		if (mAppStatus.isOnline(OrganisationTeamMemberActivity.this)) {	
			//OrganisationMemberTask orgMemberTask=new OrganisationMemberTask(OrganisationMemberActivity.this,strOrganisation);
			//orgMemberTask.execute(strOrganisation);
		
		}
		mRepositoryDBAdapter = new RepositoryDBAdapter(this,
				Constants.MembersTableName);
		mRepositoryDataModel = new RepositoryDataModel();
		mRepositoryDBAdapter.deleteAll();
	}
	
	
	public void memberResponse(String strJsonResponse){
		
		OrganisationMemberParseResult repoParse=new OrganisationMemberParseResult();
		memberDataModel=repoParse.parseRepositoryData(strJsonResponse);

		generateList();
		onListClick();
	}
	
	private void generateList(){
	
		mMemberListAdapter = new RepositoryListAdapter(OrganisationTeamMemberActivity.this,
				memberDataModel);
		listView = (ListView) findViewById(R.id.listViewOrganisationMember);
		mMemberListAdapter.notifyDataSetChanged();
		listView.setAdapter(mMemberListAdapter);
		
	}
	
	
	private void onListClick(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (mAppStatus.isOnline(OrganisationTeamMemberActivity.this)) {
					
					strMember = (memberDataModel.get(position)).toString();
					Log.d("Member name---", "" + strMember);
					
//					Intent i=new Intent(getParent(), BranchActivity.class);
//					
//					i.putExtra("username", userName);
//					i.putExtra("reponame", repoName);				
//					GroupActivity parentActivity = (GroupActivity)getParent();
//					parentActivity.startChildActivity("branch intent", i);
					
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
