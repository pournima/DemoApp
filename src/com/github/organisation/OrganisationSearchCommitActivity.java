package com.github.organisation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.github.GroupActivity;
import com.github.R;
import com.github.commits.CommitsActivity;
import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.helper.OrganisationUserCommitsParseResult;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrganisationSearchCommitActivity extends Activity {
	
	AppStatus mAppStatus;
	public ProgressDialog mProgressDialog;
	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 999;
	
	TextView txtUserDate;
	Spinner userSpinner;
	DatePicker btnDatePicker;
	Button btnOk;
	ArrayList<OrganisationUserCommitsDataModel> orgUserDataModel;
	List<String> list = new ArrayList<String>();
	
	String strJsonResponse;
	String owner;
	String repoName;
	String branchName;
	String commiterName;
	String date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_commit_layout);
		
		mAppStatus = AppStatus.getInstance(this);
		
		txtUserDate=(TextView) findViewById(R.id.userDate);
		btnDatePicker = (DatePicker) findViewById(R.id.datePickerButton);
		userSpinner = (Spinner) findViewById(R.id.spinnerUserCommits);
		
		txtUserDate.setVisibility(View.INVISIBLE);
		
		strJsonResponse=getIntent().getExtras().getString("response");
		owner=getIntent().getExtras().getString("owner");
		repoName=getIntent().getExtras().getString("reponame");
		branchName=getIntent().getExtras().getString("branchname");
		
		datePicker();
		
		onSearchClick(strJsonResponse);
		 
		onSpinnerClick();
		onClickOk();
	//	dateButtonPicker();
		
		
//		btnDatePicker.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				datePicker();
//				showDialog(DATE_DIALOG_ID);
//			}
//		});
		
		
	}

	 public void onDateSelectedButtonClick(View v){
	        day = btnDatePicker.getDayOfMonth();
	        month = btnDatePicker.getMonth()+1;
	        year = btnDatePicker.getYear();
	        txtUserDate.setText(day+"-"+month+"-"+year);
	       
	       // Toast.makeText(this, day+"/"+month+"/"+year, Toast.LENGTH_SHORT).show();
	    }
	 
	public void onSearchClick(String jsonResponse){
		
		OrganisationUserCommitsParseResult orgUserCommits=new OrganisationUserCommitsParseResult();
		orgUserDataModel=orgUserCommits.parseCommitsData(jsonResponse);

		for (OrganisationUserCommitsDataModel obj : orgUserDataModel) 
		{
			list.add(obj.getName());
		}
		
}
	
	private void onSpinnerClick(){
		// for spinner
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				userSpinner.setAdapter(dataAdapter);
				
				userSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
				
						commiterName=(String) userSpinner.getItemAtPosition(arg2);
						
					//	Toast.makeText(OrganisationSearchCommitActivity.this, commiterName,Toast.LENGTH_SHORT).show();
			
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				} );
			
	}
	
	private void datePicker(){
  
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		// set current date into textview
		// Month is 0 based, just add 1
		
		txtUserDate.setText(new StringBuilder()
			.append(month + 1).append("-").append(day).append("-")
			.append(year).append(" "));
 
		// set current date into datepicker
		btnDatePicker.init(year, month, day, null);
	
	}

	private void onClickOk(){
		
		btnOk=(Button) findViewById(R.id.okButton);
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
							
		        day = btnDatePicker.getDayOfMonth();
		        month = btnDatePicker.getMonth()+1;
		        year = btnDatePicker.getYear();
				txtUserDate.setText(day+"-"+month+"-"+year);
				
				date=day+"-"+month+"-"+year;
				btnOk.setEnabled(false);
				showDialog(0);
				if (mAppStatus.isOnline(OrganisationSearchCommitActivity.this)) {
					OrganisationUserCommitsTask mCommitsTask = new OrganisationUserCommitsTask(OrganisationSearchCommitActivity.this, branchName,owner,repoName,commiterName,date);
					mCommitsTask.execute(branchName);
				}
//				Intent i=new Intent(getParent(),OrganisationUserCommitsActivity.class);
//				
//				i.putExtra("owner", owner);
//				i.putExtra("reponame", repoName);
//				i.putExtra("branchname",branchName);
//				i.putExtra("committer_name",commiterName);
//				i.putExtra("date", date);
//				GroupActivity parentActivity = (GroupActivity)getParent();
//				parentActivity.startChildActivity("orgUserCommits intent", i);
				
			}
		});
		
	}

	
	public void commitsResponce(String strResponse){
		Constants.flagUserCommit=true;
		Intent i=new Intent(getParent(),OrganisationUserCommitsActivity.class);
		i.putExtra("commitResponse", strResponse);
		i.putExtra("owner", owner);
		i.putExtra("reponame", repoName);
		i.putExtra("branchname",branchName);
		i.putExtra("committer_name",commiterName);
		i.putExtra("date", date);
		GroupActivity parentActivity = (GroupActivity)getParent();
		parentActivity.startChildActivity("orgUserSearchCommits intent", i);	
		
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
	
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case DATE_DIALOG_ID:
//		   // set date picker as current date
//		// set date picker as current date
//		   return new DatePickerDialog(this, datePickerListener, 
//                         year, month,day);
//		}
//		return null;
//	}
//	
//	private DatePickerDialog.OnDateSetListener datePickerListener 
//    = new DatePickerDialog.OnDateSetListener() {
//
//		// when dialog box is closed, below method will be called.
//		public void onDateSet(DatePicker view, int selectedYear,
//			int selectedMonth, int selectedDay) {
//		year = selectedYear;
//		month = selectedMonth;
//		day = selectedDay;
//		
//		// set selected date into textview
//		txtUserDate.setText(new StringBuilder().append(month + 1)
//		   .append("-").append(day).append("-").append(year)
//		   .append(" "));
//		
//	
//		// set selected date into datepicker also
//		btnDatePicker.init(year, month, day, null);
//		
//		}
//	};

}
