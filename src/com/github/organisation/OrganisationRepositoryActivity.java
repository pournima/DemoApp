package com.github.organisation;

import java.util.ArrayList;

import com.github.GroupActivity;
import com.github.R;
import com.github.branch.BranchActivity;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationRepositoryParseResult;
import com.github.members.OrganisationMemberActivity;
import com.github.members.OrganisationMemberTask;
import com.github.repository.RepositoryActivity;
import com.github.repository.RepositoryListAdapter;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrganisationRepositoryActivity extends Activity {

	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;
	
	String organisationRepo;
	String orgRepositoryName;
	String userName;
	
	ListView listView;
	Button btnMember;
	
	AppStatus mAppStatus;
	OrganisationRepositoryDBAdapter mRepositoryDBAdapter;
	OrganisationRepositoryDataModel mRepositoryDataModel;
	
	ArrayList<OrganisationRepositoryDataModel> orgRepositoryData;
	OrganisationRepositoryListAdapter mRepositoryListAdapter;
	
	String strJsonResponse;
	String strOrganisation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repository_layout);
		mAppStatus = AppStatus.getInstance(this);
		userName=mAppStatus.getSharedUserName(Constants.LOGIN_USERNAME);
		
		strJsonResponse=getIntent().getExtras().getString("strJsonResponse");
		strOrganisation=getIntent().getExtras().getString("organisation");
		
		Log.d("repo response", strJsonResponse);
		getOrganisationRepository(strJsonResponse);
		
		onMemberButtonClick();
		
	}
	
	
	private void onMemberButtonClick(){
		btnMember=(Button) findViewById(R.id.buttonMember);
		
		
		btnMember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getParent(), OrganisationMemberActivity.class);

				i.putExtra("organisation", strOrganisation);
				
				GroupActivity parentActivity = (GroupActivity)getParent();
				parentActivity.startChildActivity("orgMember intent", i);
				
			}
		});
		
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

	
	/*---------- Backup button captured ----------------- */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("Backup Button", "Pressed");
			warningDialogBox();
			
		}

		return super.onKeyDown(keyCode, event);
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
				Toast toast = Toast.makeText(OrganisationRepositoryActivity.this, mesage, 8000);
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
