package com.github;

import com.github.helper.AppStatus;
import com.github.repository.GroupRepositoryActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginInActivity extends Activity {
	
	public ProgressDialog mProgressDialog;
	AppStatus mAppStatus;
	Handler mhandler;
	
	EditText txtUserName;
	Button btnLogin;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		mAppStatus = AppStatus.getInstance(this);
		
		txtUserName=(EditText) findViewById(R.id.editTextUserName);
		btnLogin=(Button) findViewById(R.id.btnLoginIn);
			
		
		txtUserName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// Your validation code goes here
				
				
				if ((txtUserName.getText().toString().trim()).equals("")) {
					Log.i("Empty Text", "@@@@@@@@");
				}
			}
		});
		
		onclickButton();
	}

	private void onclickButton(){
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				String strUserName = txtUserName.getText().toString();
				if (strUserName.equals("")) {
					Log.i("Status Validation: ", "Please Enter username");
					warningDialogBox("Please Enter username  ..!");
					return;
				}
				btnLogin.setEnabled(false);
				strUserName = strUserName.trim();
				validateUserName(strUserName);

			}
		});
	}
	

		public void validateUserName(final String userName) {
			
			if (mAppStatus.isOnline(LoginInActivity.this)) {
				showDialog(0);
				LoginTask mLoginTask = new LoginTask(LoginInActivity.this,userName);
				mLoginTask.execute(userName);
				
			} else {
				dismissDialog(0);
				Log.v("SPLASH_SCREEN", "You are not online!!!!");
				warningDialogBox("Please check you internet connection!!");
			}
		
		}
		
	
//		public void userAuthenticate(String strUserLoginName){
//
//			Intent intent = new Intent(LoginInActivity.this, GroupRepositoryActivity.class); 
//			intent.putExtra("username",strUserLoginName);
//			startActivity(intent);
//			finish();
//			
//		}
//		
//		
//		public void userNotAuthenticate(String strUserName){
//			Intent intentWeb = new Intent(LoginInActivity.this, GitHubAppActivity.class);
//			intentWeb.putExtra("username",strUserName);
//			startActivity(intentWeb);
//			finish();
//		}
		
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
									//moveTaskToBack(true);
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
	
		
		/*-----------onCreateDialog method START ------ */
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

		
		/*-----------onCreateDialog method END ------ */

		/*---------- Backup button captured ----------------- */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				Log.i("Backup Button", "Pressed");
				warningDialogBox();
				
			}

			return super.onKeyDown(keyCode, event);
		}	
		
}
