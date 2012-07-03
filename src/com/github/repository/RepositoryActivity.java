package com.github.repository;

import java.util.ArrayList;
import com.github.GroupActivity;
import com.github.LoginTask;
import com.github.R;
import com.github.branch.BranchActivity;
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
	private ProgressDialog loading;
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
			
			if(Constants.gitflag.booleanValue()){
				showDialog(0);
				if (mAppStatus.isOnline(RepositoryActivity.this)) {
			
					LoginTask mLoginTask = new LoginTask(RepositoryActivity.this,userName);
					mLoginTask.execute(userName);
					
					if(Constants.flagAuthonticate){
						getRepositoryData();
					}
					
				} else {
					
					Log.v("SPLASH_SCREEN", "You are not online!!!!");
					// showLoading(false, "", "");
					warningDialogBox("Please check you internet connection!!");
				}
			}else{

				getRepositoryData();
				
			}
			//generateList();
			//onListClick();
			
		}
		
		
	private void getRepositoryData(){

		mRepositoryDBAdapter = new RepositoryDBAdapter(this,
				Constants.RepositoryTableName);
		mRepositoryDataModel = new RepositoryDataModel();

		try {
			//getting all Repo Data from API into response
			
			//String pageNumber = new Integer(PageNo).toString();
			if (mAppStatus.isOnline(RepositoryActivity.this)) {		
				showDialog(0);
				mRepositoryDBAdapter.deleteAll();
				
				RepositoryTask mRepositoryTask = new RepositoryTask(this, userName);
				mRepositoryTask.execute(userName);
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

				if (mAppStatus.isOnline(RepositoryActivity.this)) {
					
					repoName = (repositoryData.get(position)).toString();
					Log.d("Repository name---", "" + repoName);
					
					//String pageNumber = new Integer(PageNo).toString();
					
					Intent i=new Intent(getParent(), BranchActivity.class);
					
					i.putExtra("username", userName);
					i.putExtra("reponame", repoName);
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
				Toast toast = Toast.makeText(RepositoryActivity.this, mesage, 8000);
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
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		 if(item.getItemId()==R.id.menuLogOut){
//			//***LogOut
//
//			AppStatus mAppStatus=new AppStatus();
//			mAppStatus.clearAuthKey(Constants.AUTH_KEY);
//			
//			Intent intent=new Intent(getApplicationContext(),LoginInActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			//intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			
//			finish();
//			
//		}else if(item.getItemId()==R.id.menuOrganisation){
//			
//			Intent intent=new Intent(getApplicationContext(),OrganisationActivity.class);
//			startActivity(intent);
//		}
//
//		return true;
//
//	}


}
